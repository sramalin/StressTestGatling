package  org.thoughtworks.sampleRT.simulations;

import io.gatling.core.Predef._
import io.gatling.http.Predef._



class dasSignIn extends Simulation {

	
	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8","Content-Type" -> "application/json",
		"Upgrade-Insecure-Requests" -> "1")

	//val authHeaders = Map("Authorization" -> "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjlGWERwYmZNRlQyU3ZRdVhoODQ2WVR3RUlCdyIsImtpZCI6IjlGWERwYmZNRlQyU3ZRdVhoODQ2WVR3RUlCdyJ9.eyJhdWQiOiJjOTc0MTM4OS0wY2M4LTRjNzItYTZjYS03MTBlMTNiN2Y5MWEiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmU1N2JhZC0wMDJhLTQ0YmItODI5Ny04MDViMzYwNTdmY2QvIiwiaWF0IjoxNDk4MTIzMDYwLCJuYmYiOjE0OTgxMjMwNjAsImV4cCI6MTQ5ODEyMzk2MCwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhEQUFBQWllMC9UaUI2NGJtM0prMEZjaytna3VDY0pFM1FzQVFpTStQbDk1Uy92cXc9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImEzMTZhNjA0LTk1YjMtNGExNS1hY2EwLTIyOGQ4MjZjNDg5ZSIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiZGFzaCIsImdpdmVuX25hbWUiOiJEZWJlbmRyYSIsImlwYWRkciI6IjEzLjcxLjEyMi44NyIsIm5hbWUiOiJEZWJlbmRyYSIsIm9pZCI6IjdhYTIwMmUzLTE2MmEtNGNjZi04NGFhLTljNzFmMmJmYzEzZSIsInBsYXRmIjoiMTQiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiIzU1ZfNVZHUFBWbEdTdUhnM0g1ZDNjcmlhV28zRGQyZ01NRVlqaXBrc1hvIiwidGlkIjoiMmZlNTdiYWQtMDAyYS00NGJiLTgyOTctODA1YjM2MDU3ZmNkIiwidW5pcXVlX25hbWUiOiI3ZjEyYzUxNS04ZGUyLTRmN2QtYmY4ZS05ODA1NWNiZDA4ZDJAZ3Jhbml0ZXRlbmFudGRldi5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiI3ZjEyYzUxNS04ZGUyLTRmN2QtYmY4ZS05ODA1NWNiZDA4ZDJAZ3Jhbml0ZXRlbmFudGRldi5vbm1pY3Jvc29mdC5jb20iLCJ2ZXIiOiIxLjAifQ.JjoBk9xZqpA1-IqgyoYPcuMynuUsdHS82jHN-d7sRHQTjN_07ta_xDn-Cj8iBhOZCuEvlSZjZz_6jT820hBzNWq3a2uOYcW5tIe6zVmsKE7yeloGM_A6nYaNStVtcZoXi8i_utFXLRtz5qL8bM28aEEB7hDTx4wCwdUHP-rGpBXNWAG4WhG6aZO7MfwsggKZL04LGfa82uIkUShhcwr3gnZ8W-0lTqd3n9Mr57q_ZJOhiEWq_AS_BbG8yMhkHVE_b9upN1Khb74NYiUzj9iV2TIvKDstyUuPfb6-0-sTTCP29phl733D44uSjnT-OmAVSQ0SX_obTLr7N3E4_YlrJw")

  val contentType = Map("Content-Type" -> "application/json")
  val apimSubscriptionKey = Map("Ocp-Apim-Subscription-Key" -> "823ed70bf7134f1db011c3a4f892dc64")

	val headers_3 = Map("Pragma" -> "no-cache")

	val headers_6 = Map("Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

    val uri1 = "https://graniteqa.azure-api.net/qa-staging"

	val scn = scenario("DAS signin")
      .exec(http("DAS sign in")
      .post(uri1 + "/signin")
//      .headers(headers_0)
      .headers(contentType)
      .headers(apimSubscriptionKey)
      .body(StringBody("""{ "username": "test1234@grr.la","password":"Password.1234" }""")).asJSON

        .check(status.is(200))
      .check(jsonPath("$.accessToken").saveAs("response1")))
  	.exec(session => {
      val maybeaccessToken = "Bearer "+session.get("response1").as[String]
      println(maybeaccessToken)
      session
    })


//	setUp(scn.inject(atOnceUsers(5)))
//	  .assertions(global.responseTime.max.lt(3000))
//    .assertions(global.failedRequests.count.is(0)
//    )


  setUp(scn.inject(atOnceUsers(1)))
  	  .assertions(global.responseTime.max.lt(5000))
      .assertions(global.failedRequests.count.is(0))
      .assertions(forAll.responseTime.max.lt(3000))

	  


}