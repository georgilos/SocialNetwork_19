package com.socialnetwork.exceptions;

/**
 * Exception μη έγκυρης επιλογής από μενού
 */
public class InvalidChoiceException extends Exception {

    public InvalidChoiceException() {
        super("WARNING: Invalid choice! (Μη έγκυρη επιλογή)");
    }

}
