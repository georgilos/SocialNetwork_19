package com.socialnetwork.exceptions;

/**
 * Exception απόπειρας like σε post που ο χρήστης έχει ήδη κάνει like
 */
public class UserAlreadyLikesPostException extends Exception {

    public UserAlreadyLikesPostException() {
        super("WARNING: You have already liked this post! (Έχετε ήδη κάνει like σε αυτό το μήνυμα)");
    }

}
