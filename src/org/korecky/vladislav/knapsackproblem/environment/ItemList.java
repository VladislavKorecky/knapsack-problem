package org.korecky.vladislav.knapsackproblem.environment;

import java.util.BitSet;
import java.util.Random;


/**
 * Special collection of treasure items with helper functions.
 */
public class ItemList {
    private final Item[] items;
    private static final Random random = new Random();

    public ItemList(int itemCount) {
        items = new Item[itemCount];
    }

    /**
     * Fill the item list with random items. The items have a max size of 40kg and max value of 2_000_000 dollars.
     */
    public void initialize() {
        for (int i = 0; i < items.length; i++) {
            float weight = random.nextFloat(40);
            float value = random.nextFloat(2000000);

            items[i] = new Item(weight, value);
        }
    }

    /**
     * Fill the item list with items of same weight that add up to the max weight. The items have a random value up
     * to 2_000_000 dollars.
     */
    public void initialize(float maxWeight) {
        float itemWeight = maxWeight / items.length;

        for (int i = 0; i < items.length; i++) {
            float value = random.nextFloat(2000000);

            items[i] = new Item(itemWeight, value);
        }
    }

    /**
     * Return the total value of all items. Return 0 if the weight of the items exceeds a threshold.
     *
     * @param maxWeight Maximum weight of all items.
     * @return The value of all items.
     */
    public float getTotalValue(float maxWeight) {
        float totalValue = 0;
        float totalWeight = 0;

        // count up the total value and total weight
        for (Item item : items) {
            totalValue += item.value();
            totalWeight += item.weight();
        }

        // check that the weight is in bounds
        if (totalWeight > maxWeight) {
            return 0;
        }

        return totalValue;
    }

    /**
     * Return the total value of all items. Ignore the weight.
     *
     * @return The value of all items.
     */
    public float getTotalValue() {
        float totalValue = 0;

        // count up the total value
        for (Item item : items) {
            totalValue += item.value();
        }

        return totalValue;
    }

    /**
     * Create a sublist of items by using a mask. Each bit in the mask corresponds to one item. The bit's value dictates
     * if the item is or is not included in the sublist.
     *
     * @param mask Mask that determines which items get included in the sublist.
     * @return The created sublist.
     */
    public ItemList getSublist(BitSet mask) {
        // create the sublist
        int sublistLength = mask.cardinality();
        ItemList sublist = new ItemList(sublistLength);

        // last index with a non-null value in the sublist
        int lastSublistIndex = 0;

        // go through each item and its corresponding mask bit
        for (int i = 0; i < items.length; i++) {

            // check the bit to see if the item should be included
            if (mask.get(i)) {

                // put the item at the last place in the sublist
                sublist.items[lastSublistIndex] = items[i];

                // update the sublist index
                lastSublistIndex++;
            }
        }

        return sublist;
    }

    public Item[] getItems() {
        return items;
    }
}
