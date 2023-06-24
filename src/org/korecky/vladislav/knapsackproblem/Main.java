package org.korecky.vladislav.knapsackproblem;

import org.korecky.vladislav.knapsackproblem.environment.ItemList;
import org.korecky.vladislav.knapsackproblem.evolution.GeneticAlgorithm;
import org.korecky.vladislav.knapsackproblem.evolution.Solution;

public class Main {
    public static void main(String[] args) {
        // CONFIG
        final int epochs = 500;

        final int itemCount = 100;
        final float maxWeight = 40;

        final int populationSize = 1000;
        final float eliteRate = 0.1f;
        final float mutationProbability = 0.01f;

        // SETUP
        ItemList items = new ItemList(itemCount);
        items.initialize();

        // EVOLUTION
        GeneticAlgorithm evolution = new GeneticAlgorithm(populationSize, items, maxWeight);

        for (int i = 0; i < epochs; i++) {
            // test population and calculate the fitness values
            evolution.testPopulation();

            // get the best member of the population
            Solution bestSolution = evolution.getBestSolution();

            // get selected items of the solution
            ItemList bestItems = items.getSublist(bestSolution.getSolutionMask());

            // get the value of the best solution and print it out
            float bestValue = bestItems.getTotalValue(maxWeight);
            System.out.println(bestValue);

            // turn the fitness values into probabilities of getting picked
            evolution.generateProbabilities();

            // create a new population by using selection, crossover and mutation + elitism
            evolution.generateChildren(eliteRate, mutationProbability);
        }
    }
}
