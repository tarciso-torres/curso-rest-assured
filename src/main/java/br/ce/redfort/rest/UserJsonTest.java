package br.ce.redfort.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class UserJsonTest {

	@Test
	public void deveVerificarPrimeiroNivel() {
		given() // Pr� condi��es
		.when() // A��o
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
		given() // Pr� condi��es
		.when() // A��o
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
		given() // Pr� condi��es
		.when() // A��o
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
		given() // Pr� condi��es
		.when() // A��o
			.get("https://restapi.wcaquino.me/users/4")
		.then() // Assertivas
			.statusCode(404)
			.body("error", is("Usu�rio inexistente"))
		;
	}
	
	@Test
	public void deveVerificarListaRaiz() {
		given() // Pr� condi��es
		.when() // A��o
			.get("https://restapi.wcaquino.me/users")
		.then() // Assertivas
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("Jo�o da Silva", "Maria Joaquina", "Ana J�lia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
			.body("filhos[2].name", hasItems("Zezinho", "Luizinho"))
			.body("salary", contains(1234.5678f, 2500, null))
		;
	}
	
	@Test
	public void deveFazerVerificacaosAvancadas() {
		given() // Pr� condi��es
		.when() // A��o
			.get("https://restapi.wcaquino.me/users")
		.then() // Assertivas
			.statusCode(200)
			.body("$", hasSize(3))
			.body("age.findAll{it <= 25}.size()", is(2))
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
			.body("findAll{it.age <= 25}[-1].name", is("Ana J�lia"))
			.body("find{it.age <= 25}.name", is("Maria Joaquina"))
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana J�lia"))
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
			.body("age.collect{it * 2}", hasItems(60, 50, 40))
			.body("id.max()", is(3))
			.body("salary.min()", is(1234.5678f))
			.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))
			.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
		;
	}
	
	@Test
	public void devoUnirJsonPathComJava() {
		ArrayList<String> names = 
				given()
				.when()
					.get("https://restapi.wcaquino.me/users")
				.then() // Assertivas
					.statusCode(200)
					.extract().path("name.findAll{it.startsWith('Maria')}")
				;
		
		Assert.assertEquals(1, names.size());
		Assert.assertTrue(names.get(0).equalsIgnoreCase("Maria joaquina"));
		Assert.assertEquals(names.get(0).toUpperCase(), "Maria joaquina".toUpperCase());
	}

}
