package  org.thoughtworks.sampleRT.simulations;

import java.io.File
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.typesafe.config._




class deviceStatus extends Simulation {

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



  val getAccessToken = scenario("Scenario 1")
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
      .exec(scn2.deviceStatus)

  object scn2 {
  val deviceStatus =group("Get Device status") {
      val authToken = "Bearer " + "${authToken1}"
      exec(http("Get Device status")
      .get(uri1 + "/api/devices/00d02d9d12d7")
      .headers(Map("Authorization" -> authToken))
      .headers(apimSubscriptionKey)
      .check(status.is(200))

    )
  }
}



  setUp(getAccessToken.inject(rampUsers(1) over (1)))
  	  .assertions(global.responseTime.max.lt(5000))
      .assertions(global.failedRequests.count.is(0))
      .assertions(forAll.responseTime.max.lt(3000))



}