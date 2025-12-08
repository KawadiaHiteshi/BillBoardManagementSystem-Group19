package Business;

import Business.Network.Network;
import Business.Network.NetworkDirectory;
import Business.UserAccount.UserAccountDirectory;
import java.util.ArrayList;

public class EcoSystem {

    private static EcoSystem business;
    private ArrayList<Network> networkList;
    private UserAccountDirectory userAccountDirectory;

    // Private Constructor
    private EcoSystem() {
        networkList = new ArrayList<>();
        userAccountDirectory = new UserAccountDirectory();
    }

    // Singleton Access Method
    public static EcoSystem getInstance() {
        if (business == null) {
            business = new EcoSystem();
        }
        return business;
    }

    public ArrayList<Network> getNetworkList() {
        return networkList;
    }

    public Network createAndAddNetwork(String name) {
        Network network = new Network(name);
        networkList.add(network);
        return network;
    }

    public UserAccountDirectory getUserAccountDirectory() {
        return userAccountDirectory;
    }
}
