package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import javax.xml.transform.TransformerFactoryConfigurationError;
import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void printTransferList(Transfer[] transfers, int accountId) {
        System.out.println("-------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To         Amount");
        System.out.println("-------------------------");
        for(Transfer transfer: transfers) {

            if (transfer.getAccountFrom() == accountId) {
                System.out.println(transfer.getTransferId() + "     To:   " + transfer.getUsername() + "       $" + transfer.getAmount());
            } else {
                System.out.println(transfer.getTransferId() + "     From: " + transfer.getUsername() + "       $" + transfer.getAmount());
            }
        }
        System.out.println("---------");
    }

    public void printPendingTransfers(Transfer[] transfers, int accountId) {
        System.out.println("-------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          To         Amount");
        System.out.println("-------------------------");
        for(Transfer transfer: transfers) {
            if (transfer.getTransferStatusId() == 1 && transfer.getAccountFrom() == accountId) {
                System.out.println(transfer.getTransferId() + "      " + transfer.getUsername() + "       $" + transfer.getAmount());
            }
        }
        System.out.println("---------");
    }

    public void printUsers(User[] users){
        System.out.println("-------------------------");
        System.out.println("Users");
        System.out.println("ID          Name");
        System.out.println("-------------------------");
        for(User user: users) {
            System.out.println(user.getId() + "        " + user.getUsername());
        }
        System.out.println("---------");
    }



    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }


    public void printApproveRejectMenu(){
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("---------");
    }

    public void printOneTransfer(Transfer transfer){
        System.out.println("-------------------------");
        System.out.println("Transfer detail: ");
        System.out.println("-------------------------");
        System.out.println("ID: " + transfer.getTransferId());
        System.out.println("From: " + transfer.getUserFrom());
        System.out.println("To: " + transfer.getUserTo());
        System.out.println("Type: " + transfer.getTransferTypeDescription());
        System.out.println("Status: " + transfer.getTransferStatusDescription());
        System.out.println("Amount: $" + transfer.getAmount());
        System.out.println("---------");
    }


    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}
