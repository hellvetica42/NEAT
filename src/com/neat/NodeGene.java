package com.neat;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;
import java.util.Collection;
import java.util.Comparator;
/**
 *
 * @author Petar
 */
public class NodeGene {
    final int innovation;
    
    Random random;
    
    TYPE type;

    double x, y;

    double input = 0;
    double output = 0;

    boolean activated = false;
    
    ArrayList<ConnectionGene> inputs = new ArrayList<>();
    ArrayList<ConnectionGene> outputs = new ArrayList<>();

    HashSet<Integer> freeNodes = new HashSet<>();
    HashSet<Integer> connectedNodes = new HashSet<>();
    

    public NodeGene(int innovation, TYPE type, Random random){
        this.innovation = innovation;
        this.type = type;
        this.random = random;
        if(this.type == TYPE.INPUT)
            this.x = 0;
        else if(this.type == TYPE.OUTPUT)
            this.x = 1;

        this.y = this.random.nextDouble();
    }
    public NodeGene(int innovation, Random random){
        this(innovation, TYPE.HIDDEN, random);
        
    }

    void setOutput(double x){
        this.activated = true;
        this.output = x;
    }

    double calculate(){

        if(!activated){
            double x = 0;
            for(ConnectionGene i : inputs){
               if(i.isEnabled()  ){
                   if(i.isActivated())
                        x += i.getActivation();
                    else 
                        throw new RuntimeException("Previous connection was not activated");
               } 
            }

            this.activated = true;
            this.output = 1 / (1 + Math.exp(-x));
        } 

        for(ConnectionGene o : outputs){
            o.activate(this.output);
        }

        return this.output;
    }

    void reset(){
        this.activated = false;
        this.output = 0;
    }
    
    void connectInput(ConnectionGene in){
        if(this.type != TYPE.INPUT){
            inputs.add(in);
            freeNodes.remove(in.from);
            connectedNodes.add(in.from);
        }
    }
    
    void connectOutput(ConnectionGene out){
        if(this.type != TYPE.OUTPUT){
            outputs.add(out);
            freeNodes.remove(out.to);
            connectedNodes.add(out.to);
        }
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

    public static Comparator<NodeGene> innovationComparator = new Comparator<NodeGene>(){
        @Override
            public int compare(NodeGene n1, NodeGene n2){
                if(n1.getId() > n2.getId())
                    return 1;
                else if(n2.getId() > n1.getId())
                    return -1;
                else
                    return 0;
            }
   };

    public static Comparator<NodeGene> xComparator = new Comparator<NodeGene>(){
        @Override
            public int compare(NodeGene n1, NodeGene n2){
                if(n1.getX() > n2.getX())
                    return 1;
                else if(n2.getX() > n1.getX())
                    return -1;
                else
                    return 0;
            }
   };

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

    public HashSet<Integer> getFreeNodes(){
        return freeNodes;
    }

    void setX(double x){
        this.x = x;
    }

    void setY(double y){
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public ArrayList<ConnectionGene> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<ConnectionGene> inputs) {
        this.inputs = inputs;
    }

    public ArrayList<ConnectionGene> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<ConnectionGene> outputs) {
        this.outputs = outputs;
    }
}
