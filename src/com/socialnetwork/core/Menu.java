package com.socialnetwork.core;

import com.socialnetwork.enums.FriendRequestStatus;
import com.socialnetwork.exceptions.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Μενού επιλογών
 */
public class Menu {

    // Συνδεδεμένος χρήστης
    private static User loggedUser;

    // Επιλογές αρχικού μενού κοινωνικού δικτύου
    private static final String[] mainMenuOptions = {
            "1) Log in (Σύνδεση)",
            "2) Sign up (Εγγραφή)",
            "3) Close program (Τερματισμός προγράμματος)"
    };

    // Επιλογές αρχικού μενού συνδεδεμένου χρήστη
    private static final String[] userMainMenuOptions = {
            "1) See your wall (Προβολή τοίχου χρήστη)",
            "2) See friends wall (Προβολή τοίχου φίλου)",
            "3) Send friend request (Στείλε αίτημα φιλίας)",
            "4) Accept/Reject friend request (Αποδοχή/Απόρριψη αιτήματος φιλίας)",
            "5) See my friends (Προβολή φίλων)",
            "6) Log out (Αποσύνδεση)",
            "7) Close program (Τερματισμός προγράμματος)"
    };

    // Επιλογές ενεργειών χρήστη σε τοίχο
    private static final String[] wallActionMenuOptions = {
            "1) Post a message (Στείλε μήνυμα στο wall)",
            "2) Reply to a message (Απάντησε σε μήνυμα)",
            "3) Like (Κάνε LIKE)",
            "4) View wall (Προβολή τοίχου)",
            "0) Back (Πίσω)"
    };

    // Επιλογές ενεργειών χρήστη σε αίτημα φιλίας
    private static final String[] friendRequestActionOptions = {
            "1) Accept (Αποδοχή)",
            "2) Reject (Απόρριψη)",
            "0) Back (Πίσω)"
    };

    private Scanner scanner = new Scanner(System.in);

    public void initializeNetworkMenu() {

        int choice;
        boolean exit = false;

        do {

            try {
                // Τυπώνει το αρχικό μενού
                printMainMenu();

                // Επιλογή
                choice = chooseOption(mainMenuOptions.length);

                switch (choice) {
                    case 1:
                        // Επιλογή Log in
                        try {
                            // Σύνδεση χρήστη
                            userLogIn();

                            // Τυπώνει το κεντρικό μενού του συνδεδεμένου χρήστη
                            userMainMenu();
                        }
                        catch (InvalidUsernameException e) {
                            // Τυπώνεται το μήνυμα του exception
                            System.out.println(e.getMessage());
                        }

                        break;
                    case 2:
                        // Επιλογή Sign up
                        try {
                            // Εγγραφή νέου χρήστη
                            userSignUp();

                            // Τυπώνει το κεντρικό μενού του συνδεδεμένου χρήστη
                            userMainMenu();
                        }
                        catch (UserExistsException e) {
                            // Τυπώνεται το μήνυμα του exception
                            System.out.println(e.getMessage());
                        }

                        break;
                    case 3:
                        // Αποθήκευση τρέχουσας κατάστασης δικτύου
                        Network.getInstance().writeNetworkToFile();

                        // Κλείνει το πρόγραμμα
                        exit = true;

                        break;
                    default:
                        break;
                }

            } catch (InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }

        } while (!exit);

    }

    /**
     * Παίρνει ώς όρισμα τον αριθμό των πιθανών επιλογών, ζητάει από τον χρήστη να επιλέξει και
     * επιστρέφει την επιλογή του χρήστη. Σε περίπτωση που η επιλογή είναι εκτός ορίων ή άκυρη,
     * παράγει το κατάλληλο exception
     */
    private int chooseOption(int numberOfChoices) throws InvalidChoiceException {
        int choice;

        // Αν δοθεί κάτι άλλο εκτός από αριθμό, πιάνει το exception και ακυρώνει την επιλογή σετάροντας την -1
        try {
            choice = scanner.nextInt();
        }
        catch (InputMismatchException e) {
            choice = -1;
        }

        // Απορροφά τον χαρακτήρα "\n" που προκύπτει όταν ο χρήστης πατήσει enter
        scanner.nextLine();

        // Αν η επιλογή είναι εκτός ορίων παράγει exception
        if (choice < 0 || choice > numberOfChoices) {
            throw new InvalidChoiceException();
        }

        // Επιστροφή επιλογής χρήστη
        return choice;
    }

