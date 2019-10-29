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
        return addNodeGene(TYPE.HIDDEN);
    }

    public NodeGene addNodeGene(TYPE type){
        NodeGene newNode = new NodeGene(nodeInnovation++, type, random);
        updateNodeGenes(newNode);
        nodeGenes.put(newNode.innovation, newNode);
        return newNode;
    }
    
    public NodeGene addNodeGene(NodeGene newNode){
        updateNodeGenes(newNode);
        nodeGenes.put(newNode.innovation, newNode);
        return newNode;
    }

    public void updateNodeGenes(NodeGene newNode){
        for(NodeGene gene : nodeGenes.values()){
           gene.addFreeNode(newNode); 
        }
    }
    
    public ConnectionGene addConnectionGene(ConnectionGene newConnectionGene){
        connectionGenes.put(newConnectionGene.innovation, newConnectionGene);
        return newConnectionGene;
    }
    
    public ConnectionGene addConnectionGene(NodeGene from, NodeGene to){
        return addConnectionGene(from, to, random.nextDouble());
    }

    public ConnectionGene addConnectionGene(NodeGene from, NodeGene to, int innovation, boolean enable){
        return addConnectionGene(from, to, innovation, random.nextDouble(), enable);
    }

    public ConnectionGene addConnectionGene(NodeGene from, NodeGene to, int innovation, double weight, boolean enable){
        ConnectionGene newConnectoionGene = new ConnectionGene(from, to, innovation, weight, random);
        if(!enable)
            newConnectoionGene.disable();
        connectionGenes.put(innovation, newConnectoionGene);
        return newConnectoionGene;
    }
    
    public ConnectionGene addConnectionGene(NodeGene from, NodeGene to, double weight){
        return addConnectionGene(from, to, connectionInnovation++, weight, true);
    }

    public void removeConnectionGene(int key){
        connectionGenes.remove(key);
    }

    public void disableConnectionGene(int key){
        connectionGenes.get(key).disable();
    }

    public void enableConnectionGene(int key){
        connectionGenes.get(key).enable();
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
        
        if(this.isFullyConnected()){
            return;
        }

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
    public static Genome Crossover(Genome parent1, Genome parent2, Random random){
        Genome child = new Genome();
        
        //get nodes from most fit parent
        for(NodeGene node : parent1.getNodeGenes().values()){
            child.addNodeGene(node.copy());
        }

        int youngestGene = Math.max(parent1.getYougestConnectionGene(), parent2.getYougestConnectionGene());
        
        for(int i = 1; i < youngestGene; i++){
            if(parent1.getConnectionGenes().keySet().contains(i)){
                if(parent2.getConnectionGenes().keySet().contains(i)){//MATCHING GENE
                    if(random.nextBoolean())
                        child.addConnectionGene(parent1.getConnectionGenes().get(i).copy());
                    else
                        child.addConnectionGene(parent2.getConnectionGenes().get(i).copy());
                }
                else{
                    child.addConnectionGene(parent1.getConnectionGenes().get(i).copy());
                }
            }
        }
/*
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
 */       
        return child;
    }

    public double getFitness() {
        return fitness;
    }

    public boolean isFullyConnected(){
       for(NodeGene gene1 : nodeGenes.values()){
           if(!gene1.getFreeNodes().isEmpty()) //If there are nodes that can be connected its not fully connected
                return false;
       }
        return true;
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

    int getYougestConnectionGene(){
        int i = 1;
        for(int a : connectionGenes.keySet()){
            i = a>i ? a : i;
        }
        return i;
    }
}
