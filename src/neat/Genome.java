package neat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Petar
 */
public class Genome {
    Random random = new Random();
    
    double fitness = 0;

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    int inputCount, outputCount;
    int connectionInnovation = 1, nodeInnovation = 1;
    
    HashMap<Integer, ConnectionGene> connectionGenes;

    public HashMap<Integer, ConnectionGene> getConnectionGenes() {
        return connectionGenes;
    }

    public HashMap<Integer, NodeGene> getNodeGenes() {
        return nodeGenes;
    }
    HashMap<Integer, NodeGene> nodeGenes;
    
    Genome(){
        connectionGenes = new HashMap<>();
        nodeGenes = new HashMap<>();
    }

    NodeGene addNodeGene(){
        NodeGene newNode = new NodeGene(nodeInnovation++, random);
        nodeGenes.put(newNode.innovation, newNode);
        return newNode;
    }
    
    NodeGene addNodeGene(NodeGene newNode){
        nodeGenes.put(newNode.innovation, newNode);
        return newNode;
    }
    
    void addConnectionGene(ConnectionGene newConnectionGene){
        connectionGenes.put(newConnectionGene.innovation, newConnectionGene);
    }
    
    void addConnectionGene(NodeGene from, NodeGene to){
        connectionGenes.put(connectionInnovation, new ConnectionGene(from, to, connectionInnovation++, random));
    }
    
    void addConnectionGene(NodeGene from, NodeGene to, double weight){
        connectionGenes.put(connectionInnovation, new ConnectionGene(from, to, connectionInnovation++, weight, random));
    }
    
    void addNodeMutation(){
        ConnectionGene toDisable = connectionGenes.get(random.nextInt(connectionGenes.size()));
        toDisable.disable();
        NodeGene newNode = addNodeGene();
        addConnectionGene(toDisable.from, newNode, 1);
        addConnectionGene(newNode, toDisable.to, toDisable.weight);
    }
    
    NodeGene getRandomNode(){
        //input and output nodes are added first so we can isolate the hidden ondes
        return nodeGenes.get(random.nextInt(nodeGenes.size()));
    }
    
    void addConnectionMutation(){
        NodeGene fromNode, toNode;
        
         //TODO: clean this up, what if fully connected?
        do{
            fromNode = getRandomNode();
            toNode = getRandomNode();
         //iterate while they are the same node or the're both INPUTS/OUTPUTS
        }while(fromNode == toNode || 
            ((fromNode.type == toNode.type) && fromNode.type != NodeType.HIDDEN) ||
            fromNode.isConnected(toNode)); //repeat if they're connected
        
        boolean reversed = fromNode.type == NodeType.OUTPUT || toNode.type == NodeType.INPUT;
        
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
}
