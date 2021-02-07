package br.ce.redfort.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.junit.Test;

public class FileTest {
	
	@Test
	public void deveObrigarEnvioArquivo() {
		given()
			.log().all()
		.when()
			.post("https://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404)
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void deveFazerUploadArquivo() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("https://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("users.pdf"))
		;
	}
	
	@Test
	public void naoDeveFazerUploadArquivoGrande() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/clean-flutter-app--60.zip"))
		.when()
			.post("https://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(413)
		;
	}

}