    /**
     * Σύνδεση χρήστη
     */
    private void userLogIn() throws InvalidUsernameException {
        // Εισαγωγή username από τον χρήστη
        System.out.println("------------------------------------------------------------------------------------------------------------");
        System.out.println("Please insert username (Παρακαλώ εισάγεται το όνομα χρήστη):");
        String username = scanner.nextLine();

        // Ανάκτηση χρήστη από το δίκτυο
        User user = Network.getInstance().getNetworkUserByName(username);

        // Αν δεν υπάρχει χρήστης με το δοθέν username, βγαίνει exception
        if (user == null) {
            throw new InvalidUsernameException();
        }

        // Σετάρεται ώς συνδεδεμένος χρήστης, ο χρήστης που ανακτήθηκε
        loggedUser = user;
    }

    /**
     * Εγγραφή νέου χρήστη
     */
    private void userSignUp() throws UserExistsException {
        // Εισαγωγή username και email από τον χρήστη
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Please insert a username (Παρακαλώ εισάγεται ένα όνομα χρήστη):");
        String username = scanner.nextLine();

        System.out.println("Please insert your email (Παρακαλώ εισάγεται το email σας):");
        String email = scanner.nextLine();

        // Δημιουργία νέου αντικειμένου χρήστη
        User newUser = new User(username, email);

        // Έλεγχος αν υπάρχει ήδη ο χρήστης
        if (Network.getInstance().userExistsInNetwork(newUser)) {
            throw new UserExistsException();
        }

        // Προσθήκη νέου χρήστη στο Network
        Network.getInstance().addUserToNetwork(newUser);

        // Ορισμός νέου χρήστη ώς συνδεδεμένου
        loggedUser = newUser;
    }

    /**
     * Μενού επιλογών συνδεδεμένου χρήστη
     */
    private void userMainMenu() {
        int choice;

        boolean back = false;

        do {
            try {
                // Εμφάνιση μενού επιλογών για συνδεδεμένο χρήστη
                printUserMainMenu();

                // Επιλογή
                choice = chooseOption(userMainMenuOptions.length);

                switch (choice) {
                    case 1:
                        // See your wall
                        seeYourWall();

                        break;
                    case 2:
                        // See friends wall
                        seeFriendsWall();

                        break;
                    case 3:
                        // Send friend request
                        sendFriendRequests();

                        break;
                    case 4:
                        // Accept/Reject friend request
                        manageUserIncomingFriendRequests();

                        break;
                    case 5:
                        // Λίστα φίλων χρήστη
                        List<User> friends = loggedUser.getFriendList();

                        // Εμφάνιση μη αριθμημένης λίστας φίλων χρήστη
                        printUserFriends(friends, false);

                        System.out.println("Press <ENTER> to go back to menu (Πατήστε το <ENTER> για να επιστρέψετε στο μενού)...");
                        scanner.nextLine();

                        break;
                    case 6:
                        // Ορισμός flag Για επιστροφή στο προηγούμενο μενού σε true
                        back = true;

                        // Log out του τρέχοντος συνδεδεμένου χρήστη
                        loggedUser = null;

                        break;
                    case 7:
                        // Αποθήκευση τρέχουσας κατάστασης δικτύου
                        Network.getInstance().writeNetworkToFile();

                        // Κλείνει το πρόγραμμα
                        System.exit(0);

                        break;
                    default:
                        break;
                }
            }
            catch (InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }

        } while (!back);
    }

    /**
     * Προβολή τόιχου συνδεδεμένου χρήστη
     */
    private void seeYourWall() {
        // Διαχείριση ενεργειών στον τοίχο
        performActionsOnWall(loggedUser);
    }

    /**
     * Προβολή τόιχου ενός φίλου του σθνδεδεμένου χρήστη
     */
    private void seeFriendsWall() {
        List<User> friends;
        int choice;

        do {
            try {
                // Λίστα φίλων χρήστη
                friends = loggedUser.getFriendList();

                // Εμφάνιση αριθμημένης λίστας φίλων συνδεδεμένου χρήστη
                printUserFriends(friends, true);

                // Αν η λίστα φίλων είναι κενή, επιστροφή ένα επίπεδο πάνω
                if (friends.isEmpty()) {
                    return;
                }

                System.out.println("Choose from list or Press 0 to go back (Επιλέξτε από την λίστα ή Επιλέξτε 0 για επιστροφή): ");

                // Επιλογή
                choice = chooseOption(friends.size());

                // Αν δοθεί το 0, γυρίζει στο προηγούμενο μενού
                if (choice == 0) {
                    return;
                }

                // Διαχείριση ενεργειών στον τοίχο
                performActionsOnWall(friends.get(choice - 1));
            }
            catch (InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }

        } while (true);
    }

