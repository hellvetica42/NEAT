package com.neat;

import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author Petar
 */
public class NodeGene {
    final int innovation;
    
    Random random;
    
    TYPE type;
    
    ArrayList<ConnectionGene> inputs = new ArrayList<>();
    ArrayList<ConnectionGene> outputs = new ArrayList<>();
    ArrayList<NodeGene> connectedNodes = new ArrayList<>();

    public NodeGene(int innovation, TYPE type, Random random){
        this.innovation = innovation;
        this.type = type;
        this.random = random;
    }
    public NodeGene(int innovation, Random random){
        this(innovation, TYPE.HIDDEN, random);
        
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
    
    public TYPE getType(){
	    return this.type;
    }

    public int getId(){
        return innovation;
    }
}
