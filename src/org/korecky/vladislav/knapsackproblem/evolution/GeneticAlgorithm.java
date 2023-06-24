package org.korecky.vladislav.knapsackproblem.evolution;

import org.korecky.vladislav.knapsackproblem.environment.ItemList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


/**
 * Implementation of the genetic algorithm.
 */
public class GeneticAlgorithm {
    private Solution[] population;
    private final ItemList itemList;
    private final float maxWeight;
    private final static Random random = new Random();

    /**
     * Start a new evolution simulation.
     *
     * @param populationSize Number of solutions in the population.
     * @param itemList List of treasure items that can be put in the knapsack.
     * @param maxWeight Maximum weight that can be carried.
     */
    public GeneticAlgorithm(int populationSize, ItemList itemList, float maxWeight) {
        population = new Solution[populationSize];
        this.itemList = itemList;
        this.maxWeight = maxWeight;


        // create a random population
        for (int i = 0; i < populationSize; i++) {
            population[i] = new Solution(itemList.getItems().length);
        }
    }

    /**
     * Test the population on the Knapsack problem and save their fitness values.
     */
    public void testPopulation() {
        for (Solution solution : population) {
            // get the items that the solution chose
            ItemList solutionItems = itemList.getSublist(solution.getSolutionMask());

            // calculate the fitness value for the given items
            float fitness = (float) Math.pow(solutionItems.getTotalValue(maxWeight), 2);
            solution.setFitness(fitness);
        }
    }

    /**
     * Generate and save probabilities of each solution getting picked in selection using the fitness values.
     */
    public void generateProbabilities() {
        // calculate the sum of all fitness values
        float fitnessSum = 0;
        for (Solution solution : population) {
            fitnessSum += solution.getFitness();
        }

        // check if the sum is 0, in that case, there would be a division by 0
        // this check gives each solution the same probability to circumvent that
        if (fitnessSum == 0) {
            float prob = 1f / population.length;

            for (Solution solution : population) {
                solution.setProbability(prob);
            }

            return;
        }

        // divide each fitness by the sum to get the probability
        for (Solution solution : population) {
            solution.setProbability(solution.getFitness() / fitnessSum);
        }
    }

    /**
     * Select a random solution from the population based on their probabilities.
     *
     * @return Randomly selected solution.
     */
    public Solution select() {
        // random number that determines the selected solution;
        float p = random.nextFloat();

        // sum of the probabilities that increases over time
        double cumulativeProbability = 0.0;

        for (Solution solution : population) {
            // increase the cumulative probability
            cumulativeProbability += solution.getProbability();

            // check if the current solution should be picked
            if (p <= cumulativeProbability) {
                return solution;
            }
        }

        return population[population.length - 1];
    }

    /**
     * Generate a new population of children using the old population as parents. This includes both crossover and
     * mutation.
     *
     * @param eliteRate Percentage of the best individuals that are going to get passed onto the next generation without changes.
     * @param mutationProbability Probability for each bit to mutate.
     */
    public void generateChildren(float eliteRate, float mutationProbability) {
        Solution[] children = new Solution[population.length];

        // sort the population by their fitness
        // this will help with preserving the elites and make the selection faster
        Arrays.sort(population, Comparator.comparingDouble(Solution::getFitness).reversed());

        // calculate the number of elites from the rate
        int numberOfElites = Math.round(population.length * eliteRate);

        // preserve the elites (elite is a very good solution, e.g. top 1%)
        // the following code doesn't work if the population isn't sorted
        for (int i = 0; i < numberOfElites; i++) {
            children[i] = new Solution(population[i]);
        }

        // create children with selection, crossover and mutation
        for (int i = numberOfElites; i < children.length; i++) {
            // select the both parents
            Solution parent1 = select();
            Solution parent2 = select();

            // create a copy of the first parent to get a child
            Solution child = new Solution(parent1);

            // crossover the child with the second parent
            child.crossover(parent2);

            // mutate the child to inject new variation
            child.mutate(mutationProbability);

            children[i] = child;
        }

        population = children;
    }

    /**
     * Return the best solution of the population.
     *
     * @return Solution with the highest fitness.
     */
    public Solution getBestSolution() {
        Solution bestSolution = population[0];

        for (Solution solution : population) {
            if (solution.getFitness() > bestSolution.getFitness()) {
                bestSolution = solution;
            }
        }

        return bestSolution;
    }
}
