package com.socialnetwork.exceptions;

/**
 * Exception απόπειρας αποστολής αιτήματος φιλίας σε χρήστη που είναι ήδη φίλος
 */
public class AlreadyFriendException extends Exception {

    public AlreadyFriendException() {
        super("WARNING: The selected user is already a friend! (Ο χρήστης είναι ήδη φίλος σας)");
    }

}
