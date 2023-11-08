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

public class TaskControllerRA {
	
	private String memberUsername, memberPassword, adminUsername, adminPassword;
	private String adminToken, memberToken, invalidToken;
	
	private Long existingTaskId, nonExistingTaskId, dependentTaskId;
	
	private String taskName;
	
	private Map<String, Object> postTaskInstance;
	private Map<String, Object> putTaskInstance;
	
	@BeforeEach
	public void setup() throws JSONException {
		
		baseURI = "http://localhost:8080";
		taskName = "task 1";
		
		memberUsername = "maria@gmail.com";
		memberPassword = "123456";
		
		adminUsername = "verfute2005@gmail.com";
		adminPassword = "123456";
		
		memberToken = TokenUtil.obtainAccessToken(memberUsername, memberPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "xpto";
		
		postTaskInstance = new HashMap<>();
		postTaskInstance.put("projectId", 1L);
		postTaskInstance.put("responsibleId", 3L);
		postTaskInstance.put("name", "X3 24");
		postTaskInstance.put("description", "test Description task inserteee");
		postTaskInstance.put("startDate", "2020-07-13T20:50:07.12345Z");
		postTaskInstance.put("finishDate", "2020-08-13T20:50:07.12345Z");
		postTaskInstance.put("completed", 1L);
		
		putTaskInstance = new HashMap<>();
		putTaskInstance.put("projectId", 2L);
		putTaskInstance.put("responsibleId", 1L);
		putTaskInstance.put("name", "X11");
		putTaskInstance.put("description", "testsssssss Description task inserteee");
		putTaskInstance.put("startDate", "2020-08-13T20:50:07.12345Z");
		putTaskInstance.put("finishDate", "2020-09-13T20:50:07.12345Z");
		putTaskInstance.put("completed", 1L);
	}
	
	@Test
	public void findByIdShouldReturnTaskWhenIdExisting() {

		existingTaskId = 2L;

			given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/task/{id}", existingTaskId)
		.then()
			.statusCode(200)
			.body("id", is(2))
			.body("name", equalTo("task 2"))
			.body("description", equalTo(" descrição da tarefa do projeto 2"))
			.body("startDate", equalTo("2020-07-13T20:50:07.123450Z"))
			.body("finishDate", equalTo("2020-07-15T14:33:25.123450Z"))
			.body("projectId", is(2))
			.body("projectName",equalTo("test 2"));
	}
	
	@Test
	public void findByIdShouldReturnTaskWhenIdNonExisting() {

		nonExistingTaskId = 100L;

			given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/task/{id}", nonExistingTaskId)
		.then()
			.statusCode(404);
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminOrMemberLogged() {

		nonExistingTaskId = 100L;

			given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/task/{id}", nonExistingTaskId)
		.then()
			.statusCode(401);
	}
	
	@Test
	public void findAllShouldReturnPagedProjectsWhenProjectsNameParamIsEmpty() {
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.get("/task?page=0")
		.then()
			.statusCode(200)
			.body("content.name", hasItems("task 1", "task 2"));
	}
	
	@Test
	public void findAllShouldReturnPagedTaskssWhenProjectsNameParamIsNotEmpty() {
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.get("/task?name={projectName}", taskName)
		.then()
			.statusCode(200)
			.body("content.id[0]", is(1))
			.body("content.name[0]", equalTo("task 1"));
	}
	
	@Test
	public void insertShouldReturnTaskCreatedWhenLoggedAsAdmin() {
		JSONObject newProduct = new JSONObject(postTaskInstance);
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/task")
		.then()
			.statusCode(201);
	}
	
	@Test
	public void insertShouldReturnTaskReturnUnauthorizedWhenInvalidToken() {
		JSONObject newProduct = new JSONObject(postTaskInstance);
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/task")
		.then()
			.statusCode(401);
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidName() {
		postTaskInstance.put("name", "");
		JSONObject newProduct = new JSONObject(postTaskInstance);
		
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/task")
		.then()
			.statusCode(422)
			.body("errors.message[0]", equalTo("Campo obrigatório"));	
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidDate() {
		
		postTaskInstance.put("finishDate", "2020-06-13T20:50:07.12345Z");
		JSONObject newProduct = new JSONObject(postTaskInstance);
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/task")
		.then()
			.statusCode(422);
	}
	
	@Test
	public void updateShouldReturnTaskCreatedWhenLoggedAsAdmin() {
		JSONObject newProduct = new JSONObject(putTaskInstance);
		
		existingTaskId = 3L;
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/task/{id}", existingTaskId)
		.then()
			.statusCode(200);	
	}
	
	@Test
	public void updateShouldReturnForbbidenWhenLoggedAsMember() {
		JSONObject newProduct = new JSONObject(putTaskInstance);
		
		existingTaskId = 3L;
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + memberToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/task/{id}", existingTaskId)
		.then()
			.statusCode(403);	
	}
	
	@Test
	public void deleteShouldReturnNotContentWhenIdExistingAndAdminLogged() {
		existingTaskId = 5L;
		
		given()
			.header("Authorization", "Bearer " + adminToken)
		.when()
			.delete("/task/{id}", existingTaskId)
		.then()
			.statusCode(204);	
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() throws Exception {
		nonExistingTaskId = 100L;
		
		given()
			.header("Authorization", "Bearer " + adminToken)
		.when()
			.delete("/task/{id}", nonExistingTaskId)
		.then()
			.statusCode(404)
			.body("status", equalTo(404));
	}
	
	
	@Test
	public void deleteShouldReturnForbiddenWhenIdExistsAndMemberLogged() throws Exception {
		existingTaskId = 5L;
		
		given()
			.header("Authorization", "Bearer " + memberToken)
		.when()
			.delete("/task/{id}", existingTaskId)
		.then()
			.statusCode(403);
	}
	
	@Test
	public void deleteShouldReturnUnauthorizedWhenIdExistsAndInvalidToken() throws Exception {
		existingTaskId = 25L;
		
		given()
			.header("Authorization", "Bearer " + invalidToken)
		.when()
			.delete("/task/{id}", existingTaskId)
		.then()
			.statusCode(401);
	}

}
