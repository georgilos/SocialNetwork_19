package com.socialnetwork.exceptions;

/**
 * Exception εισαγωγής μη έγκυρου ονόματος χρήστη
 */
public class InvalidUsernameException extends Exception {

    public InvalidUsernameException() {
        super("WARNING: User with given username not found! (Δεν βρέθηκε χρήστης με το δοθέν όνομα)");
    }

}
