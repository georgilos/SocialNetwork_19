package com.socialnetwork.enums;

/**
 * Κατάσταση αιτήματος φιλίας
 */
public enum FriendRequestStatus {

    ACCEPTED {
        @Override
        public String toString() {
            return "Accepted";
        }
    },

    REJECTED {
        @Override
        public String toString() {
            return "Rejected";
        }
    },

    PENDING {
        @Override
        public String toString() {
            return "Pending";
        }
    }

}
