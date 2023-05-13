package com.socialnetwork.exceptions;

public class UserExistsException extends Exception {

    public UserExistsException() {
        super("WARNING: User with given username and/or email already exists! (Υπάρχει ήδη καταχωρημένος χρήστης με το δοθέν όνομα ή/και email)");
    }

}
