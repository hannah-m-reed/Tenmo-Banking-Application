package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
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
                viewApproveRejectMenu();
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
                + accountService.getAccount(currentUser.getUser().getUsername(), currentUser.getToken()).getBalance());

    }

	private void viewTransferHistory() {
		consoleService.printTransferList(transferService.getTransfers(currentUser.getToken()), accountService.getAccount(currentUser.getUser().getUsername(), currentUser.getToken()).getAccountId());
	}

    private void viewTransferDetails() {
        int transferID = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        if(transferID == 0){
            mainMenu();
        } else if (!isInputValidTransfer(transferID)) {
            System.out.println("TransferId does not exist. Please try again");
        }
        else {
            consoleService.printOneTransfer(transferService.getSingleTransfer(currentUser.getToken(), transferID));
        }

    }
	private void viewPendingRequests() {
        consoleService.printPendingTransfers(
                transferService.getTransfers(currentUser.getToken()),
                accountService.getAccountByUserId(currentUser.getUser().getId(), currentUser.getToken()).getAccountId());

	}

    private void viewApproveRejectMenu(){
        int userChoice = -1;
        int accountToId = -1;
        int userId = -1;

        int transferId = consoleService.promptForInt("Please enter the transfer ID to approve/reject (0 to cancel): ");

        if (transferId == 0){
            mainMenu();
        } else if(!isInputValidTransfer(transferId)){
            System.out.println("Transfer ID is not valid. Please try again.");
        } else {
            consoleService.printApproveRejectMenu();
            userChoice = consoleService.promptForInt("Please choose an option: ");
            accountToId = transferService.getTransferById(currentUser.getToken(), transferId).getAccountTo();
            userId = accountService.getUserIdByAccountId(accountToId, currentUser.getToken());
        }


        if(userChoice == 0){
            mainMenu();
        } else if (userChoice == 1){
            if (transferService.getTransferById(currentUser.getToken(), transferId).getAmount().compareTo(accountService.getAccount(currentUser.getUser().getUsername(), currentUser.getToken()).getBalance()) >= 1){
                System.out.println("Cannot send more bucks than you have in your account.");
            } else {
                //add to other user's balance
                accountService.addToBalance(
                        accountService.getAccountByAccountId(accountToId, currentUser.getToken()),
                        currentUser.getToken(),
                        transferService.getTransferById(currentUser.getToken(), transferId).getAmount(),
                        accountService.getUsernameByAccount(userId, userService.userList(currentUser.getToken())));

                //subtracts from current user's balance
                accountService.subtractFromBalance(
                        accountService.getAccount(currentUser.getUser().getUsername(), currentUser.getToken()),
                        currentUser.getToken(),
                        transferService.getTransferById(currentUser.getToken(), transferId).getAmount(),
                        currentUser.getUser().getUsername());


                //update transfer as approved
                transferService.updateTransfer(currentUser.getToken(),
                        transferService.getTransferById(currentUser.getToken(), transferId),
                        "Approved", 2);
                System.out.println("Requested transfer has been approved.");
            }
        } else if (userChoice == 2){
            //update transfer as rejected
            transferService.updateTransfer(currentUser.getToken(),
                    transferService.getTransferById(currentUser.getToken(), transferId),
                    "Rejected", 3);
            System.out.println("Requested transfer has been rejected.");
        }


    }


	private void sendBucks() {

        consoleService.printUsers(userService.userList(currentUser.getToken()));

        int userId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
        if(userId == 0){
            mainMenu();
        } else if (userId == currentUser.getUser().getId()){
            System.out.println("Cannot send money to yourself. Please try again");

        } else if (!isInputValidUser(userId)){
            System.out.println("User does not exist. Please try again.");
        } else {
            String username = accountService.getUsernameByAccount(userId, userService.userList(currentUser.getToken()));

            BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");

            if (amount.compareTo(accountService.getAccount(currentUser.getUser().getUsername(), currentUser.getToken()).getBalance()) >= 1 ){
                System.out.println("Cannot send more bucks then you have in your account. Please try again.");
            } else if (amount.compareTo(BigDecimal.ZERO) <= 0){
                System.out.println("Cannot send zero or negative bucks. Please try again.");
            } else{
                //adds to other user's balance
                accountService.addToBalance(
                        accountService.getAccountByUserId(userId, currentUser.getToken()),
                        currentUser.getToken(),
                        amount,
                        username);

                //subtracts from current user's balance
                accountService.subtractFromBalance(
                        accountService.getAccount(currentUser.getUser().getUsername(), currentUser.getToken()),
                        currentUser.getToken(),
                        amount,
                        currentUser.getUser().getUsername());

                System.out.println("Transfer was successful!");


                Transfer newTransfer = new Transfer(2,
                        2,
                        accountService.getAccountByUserId(currentUser.getUser().getId(), currentUser.getToken()).getAccountId(),
                        accountService.getAccountByUserId(userId, currentUser.getToken()).getAccountId(),
                        amount,
                        "Send",
                        "Approved");

                transferService.createTransfer(currentUser.getToken(), newTransfer);
            }
        }


	}

    private void requestBucks() {

        consoleService.printUsers(userService.userList(currentUser.getToken()));

        int userId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
        if(userId == 0){
            mainMenu();
        } else if (userId == currentUser.getUser().getId()) {
            System.out.println("Cannot request money from yourself. Please try again");

        } else if (!isInputValidUser(userId)) {
            System.out.println("User does not exist. Please try again.");
        } else {

            BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");


             if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Cannot request zero or negative bucks. Please try again.");
            } else {
                 Transfer newTransfer = new Transfer(1,
                         1,
                         accountService.getAccountByUserId(userId, currentUser.getToken()).getAccountId(),
                         accountService.getAccountByUserId(currentUser.getUser().getId(), currentUser.getToken()).getAccountId(),
                         amount,
                         "Request",
                         "Pending");

                 transferService.createTransfer(currentUser.getToken(), newTransfer);

                 System.out.println("Transfer pending.");

            }
        }
    }

    private boolean isInputValidUser(int userId) {
        boolean containsId = false;

        for(User user : userService.userList(currentUser.getToken())){
            if (user.getId() == userId){
                containsId = true;
            }
        }
        return containsId;
    }

    private boolean isInputValidTransfer(int transferID) {
        boolean containsId = false;

        for(Transfer transfer : transferService.getTransfers(currentUser.getToken())){
            if (transfer.getTransferId() == transferID){
                containsId = true;
            }
        }
        return containsId;
    }

}
