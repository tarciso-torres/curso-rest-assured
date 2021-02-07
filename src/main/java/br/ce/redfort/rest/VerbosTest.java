package br.ce.redfort.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

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
	public void deveSalvarUsuarioViaXMLUsandoObjeto() {
		User usuario = new User("Usuario via XML", 40);
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(usuario)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;
		
		assertThat(usuarioInserido.getId(), is(notNullValue()));
		assertEquals(usuarioInserido.getName(), "Usuario via XML");
		assertThat(usuarioInserido.getAge(), is(40));
	}

	@Test
	public void deveSalvarUsuarioUsandoMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuário via map");
		params.put("age", 25);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuário via map"))
			.body("age", is(25))
		;
	}

	@Test
	public void deveSalvarUsuarioUsandoObjeto() {
		User user = new User("Usuário via objeto", 35);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuário via objeto"))
			.body("age", is(35))
		;
	}

	@Test
	public void deveDeserializarObjetoSalvarUsuario() {
		User user = new User("Usuário via objeto", 35);
		
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.extract().body().as(User.class)
		;
		
		assertThat(usuarioInserido.getId(), is(notNullValue()));
		assertEquals(usuarioInserido.getName(), "Usuário via objeto");
		assertThat(usuarioInserido.getAge(), is(35));
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
			.body("error", is("Name é um atributo obrigatório"))
		;
	}

	@Test
	public void deveAlterarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuário Alterado\", \"age\": 80}")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuário Alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}

	@Test
	public void deveCustomizarURLParte1() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuário Alterado\", \"age\": 80}")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{id}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuário Alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}

	@Test
	public void deveCustomizarURLParte2() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuário Alterado\", \"age\": 80}")
			.pathParam("entidade", "users")
			.pathParam("id", "1")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{id}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuário Alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		;
	}
}
