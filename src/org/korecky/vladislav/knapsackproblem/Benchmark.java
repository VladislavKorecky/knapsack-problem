package org.korecky.vladislav.knapsackproblem;

import org.korecky.vladislav.knapsackproblem.environment.ItemList;
import org.korecky.vladislav.knapsackproblem.evolution.GeneticAlgorithm;
import org.korecky.vladislav.knapsackproblem.evolution.Solution;


/**
 * Benchmark the genetic algorithm to see how close can it get to the best solution.
 */
public class Benchmark {
    final int generations = 500;

    final int maxItemCount = 100;
    final int itemCountIncrement = 10;
    final float maxWeight = 40;

    final int populationSize = 1000;
    final float eliteRate = 0.1f;
    final float mutationProbability = 0.01f;

    /**
     * Test the genetic algorithm against a certain number of items.
     *
     * @param itemCount Number of items.
     * @return Percentage accuracy compared to the best solution (100% = perfect solution).
     */
    private float test(int itemCount) {
        ItemList items = new ItemList(itemCount);
        items.initialize(maxWeight);

        GeneticAlgorithm evolution = new GeneticAlgorithm(populationSize, items, maxWeight);

        for (int i = 0; i < generations; i++) {
            // test population and calculate the fitness values
            evolution.testPopulation();

            // turn the fitness values into probabilities of getting picked
            evolution.generateProbabilities();

            // create a new population by using selection, crossover and mutation + elitism
            evolution.generateChildren(eliteRate, mutationProbability);
        }

        // get the best member of the population
        Solution solution = evolution.getBestSolution();

        // get selected items of the solution
        ItemList solutionItems = items.getSublist(solution.getSolutionMask());

        // get the value of the best solution and print it out
        float value = solutionItems.getTotalValue(maxWeight);

        // calculate the value of the best possible solution
        float bestValue = items.getTotalValue();

        // calculate in percentages how good is the solution compared to the optimal solution (100% = perfect solution)
        return value / bestValue * 100;
    }

    /**
     * Run the benchmark test.
     */
    public void benchmark() {
        // test out different item counts
        for (int itemCount = itemCountIncrement; itemCount <= maxItemCount; itemCount += itemCountIncrement) {
            // test the genetic algorithm on the item count
            float accuracy = test(itemCount);

            // print the benchmark result
            System.out.printf("%d | %.2f\n", itemCount, accuracy);
        }
    }

    public static void main(String[] args) {
        Benchmark benchmark = new Benchmark();
        benchmark.benchmark();
    }
}
