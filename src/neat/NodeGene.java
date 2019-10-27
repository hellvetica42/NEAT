package neat;

import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author Petar
 */
public class NodeGene {
    final int innovation;
    
    Random random;
    
    NodeType type;
    
    ArrayList<ConnectionGene> inputs = new ArrayList<>();
    ArrayList<ConnectionGene> outputs = new ArrayList<>();
    ArrayList<NodeGene> connectedNodes = new ArrayList<>();

    public NodeGene(int innovation, NodeType type, Random random){
        this.innovation = innovation;
        this.type = type;
        this.random = random;
    }
    public NodeGene(int innovation, Random random){
        this(innovation, NodeType.HIDDEN, random);
        
    }
    
    void connectInput(ConnectionGene in){
        inputs.add(in);
        connectedNodes.add(in.from);
    }
    
    void connectOutput(ConnectionGene out){
        outputs.add(out);
        connectedNodes.add(out.to);
    }
    
    boolean isConnected(NodeGene node){
        return connectedNodes.contains(node);
    }
    
    NodeGene copy(){
        return new NodeGene(innovation, type, random);
    }
}
