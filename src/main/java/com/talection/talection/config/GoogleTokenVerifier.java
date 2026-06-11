package com.talection.talection.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.stereotype.Component;

/**
 * Utility class for verifying Google ID tokens.
 * This class uses the Google API client library to verify the authenticity of ID tokens
 * issued by Google, ensuring that they are valid and intended for the specified client ID.
 */
@Component
public class GoogleTokenVerifier {

    @Value("${google.client-id}")
    private String CLIENT_ID;

    public GoogleIdToken.Payload verifyToken(String idTokenString) throws GeneralSecurityException, IOException {
        if (idTokenString == null || idTokenString.isBlank()) {
            throw new IllegalArgumentException("ID token must not be null or blank");
        }
        if (CLIENT_ID == null || CLIENT_ID.isBlank() || "YOUR_GOOGLE_CLIENT_ID".equals(CLIENT_ID)) {
            throw new IllegalStateException("google.client-id is not configured");
        }

        var transport = GoogleNetHttpTransport.newTrustedTransport();
        var jsonFactory = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken == null) {
            throw new SecurityException("Invalid ID token");
        }
        return idToken.getPayload();
    }
}