    /**
     * Ενέργειες που μπορεί να κάνει ο χρήστης στον τοίχο
     */
    private void performActionsOnWall(User user) {
        int choice;

        List<Message> posts;

        boolean back = false;

        // Εμφάνιση τοίχου του δοθέντα χρήστη
        printUserWall(user);

        do {
            try {

                // Εμφάνιση μενού ενεργειών στον τοίχο
                printWallActionsMenu(user.getUsername());

                // Επιλογή
                choice = chooseOption(wallActionMenuOptions.length);

                switch (choice) {
                    case 1:
                        // Post a message
                        postNewMessageToWall(user.getWall());

                        break;
                    case 2:
                        // Reply to a message

                        // Τυπώνει τα μηνύματα του τοίχου
                        printWallMessages(user.getWall());

                        // Λίστα με τα μηνύματα του τοίχου
                        posts = user.getWall().getPosts();

                        // Αν η λίστα μηνυμάτων δεν είναι κενή
                        if (!posts.isEmpty()) {
                            // Επιλογή μηνύματος
                            choice = chooseMessage(posts);

                            if (choice != 0) {
                                // Απάντηση σε μήνυμα
                                replyToMessageOfWall(user.getWall(), posts.get(choice - 1));
                            }
                        }

                        break;
                    case 3:
                        // Like a message

                        // Τυπώνει τα μηνύματα του τοίχου
                        printWallMessages(user.getWall());

                        // Λίστα με τα μηνύματα του τοίχου
                        posts = user.getWall().getPosts();

                        // Αν η λίστα μηνυμάτων δεν είναι κενή
                        if (!posts.isEmpty()) {
                            // Επιλογή μηνύματος
                            choice = chooseMessage(posts);

                            if (choice != 0) {
                                // Like στο μήνυμα
                                loggedUser.likeMessage(posts.get(choice - 1));

                                System.out.println("\nYour like was submitted! (Το like σου καταχωρήθηκε)\n");
                            }
                        }

                        break;
                    case 4:
                        // Προβολή τοίχου χρήστη
                        printUserWall(user);

                        System.out.println("Press <ENTER> to go back to menu (Πατήστε το <ENTER> για να επιστρέψετε στο μενού)...");
                        scanner.nextLine();

                        break;
                    case 0:
                        // Ορισμός flag για επιστροφή στο προηγούμενο μενού σε true
                        back = true;

                        break;
                    default:
                        break;
                }
            }
            catch (InvalidChoiceException | UserAlreadyLikesPostException e) {
                System.out.println(e.getMessage());
            }

        } while (!back);
    }

