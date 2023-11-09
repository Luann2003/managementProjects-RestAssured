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

public class UserControllerRA {

	private String memberUsername, memberPassword, adminUsername, adminPassword;
	private String adminToken, memberToken, invalidToken;
	
	private Long existingUserId, nonExistingUserId;
	
	private Map<String, Object> postUserInstance;
	private Map<String, Object> putUserInstance;
	
	@BeforeEach
	public void setup() throws JSONException {
		
		baseURI = "http://localhost:8080";
		
		memberUsername = "maria@gmail.com";
		memberPassword = "123456";
		
		adminUsername = "verfute2005@gmail.com";
		adminPassword = "123456";
		
		memberToken = TokenUtil.obtainAccessToken(memberUsername, memberPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "xpto";
		
		postUserInstance = new HashMap<>();
		postUserInstance.put("name", "Bob");
		postUserInstance.put("email", "bob@gmail.com");
		postUserInstance.put("password", "123456");
		
		putUserInstance = new HashMap<>();
		putUserInstance.put("name", "Ana");
		putUserInstance.put("email", "Ana@gmail.com");
		putUserInstance.put("password", "123456");
		putUserInstance.put("roles.id[0]", "1");
		putUserInstance.put("roles.id[1]", "2");
	}
	
	@Test
	public void findMeShouldReturnUserLogged() {

			given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/users/me")
		.then()
			.statusCode(200)
			.body("id", is(3))
			.body("name", equalTo("Luan Victor"))
			.body("email", equalTo("verfute2005@gmail.com"))
			.body("roles.id", hasItems(1,2,3));

	}
	
	@Test
	public void findByIdShouldReturnUserWhenIdExisting() {

		existingUserId = 3L;

			given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/users/{id}", existingUserId)
		.then()
			.statusCode(200);
	}
	
	@Test
	public void findByIdShouldReturnUserWhenIdDoestNotExisting() {

		nonExistingUserId =1003L;

			given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/users/{id}", nonExistingUserId)
		.then()
			.statusCode(404);
	}
	
	@Test
	public void findAllShouldReturnPagedUsers() {
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.get("/users")
		.then()
			.statusCode(200)
			.body("content.name", hasItems("Maria Brown", "Alex Green"));
	}
	
	@Test
	public void insertShouldReturnUserCreated() {
		JSONObject newProduct = new JSONObject(postUserInstance);
		
		given()
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/users")
		.then()
			.statusCode(201);
	}
	
	@Test
	public void insertShouldReturnUnathorizedWhenEmailExisting() {
		postUserInstance.put("email", "verfute2005@gmail.com");
		JSONObject newProduct = new JSONObject(postUserInstance);
		
		given()
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/users")
		.then()
			.statusCode(422);
	}
	
	@Test
	public void insertShouldReturnUnathorizedWhenNameIsEmpty() {
		postUserInstance.put("name", "");
		JSONObject newProduct = new JSONObject(postUserInstance);
		
		given()
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/users")
		.then()
			.statusCode(422);
	}
	
	@Test
	public void updateShouldReturnUsersCreatedWhenLoggedAsAdmin() {
		JSONObject newProduct = new JSONObject(putUserInstance);
		
		existingUserId = 4L;
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/users/{id}", existingUserId)
		.then()
			.statusCode(200);	
	}
	
	@Test
	public void updateShouldReturnResourceNotFoundWhenIdDoesNotExisting() {
		JSONObject newProduct = new JSONObject(putUserInstance);
		
		nonExistingUserId = 100L;
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/users/{id}", nonExistingUserId)
		.then()
			.statusCode(404);	
	}
	
	@Test
	public void updateShouldReturnForbbidenWhenLoggedAsMember() {
		putUserInstance.put("email", "joaozin@gmail.com");
		JSONObject newProduct = new JSONObject(putUserInstance);
		
		existingUserId = 4L;
		
		given()
			.header("Content.type", "application/json")
			.header("Authorization", "Bearer " + memberToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/users/{id}", existingUserId)
		.then()
			.statusCode(403);	
	}
	
	
	@Test
	public void deleteShouldReturnNotContentWhenIdExistingAndAdminLogged() {
		existingUserId = 4L;
		
		given()
			.header("Authorization", "Bearer " + adminToken)
		.when()
			.delete("/users/{id}", existingUserId)
		.then()
			.statusCode(204);	
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() throws Exception {
		nonExistingUserId = 100L;
		
		given()
			.header("Authorization", "Bearer " + adminToken)
		.when()
			.delete("/users/{id}", nonExistingUserId)
		.then()
			.statusCode(404)
			.body("status", equalTo(404));
	}
	
	
	@Test
	public void deleteShouldReturnForbiddenWhenIdExistsAndMemberLogged() throws Exception {
		existingUserId = 4L;
		
		given()
			.header("Authorization", "Bearer " + memberToken)
		.when()
			.delete("/users/{id}", existingUserId)
		.then()
			.statusCode(403);
	}
	
	@Test
	public void deleteShouldReturnUnauthorizedWhenIdExistsAndInvalidToken() throws Exception {
		existingUserId = 25L;
		
		given()
			.header("Authorization", "Bearer " + invalidToken)
		.when()
			.delete("/users/{id}", existingUserId)
		.then()
			.statusCode(401);
	}
}
