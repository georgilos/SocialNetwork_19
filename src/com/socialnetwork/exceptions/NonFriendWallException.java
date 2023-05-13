package com.socialnetwork.exceptions;

/**
 * Exception απόπειρας ανάρτησης post σε τοίχο μη φίλου
 */
public class NonFriendWallException extends Exception {

    public NonFriendWallException() {
        super("WARNING: You have no right to post on this wall! (Δεν έχετε το δικαίωμα να αναρτήσετε μήνυμα σε αυτόν τον τοίχο)");
    }

}
