package com.socialnetwork.enums;

/**
 * Κατεύθυνση αιτήματος φιλίας
 */
public enum FriendRequestDirection {

    INCOMING {
        @Override
        public String toString() {
            return "Incoming";
        }
    },

    OUTGOING {
        @Override
        public String toString() {
            return "Outgoing";
        }
    }

}
