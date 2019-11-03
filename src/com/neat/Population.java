package com.neat;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

public class Population{
    Random random;

    Population(Random random){
        this.random = random;
    }

    ArrayList<Genome> population = new ArrayList<>();
    ArrayList<Species> species = new ArrayList<>();
    Map<Genome, Species> speciesMap = new HashMap<>();

    double getAdjustedFitness(Genome g){
        return g.getFitness() / speciesMap.get(g).getSize();
    }

    ArrayList<Species> getSpecies(){
        return species;
    }

    Species createNewSpecies(){
        Species s = new Species(random);
        species.add(s);
        return s;
    }

    void killSpecies(Species s){
        if(species.contains(s)){
            species.remove(s);
        }
    }
    
    void speciate(){
        species.clear();
        speciesMap.clear();
       for(Genome g : population){
            boolean foundSpecies = false;
            for(Species s : species){
                if(Evaluator.getCompatibilityDistance(g, s.mascot) < 0.5){
                    foundSpecies = true;
                    s.addGenome(g);
                    speciesMap.put(g, s);
                    break;
                }
            }
            
            if(!foundSpecies){
                Species s = createNewSpecies();
                s.addGenome(g);
                speciesMap.put(g, s);
            }
       }
    }
}