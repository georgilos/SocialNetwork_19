package com.socialnetwork.core;

import com.socialnetwork.exceptions.UserAlreadyLikesPostException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Μήνυμα/post
 */
public class Message implements Comparable<Message>, Serializable {

    // Χρήστης συγγραφής του μηνύματος
    private String writtenByUser;

    // Ημ/νία συγγραφής
    private Date writtenOnDate;

    // Κείμενο μηνύματος
    private String text;

    // Αριθμός like
    private int likes;

    // Απαντήσεις
    private List<Message> replies;

    // Ονόματα χρηστών που έχουν κάνει Like
    private List<String> likedBy;

    // Flag που είναι true αν είναι post και false αν είναι reply
    private boolean isPost;

    public Message(String writtenByUser, String text, boolean isPost) {
        this.writtenByUser = writtenByUser;
        this.text = text;
        this.writtenOnDate = new Date();
        this.likes = 0;
        this.likedBy = new ArrayList<>();
        this.replies = new ArrayList<>();
        this.isPost = isPost;
    }

    /**
     * Προσθέτει απάντηση στο μήνυμα
     */
    public void addReply(Message reply) {
        List<Message> messages = new ArrayList<>(replies);

        // Προσθήκη απάντησης
        messages.add(reply);

        // Ανανέωση λίστας απαντήσεων
        setReplies(messages);
    }

    /**
     * Προσθέτει like στο μήνυμα, από τον χρήστη με το δοθέν username
     */
    public void addLikeFromUser(String username) throws UserAlreadyLikesPostException {
        // Αν ο χρήστης με το δοθέν username δεν έχει κάνει ήδη like στο μήνυμα,
        // προσθήκη του ονόματος του στην λίστα και αύξηση των like του μηνύματος κατα 1
        if (!likedBy.contains(username)) {
            this.likedBy.add(username);
            this.likes++;
        }
        else {
            throw new UserAlreadyLikesPostException();
        }
    }

    //
    // Getters - Setters
    //
    public String getWrittenByUser() {
        return writtenByUser;
    }

    public void setWrittenByUser(String writtenByUser) {
        this.writtenByUser = writtenByUser;
    }

    public Date getWrittenOnDate() {
        return writtenOnDate;
    }

    public void setWrittenOnDate(Date writtenOnDate) {
        this.writtenOnDate = writtenOnDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Message> getReplies() {
        return replies;
    }

    public void setReplies(List<Message> replies) {
        this.replies = replies;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }

    public boolean getIsPost() {
        return isPost;
    }

    public void setIsPost(boolean isPost) {
        this.isPost = isPost;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(writtenOnDate + " by " + writtenByUser + ": " + text);

        if (isPost) {
            str.append(" (Likes: ").append(likes).append(")");
        }

        if (!replies.isEmpty()) {
            for (Message reply : replies) {
                str.append("\n\t").append(reply.toString());
            }
        }

        return str.toString();
    }

    @Override
    public int compareTo(Message o) {
        if (getWrittenOnDate() == null || o.getWrittenOnDate() == null) return 0;

        return getWrittenOnDate().compareTo(o.getWrittenOnDate());
    }
}
