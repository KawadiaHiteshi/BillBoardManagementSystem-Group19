package Business.Network;

import java.util.ArrayList;

public class NetworkDirectory {

    private ArrayList<Network> networkList;

    public NetworkDirectory() {
        networkList = new ArrayList<>();
    }

    public ArrayList<Network> getNetworkList() {
        return networkList;
    }

    public Network createAndAddNetwork(String name) {
        Network network = new Network(name);
        networkList.add(network);
        return network;
    }
}
