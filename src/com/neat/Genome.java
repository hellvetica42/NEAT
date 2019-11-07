package com.neat;

import java.util.HashMap;
import java.util.Random;


/**
 *
 * @author Petar
 */
public class Genome implements Comparable<Genome> {
    Random random; 
    
    double fitness = 0;
    
    int inputCount, outputCount;
    int connectionInnovation = 1, nodeInnovation = 1;
    
    HashMap<Integer, ConnectionGene> connectionGenes;

    HashMap<Integer, NodeGene> nodeGenes;

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

    
//////NODES////////////////
    public NodeGene addNodeGene(){
        return addNodeGene(TYPE.HIDDEN);
    }

    public NodeGene addNodeGene(TYPE type){
        NodeGene newNode = new NodeGene(nodeInnovation++, type, random);
        newNode.addFreeNodes(nodeGenes.values());  
        updateNodeGenes(newNode);

        nodeGenes.put(newNode.innovation, newNode);

        return newNode;
    }
    
    public NodeGene addNodeGene(NodeGene newNode){
        updateNodeGenes(newNode);
        newNode.addFreeNodes(nodeGenes.values());

        nodeGenes.put(newNode.innovation, newNode);
        return newNode;
    }

    //whenever a new node gene is created we have to tell all other nodes that they can connect to it
    //we later remove nodes that get connected via connectionGene (in NodeGene) 
    public void updateNodeGenes(NodeGene newNode){
        for(NodeGene gene : nodeGenes.values()){
           gene.addFreeNode(newNode); 
        }
    }
    
    public void addNodeMutation(){
        ConnectionGene toDisable = getRandomConnectionGene();
        toDisable.disable();
        NodeGene newNode = addNodeGene();
        addConnectionGene(toDisable.getFromNodeId(), newNode.getId(), 1);
        addConnectionGene(newNode.getId(), toDisable.getToNodeId(),toDisable.weight);
    }
    
    NodeGene getRandomNode(){
        Integer i;
        i = (Integer)nodeGenes.keySet().toArray()[random.nextInt(nodeGenes.keySet().size())];

        return nodeGenes.get(i);
    }

////////////////////////////////
///////////CONNECTIONS////////////////////

    public ConnectionGene addConnectionGene(int from, int to, int innovation, double weight, boolean enable){
        ConnectionGene newConnectionGene = new ConnectionGene(from, to, innovation, weight, random);
        if(!enable)
            newConnectionGene.disable();

        addConnectionGene(newConnectionGene);

        return newConnectionGene;
    }

    public ConnectionGene addConnectionGene(int from, int to, int innovation, boolean enable){
        return addConnectionGene(from, to, innovation, random.nextDouble(), enable);
    }

    public ConnectionGene addConnectionGene(int from, int to, double weight){
        return addConnectionGene(from, to, connectionInnovation++, weight, true);
    }

    public ConnectionGene addConnectionGene(int from, int to, boolean enable){ 
        return addConnectionGene(from, to, connectionInnovation++, random.nextDouble(), enable);
    }

    public ConnectionGene addConnectionGene(int from, int to){
        return addConnectionGene(from, to, random.nextDouble());
    }

    public ConnectionGene addConnectionGene(ConnectionGene newConnectionGene){
        connectionGenes.put(newConnectionGene.innovation, newConnectionGene);
        
        if(nodeGenes.keySet().contains(newConnectionGene.from) && nodeGenes.keySet().contains(newConnectionGene.to)){
            nodeGenes.get(newConnectionGene.from).connectOutput(newConnectionGene);
            nodeGenes.get(newConnectionGene.to).connectInput(newConnectionGene);
        }
        else{
            newConnectionGene.disable();
        }

        return newConnectionGene;
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
            addConnectionGene(toNode.getId(), fromNode.getId(), random.nextDouble());
        else
            addConnectionGene(fromNode.getId(), toNode.getId(), random.nextDouble());
       
    }

   /////////////////////////////////////////////////////////// 
    
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
                if(parent2.getConnectionGenes().keySet().contains(i)){//MATCHING GENE FORM RANDOM PARENT

                    if(random.nextBoolean())
                        child.addConnectionGene(parent1.getConnectionGenes().get(i).copy());
                    else
                        child.addConnectionGene(parent2.getConnectionGenes().get(i).copy());

                }
                else{
                    child.addConnectionGene(parent1.getConnectionGenes().get(i).copy()); //DISJONT AND EXCESS FROM MOST FIT PARENT
                }
            }
        }
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

    @Override
    public int compareTo(Genome arg0) {
        if(this.fitness < arg0.getFitness())
            return -1;
        else if(this.fitness > arg0.getFitness())
            return 1;
        else
            return 0;
    }
}
