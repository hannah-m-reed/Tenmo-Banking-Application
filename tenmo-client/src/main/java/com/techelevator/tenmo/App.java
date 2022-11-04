package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AccountService accountService = new AccountService();
    private TransferService transferService = new TransferService();
    private UserService userService = new UserService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }



    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
                viewTransferDetails();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        System.out.println("Your current account balance is: "
                + accountService.getAccount(currentUser.getToken()).getBalance());

    }

	private void viewTransferHistory() {
        //TODO: print usernames not account numbers
		consoleService.printTransferList(transferService.getTransfers(currentUser.getToken()));
	}

    private void viewTransferDetails() {
        //TODO: print usernames not account numbers AND "Press Enter to continue.."
        consoleService.printOneTransfer(
         transferService.getSingleTransfer(currentUser.getToken(), consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ")));

    }
	private void viewPendingRequests() {
		// TODO Auto-generated method stub
        consoleService.printPendingTransfers(transferService.getTransfers(currentUser.getToken()));
	}

	private void sendBucks() {

        consoleService.printUsers(userService.userList(currentUser.getToken()));

        int userId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");

        String username = accountService.getUsernameByAccount(userId, userService.userList(currentUser.getToken()));

        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");

        //adds to other user's balance
        accountService.addToBalance(
            accountService.getAccountByUserId(userId, currentUser.getToken()),
            currentUser.getToken(),
            amount,
            username);

        //subtracts from current user's balance
        accountService.subtractFromBalance(
            accountService.getAccount(currentUser.getToken()),
            currentUser.getToken(),
            amount,
            currentUser.getUser().getUsername());


        /*
        put accountbalance for sender and receiver
        check big decimal input > current balance && > 0
         */

        //TODO: add to transaction list if successful
        //TODO if transfer succeeds print success message
	}

	private void requestBucks() {

        consoleService.printUsers(userService.userList(currentUser.getToken()));

        int userId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");

        String username = accountService.getUsernameByAccount(userId, userService.userList(currentUser.getToken()));

        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");

        //add to current user account
        accountService.addToBalance(
                accountService.getAccount(currentUser.getToken()),
                currentUser.getToken(),
                amount,
                currentUser.getUser().getUsername());

        //subtract from other account's balance
        accountService.subtractFromBalance(
                accountService.getAccountByUserId(userId, currentUser.getToken()),
                currentUser.getToken(),
                amount,
                username);

        //TODO: add to transaction list if successful
        //TODO if transfer succeeds print success message
	}

}
