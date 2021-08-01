package com.example.nazanin.finalproject.optimization.genetic_algorithm;


import android.os.Build;
import android.support.annotation.RequiresApi;

import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by nazanin on 4/30/2020.
 */
public class GeneticAlgorithm {

    double xRange,yRange;
    private Map<Point,Double> anchors;

    public GeneticAlgorithm(Map<Point,Double> anchors,double xRange,double yRange){
        this.anchors = anchors;
        this.xRange = xRange;
        this.yRange = yRange;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Population evolve(Population population){
        return mutatePopulation(crossoverPopulation(population));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    Population crossoverPopulation(Population population){
       // boolean mapped = false;
        Population crossoverPop = new Population(population.getChromosomes().size(),anchors,xRange,yRange);
        IntStream.range(0, GeneticParams.NUM_ELITE_SCHEDUELE).forEach(x -> crossoverPop.getChromosomes().set(
                x,population.getChromosomes().get(x)));
        for (int i = GeneticParams.NUM_ELITE_SCHEDUELE;i<population.getChromosomes().size();i++){
            if(GeneticParams.CROSSOVER_RATE > Math.random()){
               // while(!mapped) {
                    Chromosome chromosome1 = selectTournamentPopulation(population).sortByFitness().getChromosomes().get(0);
                    Chromosome chromosome2 = selectTournamentPopulation(population).sortByFitness().getChromosomes().get(0);
                    Chromosome chromosome = crossoverChromosome(chromosome1, chromosome2);
//                    if (checkRoutingValidity(chromosome)){
//                        crossoverPop.getChromosomes().set(i, chromosome);
//                        mapped = true;
//                    }
//
//                }
            }
            else {
                crossoverPop.getChromosomes().set(i,population.getChromosomes().get(i));
            }
        }

        return crossoverPop;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    Chromosome crossoverChromosome(Chromosome chromosome1, Chromosome chromosome2){
        Chromosome crossoverChromosome = new Chromosome(anchors,xRange,yRange).createInitialPop();
        IntStream.range(0, crossoverChromosome.getGenes().size()).forEach(
                x->{
                    if (Math.random()>0.5) {
                        crossoverChromosome.getGenes().set(x, chromosome1.getGenes().get(x));
                    }
                    else {
                        crossoverChromosome.getGenes().set(x, chromosome2.getGenes().get(x));
                    }
                });
        return crossoverChromosome;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    Population mutatePopulation(Population population){
       // boolean mapped = false;
        Population mutatePop = new Population(population.getChromosomes().size(),anchors,xRange,yRange);
        ArrayList<Chromosome> chromosomes = mutatePop.getChromosomes();
        IntStream.range(0, GeneticParams.NUM_ELITE_SCHEDUELE).forEach(x -> chromosomes.set(
                x,population.getChromosomes().get(x)));
        for (int i = GeneticParams.NUM_ELITE_SCHEDUELE; i <population.getChromosomes().size() ; i++) {
           // while(!mapped) {
                Chromosome chromosome = mutateChromosome(population.getChromosomes().get(i));
//                if (checkRoutingValidity(chromosome)) {
//                    chromosomes.set(i, chromosome);
//                    mutatePop.getChromosomes().set(i,chromosome);
//                    mapped = true;
//                }
//            }
        }
        return mutatePop;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    Chromosome mutateChromosome(Chromosome mutateChromosome){
        Chromosome chromosome = new Chromosome(anchors,xRange,yRange).createInitialPop();
        IntStream.range(0, mutateChromosome.getGenes().size()).forEach(x->{
            if(GeneticParams.MUTATION_RATE > Math.random()) mutateChromosome.getGenes().set(x,
                    chromosome.getGenes().get(x));
        });
        return mutateChromosome;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    Population selectTournamentPopulation(Population population){

        List<Chromosome> chromosomes;
        Population tournament = new Population(GeneticParams.TOUR_SEL_SIZE,anchors,xRange,yRange);
        IntStream.range(0,GeneticParams.TOUR_SEL_SIZE).forEach(x -> {
            tournament.getChromosomes().set(x,population.getChromosomes().get(
                    (int)(Math.random()*population.getChromosomes().size())
            ));
        });
        return tournament;
    }

}
