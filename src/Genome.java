import sun.nio.cs.ArrayEncoder;

import javax.xml.soap.Node;
import java.sql.Array;
import java.sql.Connection;
import java.util.*;

public class Genome {

    private Map<Integer, ConnectionGene> connections;
    private Map<Integer, NodeGene> nodes;

    public Genome() {
        nodes = new HashMap<Integer, NodeGene>();
        connections = new HashMap<Integer, ConnectionGene>();
    }

    public void addNodeGene(NodeGene node) {
        nodes.put(node.getId(), node);
    }

    public void addConnectionGene(ConnectionGene gene) {
        connections.put(gene.getInnovation(), gene);
    }

    public Map<Integer, ConnectionGene> getConnectionGenes() {
        return connections;
    }

    public Map<Integer, NodeGene> getNodeGenes() {
        return nodes;
    }

    public void addConnectionMutation(Random r) {
        Map<Integer, NodeGene> inputs;
        Map<Integer, NodeGene> hiddens;
        Map<Integer, NodeGene> outputs;

        inputs = new HashMap<Integer, NodeGene>();
        hiddens = new HashMap<Integer, NodeGene>();
        outputs = new HashMap<Integer, NodeGene>();

        ArrayList possibleConnections = new ArrayList<ArrayList>();

        //Separate the nodes by type, to prevent unsupported connections (eg. Output to Output node)
        for (NodeGene node : nodes.values()) {
            if (node.getType() == NodeGene.TYPE.INPUT) {
                inputs.put(node.getId(), node);
            }
            else if (node.getType() == NodeGene.TYPE.HIDDEN) {
                hiddens.put(node.getId(), node);
            }
            else {
                outputs.put(node.getId(), node);
            }
        }

        //For every input node, check all possible connections to hidden and output nodes and add those to the possibilities
        for (NodeGene node : inputs.values()) {

            for (NodeGene hiddenNode : hiddens.values()) {
                ArrayList currentConnections = new ArrayList<Integer>();
                if (!isConnected(node.getId(), hiddenNode.getId(), connections)) {
                    currentConnections.add(node.getId());
                    currentConnections.add(hiddenNode.getId());
                    possibleConnections.add(currentConnections.clone());
                }
            }

            for (NodeGene outputNode : outputs.values()) {
                ArrayList currentConnections = new ArrayList<Integer>();
                if (!isConnected(node.getId(), outputNode.getId(), connections)) {
                    currentConnections.add(node.getId());
                    currentConnections.add(outputNode.getId());
                    possibleConnections.add(currentConnections.clone());
                }
            }
        }

        //For every hidden node, check all possible connections to hidden and output nodes and add those to the possibilities
        for (NodeGene node : hiddens.values()) {
            for (NodeGene hiddenNode : hiddens.values()) {
                ArrayList currentConnections = new ArrayList<Integer>();
                if (!isConnected(node.getId(), hiddenNode.getId(), connections)) {
                    currentConnections.add(node.getId());
                    currentConnections.add(hiddenNode.getId());
                    possibleConnections.add(currentConnections.clone());
                }
            }

            for (NodeGene outputNode : outputs.values()) {
                ArrayList currentConnections = new ArrayList<Integer>();
                if (!isConnected(node.getId(), outputNode.getId(), connections)) {
                    currentConnections.add(node.getId());
                    currentConnections.add(outputNode.getId());
                    possibleConnections.add(currentConnections.clone());
                }
            }
        }
    }

    private boolean isConnected(int idNode1, int idNode2, Map<Integer, ConnectionGene> connectionList) {
        for (ConnectionGene connection : connectionList.values()) {
            if (connection.getInNode() == idNode1 && connection.getOutNode() == idNode2) {
                return true;
            }
        }
        return false;
    }

    public void addNodeMutation(Random r) {
        ConnectionGene connection = connections.get(r.nextInt(connections.size()));
        connection.disable();
        System.out.println(connection);
        NodeGene newNode = new NodeGene(NodeGene.TYPE.HIDDEN, nodes.size());
        connections.put(TBot.innovation, new ConnectionGene(connection.getInNode(), newNode.getId(), 1, true, TBot.innovation));
        connections.put(connection.getInnovation(), new ConnectionGene(newNode.getId(), connection.getOutNode(), connection.getWeight(), connection.isExpressed(), connection.getInnovation()));
        TBot.innovation++;
    }

    public static Genome crossOver(Genome parent1, Genome parent2, Random r) {
        Genome child = new Genome();
        for (NodeGene parent1Node : parent1.getNodeGenes().values()) {
            child.addNodeGene(parent1Node.copy());
        }

        int maximumInnovation = 0;
        for (ConnectionGene parent1Connection : parent1.getConnectionGenes().values()) {
            if (parent1Connection.getInnovation() > maximumInnovation) {
                maximumInnovation = parent1Connection.getInnovation();
            }
            if (parent2.getConnectionGenes().containsKey(parent1Connection.getInnovation())) { //Matching gene
                int choice = r.nextInt(2);
                if (choice == 0) {
                    child.addConnectionGene(parent1Connection.copy());
                }
                else {
                    child.addConnectionGene(parent2.getConnectionGenes().get(parent1Connection.getInnovation()).copy());
                }
            }
            else { //Disjoint or excess gene FROM PARENT1
                child.addConnectionGene(parent1Connection.copy());
            }
        }

        for (ConnectionGene parent2Connection : parent2.getConnectionGenes().values()) {
            if (!parent1.getConnectionGenes().containsKey(parent2Connection.getInnovation()) && parent2Connection.getInnovation() < maximumInnovation) {
                child.addConnectionGene(parent2Connection.copy());
            }
        }
        return child;
    }

    @Override
    public String toString() {
        for (ConnectionGene connection : connections.values()) {
            System.out.println(connection.getInNode() + " -> " + connection.getOutNode());
            System.out.println("Innovation: " + connection.getInnovation()) ;
        }
        System.out.println();
        return "Genome";
    }

}
