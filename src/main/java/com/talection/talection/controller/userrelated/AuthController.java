package com.talection.talection.controller.userrelated;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.talection.talection.config.GoogleTokenVerifier;
import com.talection.talection.config.JwtUtil;
import com.talection.talection.dto.requests.AuthenticationRequest;
import com.talection.talection.enums.AuthProvider;
import com.talection.talection.exception.UserNotFoundException;
import com.talection.talection.service.userrelated.UserDetailsServiceImplementation;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Controller for handling user authentication requests.
 * Supports both local and Google authentication methods.
 */
@RestController
@RequestMapping("/authenticate")
public class AuthController {
    private final UserDetailsServiceImplementation userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final GoogleTokenVerifier googleTokenVerifier;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(
            UserDetailsServiceImplementation userDetailsService,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            GoogleTokenVerifier googleTokenVerifier
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.googleTokenVerifier = googleTokenVerifier;
    }

    /**
     * Authenticates a user based on the provided authentication request.
     * Supports both local and Google authentication.
     *
     * @param request the authentication request containing credentials
     * @return a ResponseEntity containing the JWT token if authentication is successful, or an error message
     */
    @PostMapping()
    public ResponseEntity<String> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("Authentication request cannot be null");
        }
        if (request.getAuthProvider() == null) {
            return ResponseEntity.badRequest().body("AuthProvider not provided");
        }

        if (request.getAuthProvider() == AuthProvider.LOCAL) {
            try {
                return ResponseEntity.ok(authenticateLocal(request));
            } catch (BadCredentialsException e) {
                logger.error("Authentication failed for user: {}", request.getEmail());
                return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            } catch (UsernameNotFoundException e) {
                logger.error("User not found: {}", request.getEmail());
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            } catch (UserNotFoundException e) {
                logger.error("User not found with email: {}", request.getEmail());
                return new ResponseEntity<>("User not found with email: " + request.getEmail(), HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                logger.error("An error occured during authentication: {}", e.getMessage());
                return new ResponseEntity<>("An error occurred during authentication", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (request.getAuthProvider() == AuthProvider.GOOGLE) {
            try {
                return ResponseEntity.ok(authenticateGoogle(request));
            } catch (GeneralSecurityException e) {
                logger.error("Security error during Google authentication: {}", e.getMessage());
                return new ResponseEntity<>("Security error during Google authentication: ", HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (IOException e) {
                logger.error("IO error during Google authentication: {}", e.getMessage());
                return new ResponseEntity<>("IO error during Google authentication: ", HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (IllegalArgumentException e) {
                logger.error("Invalid Google ID token: {}", e.getMessage());
                return new ResponseEntity<>("Invalid Google ID token", HttpStatus.BAD_REQUEST);
            } catch (IllegalStateException e) {
                logger.error("Google auth configuration error: {}", e.getMessage());
                return new ResponseEntity<>("Google auth is not configured", HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException e) {
                logger.error("User not found for Google token: {}", request.getCredentialId());
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        }

        if (request.getAuthProvider() == AuthProvider.FEIDE) {
            return new ResponseEntity<>("FEIDE authentication is not implemented yet", HttpStatus.NOT_IMPLEMENTED);
        }

        return ResponseEntity.badRequest().body("Unsupported AuthProvider");
        
        
        
        // if (request == null) {
        //     return ResponseEntity.badRequest().body("Authentication request cannot be null");
        // }

        // if (request.getAuthProvider() == AuthProvider.LOCAL) {
        //     try {
        //         return ResponseEntity.ok(authenticateLocal(request));
        //     } catch (BadCredentialsException e) {
        //         logger.error("Authentication failed for user: {}", request.getEmail());
        //         return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        //     } catch (UsernameNotFoundException e) {
        //         logger.error("User not found: {}", request.getEmail());
        //         return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        //     } catch (UserNotFoundException e) {
        //         logger.error("User not found with email: {}", request.getEmail());
        //         return new ResponseEntity<>("User not found with email: " + request.getEmail(), HttpStatus.NOT_FOUND);
        //     } catch (Exception e) {
        //         logger.error("An error occurred during authentication: {}", e.getMessage());
        //         return new ResponseEntity<>("An error occurred during authentication", HttpStatus.INTERNAL_SERVER_ERROR);
        //     }
        // } else if (request.getAuthProvider() == AuthProvider.GOOGLE) {
        //     try {
        //         return ResponseEntity.ok(authenticateGoogle(request));
        //     } catch (GeneralSecurityException e) {
        //         logger.error("Security error during Google authentication: {}", e.getMessage());
        //         return new ResponseEntity<>("Security error during Google authentication: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        //     } catch (IOException e) {
        //         logger.error("IO error during Google authentication: {}", e.getMessage());
        //         return new ResponseEntity<>("IO error during Google authentication: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        //     } catch (IllegalArgumentException e) {
        //         logger.error("Invalid Google ID token: {}", e.getMessage());
        //         return new ResponseEntity<>("Invalid Google ID token", HttpStatus.BAD_REQUEST);
        //     } catch (UserNotFoundException e) {
        //         logger.error("User not found with Google ID: {}", request.getCredentialId());
        //         return new ResponseEntity<>("User not found with Google ID: " + request.getCredentialId(), HttpStatus.NOT_FOUND);
        //     }
        // } else {
        //     return ResponseEntity.badRequest().body("AuthProvider not provided");
        // }
    }

    private String authenticateLocal(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        if (userDetails == null) {
            throw new UserNotFoundException("User not found with email: " + request.getEmail());
        }
        return jwtUtil.generateToken(userDetails);
    }

    private String authenticateGoogle(AuthenticationRequest request) throws GeneralSecurityException, IOException {
        if (request.getCredentialId() == null || request.getCredentialId().isBlank()) {
            throw new IllegalArgumentException("Google ID token must not be null or empty");
        }

        GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(request.getCredentialId());
        if (payload == null || payload.getEmail() == null || payload.getEmail().isBlank()) {
            throw new IllegalArgumentException("Invalid Google ID token payload");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(payload.getEmail());
        if (userDetails == null) {
            throw new UserNotFoundException("User not found with email: " + payload.getEmail());
        }

        return jwtUtil.generateToken(userDetails);
    }
}
