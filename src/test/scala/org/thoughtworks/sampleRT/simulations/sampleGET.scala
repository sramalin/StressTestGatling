package  org.thoughtworks.sampleRT.simulations;

import io.gatling.core.Predef._
import io.gatling.http.Predef._



class SampleGET extends Simulation {

	val httpProtocol = http
		.baseURL("http://detectportal.firefox.com")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:53.0) Gecko/20100101 Firefox/53.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map("Accept" -> "text/css,*/*;q=0.1")

	val headers_3 = Map("Pragma" -> "no-cache")

	val headers_6 = Map("Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

    val uri1 = "http://localhost:8085"

	val scn = scenario("RecordedSimulation")
      .exec(http("Get Tickets")
      .get(uri1 + "/web/ticket/view")
      .headers(headers_0)
      .check(status.is(200)))

	setUp(scn.inject(atOnceUsers(5)))
		.protocols(httpProtocol)
	  .assertions(global.responseTime.max.lt(2000))
	  .assertions(global.successfulRequests.percent.gte(95))


}