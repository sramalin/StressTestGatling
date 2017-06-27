package  org.thoughtworks.sampleRT.simulations;

import io.gatling.core.Predef._
import io.gatling.http.Predef._



class deviceStatus extends Simulation {


  val headers_0 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Upgrade-Insecure-Requests" -> "1")
  val apimSubscriptionKey = Map("Ocp-Apim-Subscription-Key" -> "ea5a84106acb412a9d64d9e8c693962d")

  //val apimSubscriptionKey = Map("Ocp-Apim-Subscription-Key" -> "823ed70bf7134f1db011c3a4f892dc64")
  val headers_3 = Map("Pragma" -> "no-cache")
  val headers_6 = Map("Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
  val uri1 = "https://granitedev.azure-api.net/dev-stub"
  //val uri1 = "https://graniteqa.azure-api.net/qa-staging"
  val userName = "debendra250@grr.la"
  //val userName = "test1234@grr.la"
  val password = "Password.1234"
  var maybeaccessToken: String = ""
  var prefix: String = "Bearer"
  //val attribute: SessionAttribute = session("authToken")


  val contentType = Map("Content-Type" -> "application/json")

  val getAccessToken = scenario("Scenario 1")
    .exec(http("DAS sign in")
      .post(uri1 + "/signin")
      .headers(contentType)
      .headers(apimSubscriptionKey)
      //.body(StringBody("{ 'username':" +userName+",password:"+password+"}")).asJSON
      //.body(StringBody("""{ "username": "test1234@grr.la","password":"Password.1234" }""")).asJSON
      .body(StringBody("""{ "username": "debendra250@grr.la","password":"Password.1234" }""")).asJSON
      .check(status.is(200))
      .check(jsonPath("$.accessToken").saveAs("response1")))
    .exec(session => session.set("authToken1", session.get("response1").as[String]))

    .exec(ObjectB.bar)

    object ObjectB {
  val bar =group("Get Device status") {
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