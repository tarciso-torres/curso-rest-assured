package br.ce.redfort.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class VerbosTest {

	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Jose\", \"age\": 50}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		;
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": 50}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name � um atributo obrigat�rio"))
		;
	}

	@Test
	public void deveAlterarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usu�rio Alterado\", \"age\": 80}")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usu�rio Alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}

	@Test
	public void deveCustomizarURLParte1() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usu�rio Alterado\", \"age\": 80}")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{id}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usu�rio Alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}
}
