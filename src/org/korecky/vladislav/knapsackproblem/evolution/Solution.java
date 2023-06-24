package org.korecky.vladislav.knapsackproblem.evolution;

import java.util.BitSet;
import java.util.Random;


/**
 * Possible solution to the Knapsack problem that will be evolved over time using the genetic algorithm.
 */
public class Solution {
    private final BitSet solutionMask;
    private float fitness;
    private float probability;
    private static final Random random = new Random();

    /**
     * Create a random solution.
     *
     * @param length Length of the solution. Should be same as the number of items.
     */
    public Solution(int length) {
        solutionMask = new BitSet(length);

        // randomly set one bit to 1 in the BitSet
        // this initializes the BitSet in a minimalistic way (especially important for large item sizes)
        int randomIndex = random.nextInt(length);
        solutionMask.set(randomIndex);
    }

    /**
     * Create a copy of a given solution.
     *
     * @param that Solution to copy.
     */
    public Solution(Solution that) {
        this.solutionMask = (BitSet) that.solutionMask.clone();
        this.fitness = that.fitness;
        this.probability = that.probability;
    }

    /**
     * Mutate the current solution. This means randomly changing some bits based on a probability.
     *
     * @param mutationProbability Probability for each bit to mutate.
     */
    public void mutate(float mutationProbability) {
        // go through each bit
        for (int i = 0; i < solutionMask.length(); i++) {

            // check if the bit should be flipped (purely random)
            if (random.nextFloat() < mutationProbability) {
                solutionMask.flip(i);
            }
        }
    }

    /**
     * Crossover the DNA (the solution mask) of this solution with another parent. The change if inheritance is 50/50.
     *
     * @param parent Other parent to crossover with.
     */
    public void crossover(Solution parent) {
        // go through each bit
        for (int i = 0; i < solutionMask.length(); i++) {

            // check if the bit should be inherited from the parent (purely random, 50/50)
            if (random.nextFloat() < 0.5) {
                solutionMask.set(i, parent.getSolutionMask().get(i));
            }
        }
    }

    public BitSet getSolutionMask() {
        return solutionMask;
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }
}
