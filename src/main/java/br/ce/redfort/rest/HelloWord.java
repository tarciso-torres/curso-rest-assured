package br.ce.redfort.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class HelloWord {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		System.out.println("Resposta da requisição: " + response.getBody().asString() + " Status da requição: " + response.getStatusCode());
	}

}
