package br.ce.redfort.rest;

import static io.restassured.RestAssured.given;

import org.junit.Test;
import org.xml.sax.SAXParseException;

import io.restassured.matcher.RestAssuredMatchers;

public class SchemaTest {
	
	@Test
	public void deveValidarSchemaXML() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
		;
	}
	
	@Test(expected = SAXParseException.class)
	public void naoDeveValidarSchemaXMLInvalido() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/invalidUsersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
		;
	}

}
