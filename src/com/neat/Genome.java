package com.neat;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;


/**
 *
 * @author Petar
 */
public class Genome implements Comparable<Genome> {
    Random random; 
    
    double fitness = 0;
    
    public int inputCount, outputCount;
    int connectionInnovation = 1, nodeInnovation = 1;
    
    HashMap<Integer, ConnectionGene> connectionGenes;

    HashMap<Integer, NodeGene> nodeGenes;

    public Genome(int inputCount, int outputCount){
        random = new Random();
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        connectionGenes = new HashMap<>();
        nodeGenes = new HashMap<>();

        for(int i = 0; i < inputCount; i++){
            addNodeGene(TYPE.INPUT);
        }

        for(int i = 0; i < outputCount; i++){
            addNodeGene(TYPE.OUTPUT);
        }

        this.fullyConnect();

    }

    public Genome(){
        this(1,1);
    }

    public Genome copy(){
        Genome g = new Genome(this.inputCount, this.outputCount);
        g.connectionInnovation = this.connectionInnovation;
        g.nodeInnovation = this.nodeInnovation;

        for(Map.Entry<Integer, ConnectionGene> entry : this.connectionGenes.entrySet()){
            g.connectionGenes.put(entry.getKey(), entry.getValue().copy());
        }

        for(Map.Entry<Integer, NodeGene> entry : this.nodeGenes.entrySet()){
            g.nodeGenes.put(entry.getKey(), entry.getValue().copy());
        }

        return g;
    }

    public void mutate(){
       if(NEAT.PROBABILITY_MUTATE_CONNECTION > random.nextDouble()){
            addConnectionMutation();
       } 
       if(NEAT.PROBABILITY_MUTATE_NODE > random.nextDouble()){
           addNodeMutation();
       } 
       if(NEAT.PROBABILITY_MUTATE_WEIGHT_SHIFT > random.nextDouble()){
           pertrubeWeightConnectionMutation();
       } 
       if(NEAT.PROBABILITY_MUTATE_WEIGHT_RANDOM > random.nextDouble()){
           randomWeightConnectionMutation();
       } 
       if(NEAT.PROBABILITY_MUTATE_TOGGLE_CONNECTION > random.nextDouble()){
           toggleConnectionMutation();
       } 

    }

    public void reset(){
        for(NodeGene n : nodeGenes.values()){
            n.reset();
        }

        for(ConnectionGene c : connectionGenes.values()){
            c.reset();
        }
    }

    void fullyConnect(){
        while(!this.isFullyConnected()){
            this.addConnectionMutation();
        }
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
        toDisable.setEnable(false);
        NodeGene newNode = addNodeGene();
        double fromNodeX = nodeGenes.get(toDisable.getFromNodeId()).getX();
        double toNodeX = nodeGenes.get(toDisable.getToNodeId()).getX();

        newNode.setX( (fromNodeX + toNodeX) / 2);
            
        if(fromNodeX < toNodeX){
            addConnectionGene(toDisable.getFromNodeId(), newNode.getId(), 1);
            addConnectionGene(newNode.getId(), toDisable.getToNodeId(),toDisable.weight);
        }
        else if(fromNodeX > toNodeX){
            addConnectionGene(toDisable.getToNodeId(), newNode.getId(), 1);
            addConnectionGene(newNode.getId(), toDisable.getFromNodeId(),toDisable.weight);
        }
        else{
            return;
        }

    }
    
    NodeGene getRandomNode(){
        Object[] keys = nodeGenes.keySet().toArray();
        Integer key = (Integer)keys[random.nextInt(keys.length)];

        return nodeGenes.get(key);
    }

////////////////////////////////
///////////CONNECTIONS////////////////////

    public ConnectionGene addConnectionGene(int from, int to, int innovation, double weight, boolean enable){
        ConnectionGene newConnectionGene = new ConnectionGene(from, to, innovation, weight, random);
        
        newConnectionGene.setEnable(enable);

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
            newConnectionGene.setEnable(false);
        }

        return newConnectionGene;
    }

    public void removeConnectionGene(int key){
        connectionGenes.remove(key);
    }

    public void disableConnectionGene(int key){
        connectionGenes.get(key).setEnable(false);
    }

    public void enableConnectionGene(int key){
        connectionGenes.get(key).setEnable(true);
    }
    

    ConnectionGene getRandomConnectionGene(){
        Integer i;
        i = (Integer)connectionGenes.keySet().toArray()[random.nextInt(connectionGenes.keySet().size())];

        ConnectionGene c = connectionGenes.get(i);

        if(c == null){
                throw new RuntimeException("Random connection gene is null");
        }

        return c; 
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
            fromNode.isConnected(toNode) || fromNode.getX() == toNode.getX());//repeat if they're connected
        
        //boolean reversed = fromNode.type == TYPE.OUTPUT || toNode.type == TYPE.INPUT;
        boolean reversed = (fromNode.getX() > toNode.getX());
        
        if(reversed)
            addConnectionGene(toNode.getId(), fromNode.getId(), random.nextDouble());
        else
            addConnectionGene(fromNode.getId(), toNode.getId(), random.nextDouble());
       
    }

    public void pertrubeWeightConnectionMutation(){
       ConnectionGene c = getRandomConnectionGene();

        c.setWeight(c.getWeight() + (random.nextDouble() * 2 - 1) * NEAT.WEIGHT_SHIFT_STRENGTH);
    }

    public void randomWeightConnectionMutation(){
       ConnectionGene c = getRandomConnectionGene();

        c.setWeight((random.nextDouble() * 2 - 1) * NEAT.WEIGHT_RANDOM_STRENGTH);
    }

    public void toggleConnectionMutation(){
       ConnectionGene c = getRandomConnectionGene();

       c.setEnable(!c.isEnabled());
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

    public void setConnectionGenes(HashMap<Integer, ConnectionGene> connectionGenes) {
        this.connectionGenes = connectionGenes;
    }

    public void setNodeGenes(HashMap<Integer, NodeGene> nodeGenes) {
        this.nodeGenes = nodeGenes;
    }
}
