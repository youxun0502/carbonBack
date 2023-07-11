package com.liu.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

@Service
public class GLoginService {

	@Autowired
	private GoogleIdTokenVerifier gverifier;

	public GoogleIdToken getIdToken(String accessToken) throws GeneralSecurityException, IOException {
		GoogleIdToken idToken = gverifier.verify(accessToken);
		return idToken;
	}

	public Map<String, String> getUserInfo(GoogleIdToken idToken) {
		Map<String, String> userInfo = new HashMap<>();

		Payload payload = idToken.getPayload();
		
		userInfo.put("userId", payload.getSubject());
		userInfo.put("email", payload.getEmail());
		userInfo.put("name", (String) payload.get("name"));
		userInfo.put("pictureUrl", (String) payload.get("picture"));
		userInfo.put("locale", (String) payload.get("locale"));
		userInfo.put("familyName", (String) payload.get("family_name"));
		userInfo.put("givenName", (String) payload.get("given_name"));
		return userInfo;
	}	

}
