package com.techelevator.tenmo.services;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import java.util.Scanner;

public class TransferService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public TransferService(String apiBaseUrl, AuthenticatedUser currentUser) {
        this.baseUrl = apiBaseUrl;
        this.currentUser = currentUser;
    }

    public void getAllTransfers() {
        Transfer[] response = null;
        try {
            response = restTemplate.exchange(baseUrl + "account/transfers/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            System.out.println("-------------------------------------------------------------------\n" +
                    "Transfers\n" +
                    "Send#   Username   (Acct)    From/To   Recipient Acct        Amount\n" +
                    "-------------------------------------------------------------------");
            for (Transfer array : response) {
                System.out.println(array.getTransferId() + "\t" + currentUser.getUser().getUsername() + " \t" + array.getAccountFrom() + "\t\t" + "To" + "\t\t\t" + array.getAccountTo() + "\t\t\t$" + array.getAmount());
            }
            System.out.println("\nPlease enter the 'Send#' of the transaction you want details for.");
            Scanner scanner = new Scanner(System.in);
            int sendNum = scanner.nextInt();
            boolean foundSendNum = false;
            for (Transfer array : response) {
                if (sendNum == array.getTransferId()) {
                    Transfer query = restTemplate.exchange(baseUrl + "transfer/" + array.getTransferId(), HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
                    foundSendNum = true;
                    System.out.println("-------------------------------------------------------------------\n" +
                            "Detailed Transaction Information\n" +
                            "-------------------------------------------------------------------\n" +
                            "User Name: " + "\t\t" + currentUser.getUser().getUsername() + "\n" +
                            "Send#: " + "\t\t\t" + query.getTransferId() + "\n" +
                            "Acct From: " + "\t\t" + query.getAccountFrom() + "\n" +
                            "Acct To: " + "\t\t" + query.getAccountTo() + "\n" +
                            "Type: " + "\t\t\t" + query.getTransferType() + "\n" +
                            "Status: " + "\t\t" + query.getTransferStatus() + "\n" +
                            "Amount: $" + "\t\t" + query.getAmount());
                    }
                }
            if (!foundSendNum) {
                System.out.println("This is not a valid Send#");
                }
                    }
            catch(Exception e){
                System.out.println("Error getting transfer history: " + e.getMessage());
                BasicLogger.log("Error getting transfer history: " + e.getMessage());
                }
        }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}