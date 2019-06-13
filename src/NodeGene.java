public class NodeGene {
    enum TYPE {
        INPUT, HIDDEN, OUTPUT;
    }

    private TYPE type;
    private int id;

    public NodeGene(TYPE type, int id) {
        super();
        this.type = type;
        this.id = id;
    }

    public TYPE getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public NodeGene copy(){
        return new NodeGene(type, id);
    }
}
