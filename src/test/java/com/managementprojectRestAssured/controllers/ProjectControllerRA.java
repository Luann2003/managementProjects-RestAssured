package com.managementprojectRestAssured.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.managementprojectRestAssured.tests.TokenUtil;

import io.restassured.http.ContentType;

public class ProjectControllerRA {
	
	private String memberUsername, memberPassword, adminUsername, adminPassword;
	private String adminToken, memberToken, invalidToken;
	
	private Long existingProjectId, nonExistingProjectId, dependentProjectId;
	
	private String projectName;
	
	private Map<String, Object> postProjectInstance;
	private Map<String, Object> putProjectInstance;

	@BeforeEach
	public void setup() throws JSONException {
		
		baseURI = "http://localhost:8080";
		projectName = "test 1";
		
		memberUsername = "maria@gmail.com";
		memberPassword = "123456";
		
		adminUsername = "verfute2005@gmail.com";
		adminPassword = "123456";
		
		memberToken = TokenUtil.obtainAccessToken(memberUsername, memberPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "xpto";
		
		postProjectInstance = new HashMap<>();
		postProjectInstance.put("name", "meu projeto");
		postProjectInstance.put("description", "test Description RA");
		postProjectInstance.put("startDate", "2020-07-13T20:50:07.12345Z");
		postProjectInstance.put("finishDate", "2020-08-13T20:50:07.12345Z");
		
		putProjectInstance = new HashMap<>();
		putProjectInstance.put("name", "meu projeto 2");
		putProjectInstance.put("description", "test Description RA 2");
		putProjectInstance.put("startDate", "2022-07-13T20:50:07.12345Z");
		putProjectInstance.put("finishDate", "2022-08-13T20:50:07.12345Z");
		
	}
	
	@Test
	public void findByIdShouldReturnProjectWhenIdExisting() {

		existingProjectId = 2L;

			given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/projects/{id}", existingProjectId)
		.then()
			.statusCode(200)
			.body("name", equalTo("test 2"))
			.body("description", equalTo("test descrição 2"))
			.body("startDate", equalTo("2020-08-13T22:33:07.123450Z"))
			.body("finishDate", equalTo("2020-07-15T16:15:22.123450Z"))
			.body("tasks.id", hasItems(2,3));
	}
	
	@Test
	public void findByIdShouldReturnProjectWhenIdNonExisting() {

		nonExistingProjectId = 100L;

			given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/projects/{id}", nonExistingProjectId)
		.then()
			.statusCode(404);
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminOrMemberLogged() {

		nonExistingProjectId = 100L;

			given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/projects/{id}", nonExistingProjectId)
		.then()
			.statusCode(401);
	}
	
	@Test
	public void findAllShouldReturnPagedProjectsWhenProjectsNameParamIsEmpty() {
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.get("/projects?page=0")
		.then()
			.statusCode(200)
			.body("content.name", hasItems("test 1", "test 2"));
	}
	
	@Test
	public void findAllShouldReturnPagedProjectsWhenProjectsNameParamIsNotEmpty() {
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.get("/projects?name={projectName}", projectName)
		.then()
			.statusCode(200)
			.body("content.id[0]", is(1))
			.body("content.name[0]", equalTo("test 1"));
	}
	
	
	
	@Test
	public void insertShouldReturnProjectCreatedWhenLoggedAsAdmin() {
		JSONObject newProduct = new JSONObject(postProjectInstance);
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/projects")
		.then()
			.statusCode(201);

	}
	
	@Test
	public void  insertShouldReturnUnauthorizedWhenInvalidToken() {
		JSONObject newProduct = new JSONObject(postProjectInstance);
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/projects")
		.then()
			.statusCode(401);

	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidName() {
		postProjectInstance.put("name", "");
		JSONObject newProduct = new JSONObject(postProjectInstance);
		
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/projects")
		.then()
			.statusCode(422)
			.body("errors.message[0]", equalTo("Campo obrigatório"));	
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidDate() {
		
		postProjectInstance.put("finishDate", "2020-06-13T20:50:07.12345Z");
		JSONObject newProduct = new JSONObject(postProjectInstance);
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/projects")
		.then()
			.statusCode(422);
	}
	
	@Test
	public void updateShouldReturnProjectCreatedWhenLoggedAsAdmin() {
		JSONObject newProduct = new JSONObject(putProjectInstance);
		
		existingProjectId = 3L;
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/projects/{id}", existingProjectId)
		.then()
			.statusCode(200);
			
	}
	
	@Test
	public void updateShouldReturnForbbidenWhenLoggedAsMember() {
		JSONObject newProduct = new JSONObject(putProjectInstance);
		
		existingProjectId = 3L;
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + memberToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/projects/{id}", existingProjectId)
		.then()
			.statusCode(403);
			
	}
	
	@Test
	public void deleteShouldReturnNotContentWhenIdExistingAndAdminLogged() {
		existingProjectId = 5L;
		
		given()
			.header("Authorization", "Bearer " + adminToken)
		.when()
			.delete("/projects/{id}", existingProjectId)
		.then()
			.statusCode(204);	
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() throws Exception {
		nonExistingProjectId = 100L;
		
		given()
			.header("Authorization", "Bearer " + adminToken)
		.when()
			.delete("/projects/{id}", nonExistingProjectId)
		.then()
			.statusCode(404)
			.body("status", equalTo(404));
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenIdIsDependentAndAdminLogged() throws Exception {
		dependentProjectId = 3L;
		
		given()
			.header("Authorization", "Bearer " + adminToken)
		.when()
			.delete("/projects/{id}", dependentProjectId)
		.then()
			.statusCode(400);
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenIdExistsAndClientLogged() throws Exception {
		existingProjectId = 5L;
		
		given()
			.header("Authorization", "Bearer " + memberToken)
		.when()
			.delete("/projects/{id}", existingProjectId)
		.then()
			.statusCode(403);
	}
	
	@Test
	public void deleteShouldReturnUnauthorizedWhenIdExistsAndInvalidToken() throws Exception {
		existingProjectId = 25L;
		
		given()
			.header("Authorization", "Bearer " + invalidToken)
		.when()
			.delete("/projects/{id}", existingProjectId)
		.then()
			.statusCode(401);
	}
	


}
