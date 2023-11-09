package com.managementprojectRestAssured.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.managementprojectRestAssured.tests.TokenUtil;

import io.restassured.http.ContentType;

public class AuthControllerRA {

	private String memberUsername, memberPassword, adminUsername, adminPassword;
	private String adminToken, memberToken, invalidToken;
	
	private Long existingProjectId, nonExistingProjectId, dependentProjectId;
	
	private String projectName;
	
	private Map<String, Object> postRecoverTokenInstance;
	private Map<String, Object> postNewPasswrodInstance;
	
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
		
		postRecoverTokenInstance = new HashMap<>();
		postRecoverTokenInstance.put("email", "verfute2005@gmail.com");
		
		postNewPasswrodInstance = new HashMap<>();
		postNewPasswrodInstance.put("token", adminToken);
		postNewPasswrodInstance.put("password", "abcd1234" );
		
	}
	
	@Test
	public void createRecoverTokenShouldReturnToken() {
		
		JSONObject newProduct = new JSONObject(postRecoverTokenInstance);
	
		given()
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/auth/recover-token")
		.then()
			.statusCode(204);
	}
	
	@Test
	public void createRecoverTokenShouldReturnResourceNotFoundWhenEmailDoesNotExisting() {
		postRecoverTokenInstance.put("email", "emailNotExisting@gmail.com");
		JSONObject newProduct = new JSONObject(postRecoverTokenInstance);
	
		given()
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/auth/recover-token")
		.then()
			.statusCode(404);
	}
	
	
	
	@Test
	public void saveNewPasswordShouldReturn() {
		
//		JSONObject newProduct = new JSONObject(postNewPasswrodInstance);
//	
//		given()
//			.body(newProduct)
//			.contentType(ContentType.JSON)
//			.accept(ContentType.JSON)
//		.when()
//			.put("/auth/new-password")
//		.then()
//			.statusCode(204);

	}
	
}
