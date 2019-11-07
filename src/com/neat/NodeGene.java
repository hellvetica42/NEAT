package com.neat;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;
import java.util.Collection;
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

    HashSet<Integer> freeNodes = new HashSet<>();
    HashSet<Integer> connectedNodes = new HashSet<>();
    

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
        freeNodes.remove(in.from);
        connectedNodes.add(in.from);
    }
    
    void connectOutput(ConnectionGene out){
        outputs.add(out);
        freeNodes.remove(out.to);
        connectedNodes.add(out.to);
    }
    
    boolean isConnected(NodeGene node){
        return connectedNodes.contains(node.getId());
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

    public void addFreeNode(NodeGene gene){
        if(this.type == TYPE.HIDDEN){
            freeNodes.add(gene.getId());
        }
        else{
            if(gene.getType() != this.type)
                freeNodes.add(gene.getId());
        }
    }

    public void addFreeNodes(Collection<NodeGene> genes){
        for(NodeGene gene : genes){
            if(this.type == TYPE.HIDDEN){
                freeNodes.add(gene.getId());
            }
            else{
                if(gene.getType() != this.type)
                    freeNodes.add(gene.getId());
            }
        }
    }

    public HashSet<Integer> getFreeNodes(){
        return freeNodes;
    }
}
