package com.socialnetwork.core;

import com.socialnetwork.enums.FriendRequestDirection;
import com.socialnetwork.enums.FriendRequestStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Αίτημα φιλίας
 */
public class FriendRequest implements Serializable {

    // Στάλθηκε από
    private String fromUser;

    // Στάλθηκε στον
    private String toUser;

    // Ημ/νία αποστολής αιτήματος
    private Date sentOnDate;

    // Κατάσταση
    private FriendRequestStatus status;

    // Κατεύθυνση αιτήματος
    private FriendRequestDirection direction;

    public FriendRequest(String fromUser, String toUser, FriendRequestDirection direction) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.sentOnDate = new Date();
        this.status = FriendRequestStatus.PENDING;
        this.direction = direction;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public Date getSentOnDate() {
        return sentOnDate;
    }

    public void setSentOnDate(Date sentOnDate) {
        this.sentOnDate = sentOnDate;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    public FriendRequestDirection getDirection() {
        return direction;
    }

    public void setDirection(FriendRequestDirection direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        String str = this.direction.toString();

        if (this.direction.equals(FriendRequestDirection.INCOMING)) {
            str += " friend request from: '" + this.fromUser + "'.";
        }
        else if (this.direction.equals(FriendRequestDirection.OUTGOING)) {
            str += " friend request to: '" + this.toUser + "'.";
        }

        return str + ". Sent on " + this.sentOnDate +
                " (" + this.status.toString() + ")";
    }
}
