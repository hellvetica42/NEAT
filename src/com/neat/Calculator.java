package com.neat;

import java.util.ArrayList;
import java.lang.RuntimeException;
import java.util.stream.Collectors;

public class Calculator{
    int inputCount, outputCount;
    Genome genome;

    ArrayList<NodeGene> inputNodes = new ArrayList<>();
    ArrayList<NodeGene> outputNodes = new ArrayList<>();
    ArrayList<NodeGene> hiddenNodes = new ArrayList<>();

    public Calculator(Genome genome){
       this.inputCount = genome.inputCount;
       this.outputCount = genome.outputCount; 
       this.genome = genome;

       inputNodes = new ArrayList<>(genome.getNodeGenes().values().stream()
                                    .filter(n -> n.getType() == TYPE.INPUT)
                                    .sorted(NodeGene.innovationComparator)
                                    .collect(Collectors.toList()));

       outputNodes = new ArrayList<>(genome.getNodeGenes().values().stream()
                                    .filter(n -> n.getType() == TYPE.OUTPUT)
                                    .sorted(NodeGene.innovationComparator)
                                    .collect(Collectors.toList()));
        
        hiddenNodes = new ArrayList<>(genome.getNodeGenes().values().stream()
                                    .filter(n -> n.getType() == TYPE.HIDDEN)
                                    .sorted(NodeGene.xComparator)
                                    .collect(Collectors.toList()));
    }

    public double[] calculate(Double... input){

        if(input.length != inputCount) throw new RuntimeException();

        double[] output = new double[outputCount];

        hiddenNodes = new ArrayList<>(genome.getNodeGenes().values().stream()
                                    .filter(n -> n.getType() == TYPE.HIDDEN)
                                    .sorted(NodeGene.xComparator)
                                    .collect(Collectors.toList()));
        this.genome.reset();

        for(NodeGene i : inputNodes){
            i.setOutput(input[i.getId()-1]);
            i.calculate();
        }

        for(NodeGene h : hiddenNodes){
            h.calculate();    
        }

        for(NodeGene o : outputNodes){
            output[o.getId()-inputCount-1] = o.calculate();
        }

        return output;
    }
}