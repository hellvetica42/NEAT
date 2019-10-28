package com.neat;

import java.util.HashMap;
import java.util.Random;


/**
 *
 * @author Petar
 */
public class Genome {
    Random random; 
    
    double fitness = 0;

    
    int inputCount, outputCount;
    int connectionInnovation = 1, nodeInnovation = 1;
    
    public Genome(int inputCount, int outputCount){
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        connectionGenes = new HashMap<>();
        nodeGenes = new HashMap<>();
        random = new Random();
    }

    public Genome(){
        this(1,1);
    }

    HashMap<Integer, ConnectionGene> connectionGenes;

    HashMap<Integer, NodeGene> nodeGenes;
    

    public NodeGene addNodeGene(){
        NodeGene newNode = new NodeGene(nodeInnovation++, random);
        nodeGenes.put(newNode.innovation, newNode);
        return newNode;
    }

    public NodeGene addNodeGene(TYPE type){
        NodeGene newNode = new NodeGene(nodeInnovation++, type, random);
        nodeGenes.put(newNode.innovation, newNode);
        return newNode;
    }
    
    public NodeGene addNodeGene(NodeGene newNode){
        nodeGenes.put(newNode.innovation, newNode);
        return newNode;
    }
    
    public void addConnectionGene(ConnectionGene newConnectionGene){
        connectionGenes.put(newConnectionGene.innovation, newConnectionGene);
    }
    
    public void addConnectionGene(NodeGene from, NodeGene to){
        addConnectionGene(from, to, random.nextDouble());
    }
    
    public void addConnectionGene(NodeGene from, NodeGene to, double weight){
        connectionGenes.put(connectionInnovation, new ConnectionGene(from, to, connectionInnovation++, weight, random));
    }
    
    public void addNodeMutation(){
        ConnectionGene toDisable = getRandomConnectionGene();
        toDisable.disable();
        NodeGene newNode = addNodeGene();
        addConnectionGene(toDisable.from, newNode, 1);
        addConnectionGene(newNode, toDisable.to, toDisable.weight);
    }
    
    NodeGene getRandomNode(){
        Integer i;
        i = (Integer)nodeGenes.keySet().toArray()[random.nextInt(nodeGenes.keySet().size())];

        return nodeGenes.get(i);
    }

    ConnectionGene getRandomConnectionGene(){
        Integer i;
        i = (Integer)connectionGenes.keySet().toArray()[random.nextInt(connectionGenes.keySet().size())];

        return connectionGenes.get(i);
    }
    
    public void addConnectionMutation(){
        NodeGene fromNode, toNode;
        
         //TODO: clean this up, what if fully connected?
        do{
            fromNode = getRandomNode();
            toNode = getRandomNode();
         //iterate while they are the same node or the're both INPUTS/OUTPUTS
        }while(fromNode == toNode || 
            ((fromNode.type == toNode.type) && fromNode.type != TYPE.HIDDEN) ||
            fromNode.isConnected(toNode)); //repeat if they're connected
        
        boolean reversed = fromNode.type == TYPE.OUTPUT || toNode.type == TYPE.INPUT;
        
        if(reversed)
            addConnectionGene(toNode, fromNode, random.nextDouble());
        else
            addConnectionGene(fromNode, toNode, random.nextDouble());
       
    }
    
    //parent1 is more fit
    static Genome Crossover(Genome parent1, Genome parent2, Random random){
        Genome child = new Genome();
        
        //get nodes from most fit parent
        for(NodeGene node : parent1.getNodeGenes().values()){
            child.addNodeGene(node.copy());
        }
                
        for(Integer innovation : parent1.getConnectionGenes().keySet()){
            if(parent2.getConnectionGenes().containsKey(innovation)){//MATHCING gene
                if(random.nextBoolean())
                    child.addConnectionGene(parent1.getConnectionGenes().get(innovation).copy());
                else
                    child.addConnectionGene(parent2.getConnectionGenes().get(innovation).copy());
            }
            else{ //DISJOINT or EXCESS genes
                child.addConnectionGene(parent1.getConnectionGenes().get(innovation).copy());
            }
        }
        
        return child;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public HashMap<Integer, ConnectionGene> getConnectionGenes() {
        return connectionGenes;
    }

    public HashMap<Integer, NodeGene> getNodeGenes() {
        return nodeGenes;
    }
}
