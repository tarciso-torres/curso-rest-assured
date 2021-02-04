package br.ce.redfort.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

import org.junit.Test;

public class UserJsonTest {

	@Test
	public void deveVerificarPrimeiroNivel() {
		given() // Pré condições
		.when() // Ação
			.get("https://restapi.wcaquino.me/users/1")
		.then() // Assertivas
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18))
		;
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		given() // Pré condições
		.when() // Ação
			.get("https://restapi.wcaquino.me/users/2")
		.then() // Assertivas
			.statusCode(200)
			.body("id", is(2))
			.body("name", containsString("Joaquina"))
			.body("age", greaterThan(18))
			.body("endereco.rua", is("Rua dos bobos"))
		;
	}
	
	@Test
	public void deveVerificarLista() {
		given() // Pré condições
		.when() // Ação
			.get("https://restapi.wcaquino.me/users/3")
		.then() // Assertivas
			.statusCode(200)
			.body("id", is(3))
			.body("name", containsString("Ana"))
			.body("age", greaterThan(18))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"))
		;
	}
	
	@Test
	public void deveRetornarErroUsuarioInexistente() {
		given() // Pré condições
		.when() // Ação
			.get("https://restapi.wcaquino.me/users/4")
		.then() // Assertivas
			.statusCode(404)
			.body("error", is("Usuário inexistente"))
		;
	}
	
	@Test
	public void deveVerificarListaRaiz() {
		given() // Pré condições
		.when() // Ação
			.get("https://restapi.wcaquino.me/users")
		.then() // Assertivas
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
			.body("filhos[2].name", hasItems("Zezinho", "Luizinho"))
			.body("salary", contains(1234.5678f, 2500, null))
		;
	}

}
