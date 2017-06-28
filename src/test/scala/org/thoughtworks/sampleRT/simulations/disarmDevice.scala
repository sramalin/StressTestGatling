package org.thoughtworks.sampleRT.simulations

import java.io.File

import com.typesafe.config._
import io.gatling.commons.stats.KO
import io.gatling.core.Predef._
import io.gatling.http.Predef._


class disarmDevice extends Simulation {

  val targetEnv = System.getProperty("targetEnv")
  var file:File = new File(targetEnv+".conf")
  val conf = ConfigFactory.parseFile(file)
  val uri1 =conf.getString("BaseURL")
  val userName = conf.getString("userName")
  val password = conf.getString("password")
  val subscriptionKey = conf.getString("apimSubscriptionKey")
  val apimSubscriptionKey = Map("Ocp-Apim-Subscription-Key" -> subscriptionKey)
  var maybeaccessToken: String = ""
  val contentType = Map("Content-Type" -> "application/json")
  val activityID = Map("ActivityId" -> "ee099ac7-2959-4688-9fa1-dbb11ad7e8ff")
  var errorSet = scala.collection.immutable.HashSet("")



  val getAccessToken = scenario("Arm device")
      .exec{session =>session.set("username1",userName)}
      .exec{session =>session.set("password1",password)}
      .exec(http("DAS sign in")
      .post(uri1 + "/signin")
      .headers(contentType)
      .headers(apimSubscriptionKey)
      .body(StringBody("""{ "username": "${username1}","password":"${password1}" }""")).asJSON
      .check(status.is(200))
      .check(jsonPath("$.accessToken").saveAs("response1")))
      .exec(session => session.set("authToken1", session.get("response1").as[String]))
      .exec(DisArmObject.disarmDevice)

  object DisArmObject {
    val authToken = "Bearer " + "${authToken1}"
  val disarmDevice =scenario("DisArm Device")
                .exec{session =>session.set("data1","{'PartitionId':0}")}
                .exec(http("DisArm device scenario")
                .post(uri1 + "/api/devices/00D02D12345")
                .headers(contentType)
                .headers(Map("Authorization" -> authToken))
                .headers(apimSubscriptionKey)
                .headers(activityID)
      //.body(StringBody("""{ "Name": "Away","Data":"{ "PartitionId":":0"}" }""")).asJSON
                .body(StringBody("""{ "Name": "Home","Data":"${data1}" }""")).asJSON
                .check(status.is(200))
                .extraInfoExtractor {
                  extraInfo =>
                    extraInfo.status match {
                      case KO =>

                      {
                        var errorLabel = extraInfo.requestName+"-"+extraInfo.response.statusCode.getOrElse("501")
                        if(!errorSet.contains(errorLabel)){
                          errorSet += errorLabel
                          println("Error Label: "+errorLabel)
                          println("Response Body"+extraInfo.response.body.toString)
                          println("Response Body status"+extraInfo.response.status.get.getStatusText())
                        }
                      }
                        Nil
                      case _ => Nil
                    }
                }
                )

}



  setUp(getAccessToken.inject(rampUsers(1) over (1)))
  	  .assertions(global.responseTime.max.lt(5000))
      .assertions(global.failedRequests.count.is(0))
      .assertions(forAll.responseTime.max.lt(3000))



}