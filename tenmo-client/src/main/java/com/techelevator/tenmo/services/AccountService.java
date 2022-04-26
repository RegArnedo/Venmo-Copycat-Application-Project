package com.techelevator.tenmo.services;

import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

public class AccountService {
    //Mostly just followed the pattern set by AuthenticationService.java
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    // Made constructor for the baseUrl
    public AccountService(String apiBaseUrl){
        this.baseUrl = apiBaseUrl;

    }

    public BigDecimal getBalance(String token) {
        try {
            // the try catch.  The next following three lines could be pulled out and made
            // a method if we know we're going to repeat this code a lot.  We did it in an
            // exercise
            HttpHeaders headers = new HttpHeaders();        // Creating header
            headers.setBearerAuth(token);                   // Setting token
            HttpEntity entity = new HttpEntity(headers);

            // We know we need to send token in the header thus using HTTPENTITY
            // up above I set getBalance to taken token in as a parameter

            // We're expecting a big decimal
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "account/balance", HttpMethod.GET, entity, BigDecimal.class);
            return response.getBody();                      // returning the body
        } catch  (Exception e){
            BasicLogger.log("Error getting balance: "+e.getMessage());  // logging error
            return BigDecimal.ZERO;                                     // returning Zero

            /* NOTE FOR STEPHEN:  After this chunk of code, I inserted line 16 in App.java.
            App.java.
             */
        }
    }
}
