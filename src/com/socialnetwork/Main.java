package com.socialnetwork;

import com.socialnetwork.core.Network;

public class Main {

    public static void main(String[] args) {

        // Ανάκτηση δικτύου από αρχείο
        Network.getInstance().readNetworkFromFile();

        // Έναρξη μενού
        Network.getInstance().startNetworkMenu();
    }

}