    /**
     * Προσθήκη post στον τοίχο
     */
    private void postNewMessageToWall(Wall wall) {
        // Εισαγωγή μηνύματος από το χρήστη
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Please insert your message (Παρακαλώ εισάγεται το μήνυμα σας):");
        String text = scanner.nextLine();

        try {
            // Προσθήκη post στον τοίχο
            loggedUser.postMessage(wall, new Message(loggedUser.getUsername(), text, true));

            System.out.println("\nYour post was successfully posted!\n");
        } catch (NonFriendWallException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Press <ENTER> to go back to menu (Πατήστε το <ENTER> για να επιστρέψετε στο μενού)...");
        scanner.nextLine();
    }

    /**
     * Επιλογή μηνύματος του τοίχου
     */
    private int chooseMessage(List<Message> posts) {
        int choice;

        do {
            System.out.println("Choose from list or Press 0 to go back (Επιλέξτε από την λίστα ή Επιλέξτε 0 για επιστροφή): ");

            try {
                // Επιλογή
                choice = chooseOption(posts.size());

            } catch (InvalidChoiceException e) {
                e.printStackTrace();
                choice = -1;
            }

        } while (choice < 0 || choice > posts.size());

        return choice;
    }

    /**
     * Απάντηση σε ένα message του τοίχου
     */
    private void replyToMessageOfWall(Wall wall, Message message) {
        // Εισαγωγή μηνύματος από τον χρήστη
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Please insert your message (Παρακαλώ εισάγεται το μήνυμα σας):");
        String text = scanner.nextLine();

        try {
            // Προσθήκη απάντησης σε μήνυμα στον τοίχο
            loggedUser.postMessage(wall, new Message(loggedUser.getUsername(), text, false), message);

            System.out.println("\nYour reply was successfully posted!\n");
        } catch (NonFriendWallException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Αποστολή αιτημάτων φιλίας
     */
    private void sendFriendRequests() {
        int choice;
        List<User> nonFriendUsers;

        boolean back = false;

        do {
            nonFriendUsers = Network.getInstance().getNonFriendUsersOfNetworkUser(loggedUser);

            // Εμφάνιση λίστας χρηστών που δεν είναι φίλοι με τον συνδεδεμένο χρήστη
            printNonFriendUsers(nonFriendUsers);

            try {

                // Αν η λίστα είναι κενή, επιστροφή ένα επίπεδο πάνω
                if (nonFriendUsers.isEmpty()) {
                    return;
                }

                System.out.println("Choose from list or Press 0 to go back (Επιλέξτε από την λίστα ή Επιλέξτε 0 για επιστροφή): ");

                choice = chooseOption(nonFriendUsers.size());

                switch (choice) {
                    case 0:
                        // Επιστροφή στο προηγούμενο μενού

                        // Το flag επιστροφής γίνεται true
                        back = true;

                        break;
                    default:
                        // Οποιαδήποτε επιλογή εντός της λίστας

                        // Αποστολή αιτήματος φιλίας από τον συνδεδεμένο σχρήστη, στον επιλεγμένο από την λίστα
                        loggedUser.sendFriendRequest(nonFriendUsers.get(choice - 1));

                        System.out.println("Friend request sent to " + nonFriendUsers.get(choice - 1).getUsername() + ".");

                        // Το flag επιστροφής γίνεται true
                        back = true;

                        break;
                }
            }
            catch (InvalidChoiceException | AlreadyFriendException e) {
                System.out.println(e.getMessage());
            }

        } while (!back);
    }

    /**
     * Διαχείρηση αιτημάτων φιλίας
     */
    private void manageUserIncomingFriendRequests() {
        int choice;
        List<FriendRequest> friendRequests;

        boolean back = false;

        do {

            try {
                // Ανάκτηση λίστας εισερχόμενων αιτημάτων φιλίας που εκκρεμούν
                friendRequests  = loggedUser.getIncomingFriendRequests();

                // Εμφάνιση λίστας εισερχόμενων αιτημάτων φιλιάς που εκκρεμούν
                printUserIncomingFriendRequests(friendRequests);

                // Αν η λίστα είναι κενή, επιστροφή ένα επίπεδο πάνω
                if (friendRequests.isEmpty()) {
                    return;
                }

                System.out.println("Choose from list or Press 0 to go back (Επιλέξτε από την λίστα ή Επιλέξτε 0 για επιστροφή): ");

                choice = chooseOption(friendRequests.size());

                switch (choice) {
                    case 0:
                        // Επιστροφή στο προηγούμενο μενού

                        // Το flag επιστροφής γίνεται true
                        back = true;

                        break;
                    default:
                        // Απόφαση για αίτημα φιλίας
                        makeDecisionOnFriendRequest(friendRequests.get(choice - 1));

                        // Το flag επιστροφής γίνεται true
                        back = true;

                        break;
                }
            }
            catch (InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }

        } while (!back);

    }

    /**
     * Επιλογή ενέργειας για αίτημα φιλίας (αποδοχή/απόρριψη)
     */
    private void makeDecisionOnFriendRequest(FriendRequest friendRequest) {
        // Απόφαση για αποδοχή/απόρριψη
        int choice;

        // Flag επιστροφής
        boolean back = false;

        do {
            try {
                // Μενού ενεργειών για αίτημα φιλίας
                printFriendRequestActionsMenu();

                // Επιλογή
                choice = chooseOption(friendRequestActionOptions.length);

                switch (choice) {
                    case 1:
                        // Αποδοχή αιτήματος φιλίας
                        loggedUser.changeFriendRequestStatus(friendRequest, FriendRequestStatus.ACCEPTED);
                        System.out.println("You accepted friend request by " + friendRequest.getFromUser() + ".");

                        // Το flag επιστροφής γίνεται true
                        back = true;

                        break;
                    case 2:
                        // Απάρριψη αιτήματος φιλίας
                        loggedUser.changeFriendRequestStatus(friendRequest, FriendRequestStatus.REJECTED);
                        System.out.println("You rejected friend request by " + friendRequest.getFromUser() + ".");

                        // Το flag επιστροφής γίνεται true
                        back = true;

                        break;
                    case 0:
                        // Επιστροφή στο προηγούμενο μενού

                        // Το flag επιστροφής γίνεται true
                        back = true;

                        break;
                }

            } catch (InvalidChoiceException | AlreadyFriendException e) {
                System.out.println(e.getMessage());
            }

        } while (!back);
    }

    /**
     * Τυπώνει το αρχικό μενού της εφαρμογής
     */
    private void printMainMenu() {
        System.out.print("\n\n");
        System.out.println("================================================================================================================================");
        System.out.println("Welcome to the best social network!");
        System.out.println("================================================================================================================================");
        for (String str : mainMenuOptions) {
            System.out.println(str);
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Choose (Επιλέξτε): ");
    }

    /**
     * Τυπώνει το αρχικό μενού επιλογών συνδεδεμένου χρήστη
     */
    private void printUserMainMenu() {
        System.out.print("\n\n");
        System.out.println("================================================================================================================================");
        System.out.println("Main Menu");
        System.out.println("================================================================================================================================");
        for (String str : userMainMenuOptions) {
            System.out.println(str);
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Choose (Επιλέξτε): ");
    }

    /**
     * Τυπώνει το μενού ενεργειών του χρήστη σε έναν τοίχο
     */
    private void printWallActionsMenu(String username) {
        System.out.println("================================================================================================================================");
        System.out.println("Actions on " + username + "'s wall");
        System.out.println("================================================================================================================================");
        for (String str : wallActionMenuOptions) {
            System.out.println(str);
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Choose (Επιλέξτε): ");
    }

    /**
     * Τυπώνει το μενού επιλογών για απάντηση σε αίτημα φιλίας
     */
    private void printFriendRequestActionsMenu() {
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        for (String str : friendRequestActionOptions) {
            System.out.println(str);
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Choose (Επιλέξτε): ");
    }

    /**
     * Εμφανίζει τον τοίχο του χρήστη
     */
    private void printUserWall(User user) {
        System.out.print("\n\n");
        System.out.println("================================================================================================================================");
        System.out.println(user.getUsername() + "'s wall");
        System.out.println("================================================================================================================================");

        System.out.println(user.getWall().toString());

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Εμφανίζει τον τοίχο του δοθέντα χρήστη
     */
    private void printWallMessages(Wall wall) {
        System.out.print("\n\n");
        System.out.println("================================================================================================================================");
        System.out.println(wall.getOwnerUsername() + "'s wall posts");
        System.out.println("================================================================================================================================");

        List<Message> wallPosts = wall.getPosts();

        if (wallPosts.isEmpty()) {
            System.out.println("There are no posts on " + wall.getOwnerUsername() + "'s wall.");
        }
        else {
            for (int i = 0; i < wallPosts.size(); i++) {
                System.out.println(i + 1 + ") " + wallPosts.get(i).toString());
            }
        }

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Εμφανίζει τους χρήστες στους οποίους ο συνδεδεμένος χρήστης μπορεί να στείλει αίτημα φιλίας
     */
    private void printNonFriendUsers(List<User> nonFriendUsers) {
        System.out.print("\n\n");
        System.out.println("================================================================================================================================");
        System.out.println("Send friend request to:");
        System.out.println("================================================================================================================================");

        if (!nonFriendUsers.isEmpty()) {
            for (int i = 0; i < nonFriendUsers.size(); i++) {
                System.out.println(i + 1 + ") " + nonFriendUsers.get(i).getUsername());
            }
        }
        else {
            System.out.println("There are no non-friend users in the network.");
        }

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Εμφανίζει τους φίλους του συνδεδεμένου χρήστη
     * Αν στην παράμετρο numberedList δοθεί true, τότε η λίστα εμφανίζεται αριθμιμένη
     */
    private void printUserFriends(List<User> friends, boolean numberedList) {
        System.out.print("\n\n");
        System.out.println("================================================================================================================================");
        System.out.println("Your friends list:");
        System.out.println("================================================================================================================================");

        if (!friends.isEmpty()) {
            for (int i = 0; i < friends.size(); i++) {
                if (numberedList) {
                    System.out.println(i + 1 + ") " + friends.get(i).getUsername());
                }
                else {
                    System.out.println(friends.get(i).getUsername());
                }
            }
        }
        else {
            System.out.println("You have no friends at the moment.");
        }

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Τυπώνει τα αιτήματα φιλίας του χρήστη και επιστρέφει τον αριθμό τους
     */
    private void printUserIncomingFriendRequests(List<FriendRequest> friendRequestList) {
        System.out.print("\n\n");
        System.out.println("================================================================================================================================");
        System.out.println("Pending friend requests:");
        System.out.println("================================================================================================================================");

        if (friendRequestList.isEmpty()) {
            System.out.println("No pending friend requests!");
        }
        else {
            for (int i = 0; i < friendRequestList.size(); i++) {
                System.out.println(i + 1 + ") " + friendRequestList.get(i).toString());
            }
        }

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
    }
}
