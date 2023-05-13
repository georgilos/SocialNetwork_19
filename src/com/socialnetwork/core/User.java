package com.socialnetwork.core;

import com.socialnetwork.enums.FriendRequestDirection;
import com.socialnetwork.enums.FriendRequestStatus;
import com.socialnetwork.exceptions.AlreadyFriendException;
import com.socialnetwork.exceptions.NonFriendWallException;
import com.socialnetwork.exceptions.UserAlreadyLikesPostException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Χρήστης
 */
public class User implements Serializable {

    // Όνομα
    private String username;

    // Email
    private String email;

    // Τοίχος χρήστη
    private Wall wall;

    // Λίστα αιτημάτων φιλίας
    private List<FriendRequest> friendRequests;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.wall = new Wall(username);
        this.friendRequests = new ArrayList<>();
    }

    /**
     * Αποστολή αιτήματος φιλίας σε χρήστη
     */
    public void sendFriendRequest(User recipientUser) throws AlreadyFriendException {
        // Αν είναι ήδη φίλος, βγαίνει exception
        if (Network.getInstance().friendshipBetweenUsersExists(this, recipientUser)) {
            throw new AlreadyFriendException();
        }

        // Προσθήκη αιτήματος στην λίστα του αποστολέα
        addFriendRequestToList(new FriendRequest(this.username, recipientUser.getUsername(), FriendRequestDirection.OUTGOING));

        // Προσθήκη αιτήματος στην λίστα του παραλήπτη
        recipientUser.addFriendRequestToList(new FriendRequest(this.username, recipientUser.getUsername(), FriendRequestDirection.INCOMING));
    }

    /**
     * Αποδοχή/απόρριψη αιτήματος
     */
    public void changeFriendRequestStatus(FriendRequest friendRequest, FriendRequestStatus friendRequestStatus) throws AlreadyFriendException {
        // Αν δεν δόθηκε null
        if (friendRequestStatus != null) {
            // Αν έγινε αποδοχή, προστίθεται ώς φίλος
            // και ο παρών χρήστης προστίθεται ώς φίλος του άλλου
            if (friendRequestStatus.equals(FriendRequestStatus.ACCEPTED)) {
                Network.getInstance().makeFriendships(this, friendRequest.getFromUser());
            }

            // Αλλαγή κατάστασης αιτήματος φιλίας
            friendRequest.setStatus(friendRequestStatus);
        }
    }

    /**
     * Αφαίρεση φίλου
     */
    public void removeFriend(User user) {
        Network.getInstance().deleteFriendship(this, user);
    }

    /**
     * Προσθέτει post στον τοίχο
     */
    public void postMessage(Wall wall, Message post) throws NonFriendWallException {
        wall.addPost(post);
    }

    /**
     * Προσθέτει απάντηση σε post
     */
    public void postMessage(Wall wall, Message reply, Message post) throws NonFriendWallException {
        wall.addReplyToPost(reply, post);
    }

    /**
     * Like σε μήνυμα
     */
    public void likeMessage(Message message) throws UserAlreadyLikesPostException {
        message.addLikeFromUser(this.username);
    }

    /**
     * Ανάκτηση λίστας φίλων χρήστη
     */
    public List<User> getFriendList() {
        return Network.getInstance().getUserFriendList(this);
    }

    /**
     * Ανάκτηση εισερχόμενων αιτημάτων φιλίας
     */
    public List<FriendRequest> getIncomingFriendRequests() {
        List<FriendRequest> incomingFriendRequests = new ArrayList<>();

        for (FriendRequest friendRequest : friendRequests) {
            if (friendRequest.getDirection().equals(FriendRequestDirection.INCOMING) && friendRequest.getStatus().equals(FriendRequestStatus.PENDING)) {
                incomingFriendRequests.add(friendRequest);
            }
        }

        return incomingFriendRequests;
    }

    /**
     * Προσθήκη αιτήματος φιλίας στην λίστα
     */
    private void addFriendRequestToList(FriendRequest friendRequest) {
        this.friendRequests.add(friendRequest);
    }

    //
    // Getters - Setters
    //
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    public List<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

    @Override
    public String toString() {
        return "Username: " + username +
                "\nEmail: " + email;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof User)) {
            return false;
        }

        User userObj = (User) obj;

        return this.username.equals(userObj.username) || this.email.equals(userObj.email);
    }
}
