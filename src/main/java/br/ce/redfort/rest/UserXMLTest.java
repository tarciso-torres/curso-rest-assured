package br.ce.redfort.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class UserXMLTest {
	
	@Test
	public void devoTrabalharComXML() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/usersXML/3")
		.then()
			.statusCode(200)
			.rootPath("user")
			.body("name", is("Ana Julia"))
			.body("@id", is("3"))
			
			.rootPath("user.filhos")
			.body("name.size()", is(2))
			
			.detachRootPath("filhos")
			.body("filhos.name[0]", is("Zezinho"))
			.body("filhos.name[1]", is("Luizinho"))
			
			.appendRootPath("filhos")
			.body("name", hasItem("Zezinho"))
			.body("name", hasItems("Zezinho", "Luizinho"))
		;
		
	}

}
