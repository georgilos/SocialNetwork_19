package com.socialnetwork.core;

import com.socialnetwork.exceptions.NonFriendWallException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Τοίχος χρήστη
 */
public class Wall implements Serializable {

    // Όνομα κατόχου
    private String ownerUsername;

    // Λίστα με τα post του τοίχου
    private List<Message> posts;

    public Wall(String ownerUsername) {
        this.ownerUsername = ownerUsername;
        this.posts = new ArrayList<>();
    }

    /**
     * Προσθέτει post στον τοίχο
     */
    public void addPost(Message post) throws NonFriendWallException {
        // Αν ο χρήστης που έγραψε το post δεν είναι φίλος και δεν είναι ο ιδιοκτήτης του τοίχου, βγαίνει exception
        if (!Network.getInstance().friendshipBetweenUsersExists(this.ownerUsername, post.getWrittenByUser()) && !this.ownerUsername.equals(post.getWrittenByUser())) {
            throw new NonFriendWallException();
        }

        List<Message> messages = new ArrayList<>(posts);

        // Προσθήκη post στην λίστα
        messages.add(post);

        // Ανανέωση της λίστας του τοίχου
        setPosts(messages);
    }

    /**
     * Προσθέτει απάντηση σε post
     */
    public void addReplyToPost(Message replyMessage, Message post)  throws NonFriendWallException {
        // Αν ο χρήστης που έγραψε το post δεν είναι φίλος και δεν είναι ο ιδιοκτήτης του τοίχου, βγαίνει exception
        if (!Network.getInstance().friendshipBetweenUsersExists(this.ownerUsername, replyMessage.getWrittenByUser()) && !this.ownerUsername.equals(replyMessage.getWrittenByUser())) {
            throw new NonFriendWallException();
        }

        // Προσθήκη απάντησης στο post
        post.addReply(replyMessage);
    }

    //
    // Getters - Setters
    //
    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    /**
     * Επιστρέφει την λίστα με τα μηνύματα ταξινομημένη με βάση την ημ/νία
     */
    public List<Message> getPosts() {
        List<Message> messages = new ArrayList<>(posts);

        // Ταξινόμηση
        Collections.sort(messages);

        return messages;
    }

    public void setPosts(List<Message> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        if (posts.isEmpty()) {
            return "There are no posts.";
        }

        for (int i = 0; i < posts.size(); i++) {
            str.append(posts.get(i).toString());

            if (i != posts.size() - 1) {
                str.append("\n------------------------------------------------------------------------------------------------------------\n");
            }
        }

        return str.toString();
    }
}
