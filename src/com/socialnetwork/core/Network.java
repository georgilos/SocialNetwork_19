package com.socialnetwork.core;

import com.socialnetwork.exceptions.AlreadyFriendException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Network implements Serializable {

    private static Network networkInstance = new Network();

    // Λίστα χρηστών
    private List<User> users;

    // Map λίστας φίλων ανά χρήστη
    private Map<User, List<User>> friendships;

    public static Network getInstance() {
        return networkInstance;
    }

    private Network() {
        this.users = new ArrayList<>();
        this.friendships = new HashMap<>();
    }

    /**
     * Προσθήκη νέου χρήστη
     */
    public void addUserToNetwork(User user) {
        // Προσθήκη χρήστη στο δίκτυο και αρχικοποίηση της λίστας των φίλων του ώς κενή
        users.add(user);
        friendships.put(user, new ArrayList<>());
    }

    /**
     * Διαγραφη χρήστη
     */
    public void removeUserFromNetwork(User user) {
        // Αφαίρεση χρήστη από το δίκτυο και της λίστας φίλων του
        users.remove(user);
        friendships.remove(user);
    }

    /**
     * Σύνδεση πολλών χρηστών με φιλία
     */
    public void makeMultipleFriendships(List<User> users) throws AlreadyFriendException {
        // Για κάθε χρήστη στην λίστα δημιουργούνται φιλίες με τους υπόλοιπους χρήστες της λίστας
        for (User user : users) {
            List<User> remainingUsers = new ArrayList<>(users);

            remainingUsers.remove(user);

            for (User u : remainingUsers) {
                makeFriendships(user, u);
            }
        }
    }

    /**
     * Σύνδεση δύο χρηστών με φιλία
     */
    public void makeFriendships(User firstUser, User secondUser) throws AlreadyFriendException {
        // Αν είναι ήδη φίλοι, τότε παράγεται exception
        if (friendshipBetweenUsersExists(firstUser, secondUser)) {
            throw new AlreadyFriendException();
        }

        // Αν προκειται για διαφορετικούς χρήστες, δημιουργείται φιλία
        if (!firstUser.equals(secondUser)) {
            friendships.get(firstUser).add(secondUser);
            friendships.get(secondUser).add(firstUser);
        }
    }

    /**
     * Σύνδεση δύο χρηστών με φιλία
     */
    public void makeFriendships(User firstUser, String secondUserName) throws AlreadyFriendException {
        // Ανάκτηση δεύτερου χρήστη με βάση το username του
        User secondUser = getNetworkUserByName(secondUserName);

        makeFriendships(firstUser, secondUser);
    }

    /**
     * Ανάκτηση λίστας φίλων ενός χρήστη
     */
    public List<User> getUserFriendList(User user) {
        return friendships.get(user);
    }

    /**
     * Ελέγχει αν υπάρχει φιλία μεταξύ των δύο χρηστών
     */
    public boolean friendshipBetweenUsersExists(User firstUser, User secondUser) {
        // Αν ένας από τους δύο χρήστες είναι null, επιστρέφει false
        if (firstUser == null || secondUser == null) {
            return false;
        }

        // Αν οι δύο χρήστες ταυτοποιούνται (είναι ίδιοι), επιστρέφεται false
        if (firstUser.equals(secondUser)) {
            return false;
        }

        // Ανάκτηση λίστας φίλων
        List<User> firstUserFriendList = friendships.get(firstUser);

        // Αν η λίστα φίλων του πρώτου χρήστη είναι κενή ή δεν περιέχει τον δεύτερο χρήστη τότε δεν είναι φίλοι
        if (firstUserFriendList.isEmpty() || !firstUserFriendList.contains(secondUser)) {
            return false;
        }

        // Αλλιώς είναι φίλοι
        return true;
    }

    /**
     * Ελέγχει αν υπάρχει φιλία μεταξύ των δύο χρηστών με βάση τα username
     */
    public boolean friendshipBetweenUsersExists(String firstUsername, String secondUsername) {
        // Ανάκτηση χρηστών με βάση τα username τους
        User firstUser = getNetworkUserByName(firstUsername);
        User secondUser = getNetworkUserByName(secondUsername);

        // Χρήση της ήδη υπάρχουσας μεθόδου
        return friendshipBetweenUsersExists(firstUser, secondUser);
    }

    /**
     * Ανάκτηση λίστας κοινών φίλων μεταξύ δύο χρηστών
     */
    public List<User> getCommonFriendsBetweenUsers(User firstUser, User secondUser) {
        // Ορισμός μίας λίστας ίδιας με την λίστα του πρώτου χρήστη
        List<User> commonFriends = new ArrayList<>(friendships.get(firstUser));

        // Κοινά στοιχεία στις δύο λίστες
        commonFriends.retainAll(friendships.get(secondUser));

        return commonFriends;
    }

    /**
     * Κατάργηση φιλίας
     */
    public void deleteFriendship(User firstUser, User secondUser) {
        // Αφαίρεση δεύτερου χρήστη από την λίστα του πρώτου και το ανάποδο
        friendships.get(firstUser).remove(secondUser);
        friendships.get(secondUser).remove(firstUser);
    }

    /**
     * Ανάκτηση χρήστη με βάση το όνομα
     */
    public User getNetworkUserByName(String name) {
        // Για κάθε χρήστη στο δίκτυο αν το username του είναι ίδιο
        // με αυτό που δόθηκε σαν παράμετρος, επιστρέφεται ο χρήστης
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        // Αλλιώς επιστρέφεται null
        return null;
    }

    /**
     * Ξεκινάει το κεντρικό μενού
     */
    public void startNetworkMenu() {
        // Δημιουργία μενού
        Menu menu = new Menu();

        // Εκκίνηση μενού
//        menu.initialize();
        menu.initializeNetworkMenu();
    }

    /**
     * Επιστρέφει τους χρήστες που δεν είναι φίλοι με τον δοθέντα χρήστη
     */
    public List<User> getNonFriendUsersOfNetworkUser(User user) {
        List<User> nonFriendUsers = new ArrayList<>();

        // Για κάθε χρήστη στο δίκτυο, έλεγχος αν είναι φίλος με τον δοθέντα χρήστη
        // και αν είναι προστίθεται στην λίστα
        for (User u : users) {
            if (!friendshipBetweenUsersExists(user, u) && !user.equals(u)) {
                nonFriendUsers.add(u);
            }
        }

        // Επιστρέφεται η λίστα με τους χρήστες που δεν είναι φίλοι
        return nonFriendUsers;
    }

    /**
     * Ελέγχει αν υπάρχει ο χρήστης στο δίκτυο
     * Επιστρέφει true αν υπάρχει ο χρήστης
     */
    public boolean userExistsInNetwork(User user) {
        // Για κάθε χρήστη στο δίκτυο, έλεγχος αν ταυτοποιείται με τον δοθέντα χρήστη
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ανάκτηση δικτύου από αρχείο
     */
    public void readNetworkFromFile() {
        try {
            System.out.println("INFO: Attempt to read data file.");

            // Άνοιγμα αρχείου
            FileInputStream fileInputStream = new FileInputStream(new File("data.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Ανάκτηση αντικειμένου από αρχείο
            networkInstance = (Network) objectInputStream.readObject();

            // Κλείσιμο αρχείου
            objectInputStream.close();
            fileInputStream.close();

            System.out.println("INFO: Reading data file completed.");

            return;
        }
        catch (FileNotFoundException e) {
            // Το αρχείο δεν βρέθηκε
            System.out.println("INFO: Data file not found.");
        }
        catch (IOException e) {
            // Το αρχείο δεν μπορεί να διαβαστεί
            System.out.println("INFO: Can't read data file.");
        }
        catch (ClassNotFoundException e) {
            // Δεν βρέθηκε σωστή δομή αντικειμένου στο αρχείο
            System.out.println("INFO: Data found in file are not valid.");
        }

        // Φορτώνει sample data στο δίκτυο
        loadSampleData();
        System.out.println("INFO: Loaded sample data.");
    }

    /**
     * Αποθήκευση δικτύου σε αρχείο
     */
    public void writeNetworkToFile() {
        try {
            System.out.println("INFO: Saving data to file started.");

            // Άνοιγμα αρχείου (αν δεν υπάρχει, το δημιουργεί)
            FileOutputStream fileOutputStream = new FileOutputStream(new File("data.txt"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Εγγραφή αντικειμένου στο αρχείο
            objectOutputStream.writeObject(getInstance());

            // Κλείσιμο αρχείου
            objectOutputStream.close();
            fileOutputStream.close();

            System.out.println("INFO: Saving data to file completed.");
        }
        catch (IOException e) {
            System.out.println("INFO: Error saving data to file.");
        }
    }

    /**
     * ΔΕΔΟΜΕΝΑ ΓΙΑ ΔΟΚΙΜΕΣ
     */
    private void loadSampleData() {

        // Χρήστες
        Network.getInstance().addUserToNetwork(new User("kostas", "kostas123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("kiki", "kiki123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("nikos", "nikos123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("jim", "dim123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("vagelis", "vag123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("niki", "niki123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("georgia", "georgia123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("irene", "irene123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("alex", "alex123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("tolis", "tolis123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("vasiliki", "vasiliki123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("vasilis", "vas123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("aliki", "alice123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("teo", "teo123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("stelios", "stelios123@mail.gr"));
        Network.getInstance().addUserToNetwork(new User("martha", "martha123@mail.gr"));

        List<User> users = Network.getInstance().getUsers();

        // Φιλίες
        try {
            Network.getInstance().makeMultipleFriendships(users.subList(0, 8));
            Network.getInstance().makeMultipleFriendships(users.subList(3, 7));
            Network.getInstance().makeMultipleFriendships(users.subList(5, 10));
            Network.getInstance().makeMultipleFriendships(users.subList(7, 12));
            Network.getInstance().makeMultipleFriendships(users.subList(9, 16));
        } catch (AlreadyFriendException e) {

        }

        // Μηνήματα
        Message secondLevelMessage2 = new Message("kostas", "I'm fine, thanks", false);
        Message secondLevelMessage = new Message("nikos", "Hey, how are you?", false);
        Message firstLevelMessage = new Message("kostas", "Hello", true);
        Message secondLevelMessage3 = new Message("kostas", "Hey!", false);
        Message firstLevelMessage2 = new Message("kiki", "Hey there!", true);
        Message firstLevelMessage3 = new Message("nikos", "What a beautiful day!", true);

        firstLevelMessage.addReply(secondLevelMessage);
        secondLevelMessage.addReply(secondLevelMessage2);
        firstLevelMessage2.addReply(secondLevelMessage3);

        Wall wall = users.get(2).getWall();
        wall.setPosts(List.of(firstLevelMessage, firstLevelMessage2, firstLevelMessage3));

        // Αιτήματα φιλίας
        try {
            users.get(14).sendFriendRequest(users.get(0));
            users.get(0).sendFriendRequest(users.get(15));
            users.get(12).sendFriendRequest(users.get(0));
        } catch (AlreadyFriendException e) {

        }

    }

    //
    //  GETTERS - SETTERS
    //

    public List<User> getUsers() {
        return users;
    }

    public Map<User, List<User>> getFriendships() {
        return friendships;
    }
}
