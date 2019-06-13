import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class TBot {
    public static int innovation = 11;

    public static void main(String [ ] args) {
        Random r = new Random();
        Genome testGenome = new Genome();
        testGenome.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT, 1));
        testGenome.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT, 2));
        testGenome.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT, 3));
        testGenome.addNodeGene(new NodeGene(NodeGene.TYPE.HIDDEN, 4));
        testGenome.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT, 5));
        testGenome.addConnectionGene(new ConnectionGene(1,4, 1, true, 1));
        testGenome.addConnectionGene(new ConnectionGene(2,4, 1, false, 2));
        testGenome.addConnectionGene(new ConnectionGene(3,4, 1, true, 3));
        testGenome.addConnectionGene(new ConnectionGene(2,5, 1, true, 4));
        testGenome.addConnectionGene(new ConnectionGene(5,4, 1, true, 5));
        testGenome.addConnectionGene(new ConnectionGene(1,5, 1, true, 8));
        testGenome.toString();
        testGenome.addConnectionMutation(r);
        testGenome.toString();
    }
}
