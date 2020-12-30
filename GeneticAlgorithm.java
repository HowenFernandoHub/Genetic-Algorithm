/**
 * Author: Howen Anthony Fernando
 * Date: 12/09/20
 * Class: CS310
 */


import java.util.*;

public class GeneticAlgorithm {

    private ArrayList<String> cityNames;
    private ArrayList<ArrayList<Integer>> cityCostList;
    private int numberOfCities;
    private int mutationProbability;
    private int averageFitness;
    private int bestFitness;
    private int totalNumMutations;
    private int mutationsInEpoch;


    public GeneticAlgorithm(ArrayList<ArrayList<Integer>> cityCostList, ArrayList<String> cityNames) {

        this.cityNames = cityNames;
        this.cityCostList = cityCostList;
        this.numberOfCities = cityNames.size();
        this.totalNumMutations = 0;

        System.out.println("Information about the file:");
        System.out.println("    Number of cities: " +  numberOfCities);
        System.out.println("    List of city names:" + cityNames);
        System.out.printf("    Distance from %s to %s = %d\n", cityNames.get(0), cityNames.get(1), cityCostList.get(0).get(1));
        System.out.printf("    Distance from %s to %s = %d\n\n", cityNames.get(0), cityNames.get(numberOfCities - 1), cityCostList.get(0).get(numberOfCities - 1));

        int populationSize = getPopulationSize();
        int numOfEpochs = getNumOfEpochs();
        this.mutationProbability = (populationSize / 10) * numOfEpochs;

        // creates the initial set of chromosomes as an ArrayList of Chromosomes (ArrayList of Integer)
        ArrayList<ArrayList<Integer>> chromosomeSet = genInitialPopulation(populationSize);
        System.out.println("\n");

        evolve(numOfEpochs, chromosomeSet);

    }


    /**
     * evolve:
     *      Takes the initial chromosomeSet and performs the genetic algorithm on it by
     *      doing the following steps
     *          1. Sorts the chromosomes into order by fitness level (fittest first)
     *          2. Mates the chromosomes with the best fitness
     *          3. Mutates (very rarely) one of the chromosomes
     *          4. Repeat
     *      Displays information about each population set
     * @param numOfEpochs
     * @param chromosomeSet
     */
    private void evolve(int numOfEpochs, ArrayList<ArrayList<Integer>> chromosomeSet) {
        int bestFitness;

        for (int i = 1; i <= numOfEpochs; i++) {

            System.out.println("Epoch " + i + ":");

            mutationsInEpoch = 0;

            chromosomeSet = sortByFitness(chromosomeSet);
            chromosomeSet = crossOver(chromosomeSet);

            bestFitness = getChromosomeFitness(chromosomeSet.get(0));
            this.bestFitness = bestFitness;

            System.out.println("    Top Fitness = " + bestFitness);
            System.out.println("    Average Fitness = " + getAverageFitness(chromosomeSet));
            System.out.println("    Number of Mutations this Epoch: " + mutationsInEpoch);
            System.out.println("    Total Number of Mutations: " + totalNumMutations);

            System.out.println("    Printing Top 5 chromosomes: ");
            for (int j = 0; j < 4; j++) {
                System.out.println("        " + (j + 1) + ". " + chromosomeSet.get(j));
            }

            System.out.println("    Printing bottom 5 chromosomes: ");
            for (int j = (chromosomeSet.size() - 6); j < chromosomeSet.size(); j++) {
                System.out.println("        " + (j + 1) + ". " + chromosomeSet.get(j));
            }

            System.out.println("\n");

        }

        System.out.println("\nWinning Chromosome: " + chromosomeSet.get(0));
        System.out.printf("    Path Length: %d\n", getChromosomeFitness(chromosomeSet.get(0)));
        System.out.println("    Sequence of cities: ");

        for (int i = 0; i < chromosomeSet.get(0).size(); i++) {
            System.out.println("        " + cityNames.get(chromosomeSet.get(0).get(i)));
        }

    }


    /**
     * getAverageFitness:
     *      This takes in the chromosome and calculates the
     *      average fitness of all the chromosomes as an int
     * @param chromosomeSet
     * @return averageFitness
     */
    private int getAverageFitness(ArrayList<ArrayList<Integer>> chromosomeSet) {
        int sumOfFitness = 0;
        int averageFitness;
        for (int j = 0; j < chromosomeSet.size(); j++) {
            sumOfFitness += getChromosomeFitness(chromosomeSet.get(j));
        }
        averageFitness = sumOfFitness / chromosomeSet.size();
        this.averageFitness = averageFitness;
        return averageFitness;
    }


    /**
     * getPopulationSize:
     *      This prompts the user for input for the population size and calls the getUserInput method which handles
     *      actual retrieval of user input. Then this method will ensure that the userInput is
     *      above 10 for the population size and returns the input.
     * @return initPopulationSize
     */
    private int getPopulationSize() {
        System.out.println("Enter number for size of starting population (recommended number = 400 | Minimum number = 10): ");
        int initPopulationSize = getUserInput();

        // this is for if the user inputs nothing and presses enter
        if (initPopulationSize == -1000000000) {
            initPopulationSize = 400;
        }
        // ensures population size at least 10
        else if (initPopulationSize < 10) {
            while (initPopulationSize < 10) {
                System.out.println("Please choose larger population size");
                System.out.println("Enter number for size of starting population (recommended number = 400 | Minimum number = 10): ");
                initPopulationSize = getUserInput();
            }
        }
        return initPopulationSize;
    }


    /**
     * getNumOfEpochs:
     *      This prompts user for input for the number of epochs and call the getUserInput method which handles
     *      actual retrieval of user input. Then this method will ensure that the userInput is
     *      above 100 for the number of epochs and will return the input
     * @return
     */
    private int getNumOfEpochs() {
        System.out.println("Enter number of epochs(default = 200 | Minimum = 100): ");
        int numOfEpochs = getUserInput();
        // this is for if the user inputs nothing and presses enter
        if (numOfEpochs == -1000000000) {
            numOfEpochs = 200;
        }
        // ensures population size at least 10
        else if (numOfEpochs < 100) {
            while (numOfEpochs < 100) {
                System.out.println("Please choose larger number of epochs");
                System.out.println("Enter number of epochs(default = 200 | Minimum = 100): ");
                numOfEpochs = getUserInput();
            }
        }
        return numOfEpochs;
    }


    /**
     * getUserInput:
     * prompts user for integer input for. If user presses enter
     * without providing an integer, it returns -1000000000.
     * @return
     */
    private int getUserInput() {
        Scanner scan = new Scanner(System.in);
        int userInt = 0;
        String input = scan.nextLine();

        try {
            userInt = Integer.parseInt(input);
        }
        catch (NumberFormatException ignored) {
        }

        if (input.equals("")) {     // executes if user pressed enter without including any integer
            userInt = -1000000000;        // if no input, set userInt to default 0
        }

        return userInt;
    }


    /**
     * genInitialPopulation:
     *      Generates an ArrayList of a random set of Chromosomes, the initialChromosomeSet,
     *      which will be the first value that the chromosomeSet is set to. It does this by
     *      calling the genRandomChromosome method.
     * @param initialPopulationSize
     * @return initChromosomeSet
     */
    private ArrayList<ArrayList<Integer>> genInitialPopulation(int initialPopulationSize) {

        ArrayList<ArrayList<Integer>> initChromosomeSet = new ArrayList<>();

        for (int i = 0; i < initialPopulationSize; i++) {
            initChromosomeSet.add(genRandomChromosome());
        }

        return initChromosomeSet;
    }


    /**
     * genRandomChromosome:
     *      This will generate a random chromosome as an ArrayList of Integers.
     *      The random chromosome will contain integers from 0 to the populationSize - 1.
     *      It will also not contain any repeated integers. To ensure this it will begin with
     *      a sorted set of integers and then mixes it up by randomly swapping elements.
     * @return - randChromosome
     */
    private ArrayList<Integer> genRandomChromosome() {

        ArrayList<Integer> randChromosome = new ArrayList<>();

        for (int i = 0; i < numberOfCities; i++) {
            randChromosome.add(i);
        }

        for (int i = numberOfCities - 1; i > 0; i--) {
            Random rand = new Random();

            int positionToSwap = rand.nextInt(i + 1);       // bound is i +1 so that it will include ith element
            int temp = randChromosome.get(i);       // temp holds the element at pos. i

            randChromosome.set(i, randChromosome.get(positionToSwap));
            randChromosome.set(positionToSwap, temp);
        }

        return randChromosome;

    }


    /**
     * sortByFitness:
     *      This takes the chromosomeSet and sorts it based on fitness
     *      which is calculated by a separate method.
     * @param chromosomeSet
     * @return sortedChromosomes
     */
    private ArrayList<ArrayList<Integer>> sortByFitness(ArrayList<ArrayList<Integer>> chromosomeSet) {

        int[] fitnessArray = new int[chromosomeSet.size()];     // this will hold the fitness of all the chromosomes

        for (int i = 0; i < chromosomeSet.size(); i++) {
            fitnessArray[i] = getChromosomeFitness(chromosomeSet.get(i));
        }

        Arrays.sort(fitnessArray);      // sort the fitness array from most fit to least fit

        ArrayList<ArrayList<Integer>> sortedChromosomes = new ArrayList<>();

        // this for loop goes through each element of the array
        for (int i = 0; i < fitnessArray.length; i++) {

            /* this goes through each element of the chromosomeSet and sees if its fitness
             * matches the fitnessArray[i]. If it does, it adds that chromosome into the sortedChromosome
             * ArrayList at the end and then removes that chromosome from the original chromosome set
             * to prevent it from being re-added in the case of duplicate fitness.
             */
            for (int j = 0; j < chromosomeSet.size(); j++) {

                if ((getChromosomeFitness(chromosomeSet.get(j)) == fitnessArray[i])) {
                    sortedChromosomes.add(chromosomeSet.get(j));
                    chromosomeSet.remove(j);        // removes from chromosomeSet so that it won't be re-added
                    break;
                }
            }
        }
        return sortedChromosomes;
    }


    /**
     * getChromosomeFitness:
     *      Calculates the length of the path represented by the chrosome
     * @param chromosome
     * @return fitness
     */
    private int getChromosomeFitness(ArrayList<Integer> chromosome) {
        int fitness = 0;
        int currentCity;
        int nextCity;

        for (int i = 0; i < chromosome.size() - 1; i++) {
            currentCity = chromosome.get(i);
            nextCity = chromosome.get(i + 1);
            // getsTheCostTo go from currCity to nextCity and adds to fitness
            fitness += cityCostList.get(currentCity).get(nextCity);
        }

        return fitness;
    }


    /**
     * breedChromosomes:
     *      Takes 2 parents and attempts to combine them in a random matter
     *      Does this by initially making the child a copy of parent2 and then swaps
     *      a random set of half of the elements with the elements of parent1
     *      all while ensuring that the chromosome doesn't contain repeat integers.
     *      Then returns child
     * @param parent1
     * @param parent2
     * @return child chromosome
     */
    private ArrayList<Integer> breedChromosomes(ArrayList<Integer> parent1, ArrayList<Integer> parent2) {
        HashSet<Integer> alreadySwitched = new HashSet<>();     // keeps track of which positions we have already switched
        ArrayList<Integer> child = new ArrayList<>(parent2);    // child is initially a copy of parent2

        Random rand = new Random();

        int numberOfSwitches = parent1.size() / 2;      // switches half of the elements

        for (int i = 0; i < numberOfSwitches; i++) {
            boolean switched = false;

            /* This loops until switch actually occurs
             * If it loops 1,000 times, then it will automatically
             * simply return the child as it is now the exact same as parent 1
             */
            int counter = 0;
            while(!switched) {

                if (counter >= 1000) {
                    return child;       // returns the child as it is if it can't find non-matching elements to switch
                }

                int toSwitch = rand.nextInt(parent1.size());        // rolls random pos. to switch from 0 - parentSize - 1

                /* if this position hasn't already been switched
                 * AND the element at the position is not the same
                 * in both parent1 and child, THEN switch
                 */
                if (!alreadySwitched.contains(toSwitch) && (!parent1.get(toSwitch).equals(child.get(toSwitch)))) {
                    alreadySwitched.add(toSwitch);

                    int temp = child.get(toSwitch);             // maintains the element of child that we will switch

                    // loops until it find the element which matches the element it wants to switch in
                    // then it sets that element to temp
                    for (int j = 0; j <  child.size(); j++) {
                        if (child.get(j).equals(parent1.get(toSwitch))) {
                            child.set(j, temp);
                            break;
                        }
                    }

                    child.set(toSwitch, parent1.get(toSwitch));     // replaces child's element at toSwitch with
                                                                    // the toSwitchth element in parent1

                    switched = true;        // sets switched to true so it will exit the loop
                }
                counter++;
            }

        }

        // if condition met, this will mutate the chromosome by calling mutateChromosome
        if (rand.nextInt(mutationProbability) == 0) {
            totalNumMutations++;
            mutationsInEpoch++;
            return mutateChromosome(child);
        }

        return child;
    }


    /**
     * mutateChromosome:
     *      This chooses to random elements to swap places on the chromosome
     * @param chromosomeToMutate
     * @return mutated chromosome
     */
    private ArrayList<Integer> mutateChromosome(ArrayList<Integer> chromosomeToMutate) {
        Random rand = new Random();
        int swap1 = rand.nextInt(chromosomeToMutate.size());
        int swap2 = rand.nextInt(chromosomeToMutate.size());

        int temp = chromosomeToMutate.get(swap1);
        chromosomeToMutate.set(swap1, chromosomeToMutate.get(swap2));
        chromosomeToMutate.set(swap2, temp);
        return chromosomeToMutate;
    }


    /**
     * crossOver:
     *      This method takes the set of parent chromosomes and decides which chromosomes to partner up
     *      The top 10% chromosomes get to clone themselves into the next generation
     *      Bottom 10% of next generation are offspring of top and bottom 50%
     *      Middle 80% of next generation will be randomized even further
     * @param parents
     * @return offSpring (new set of Chromosomes)
     */
    private ArrayList<ArrayList<Integer>> crossOver(ArrayList<ArrayList<Integer>> parents) {

        ArrayList<ArrayList<Integer>> offSpring = new ArrayList<>();

        Random rand = new Random();

        for(int i = 0; i < parents.size(); i++) {

            // Top 10% best chromosome gets passed unchanged
            if ((parents.size() / (double)(i + 1)) >= 10.0) {
                offSpring.add(parents.get(i));
            }

            // Bottom 10% chromosomes
            else if ((parents.size() / (double)(i + 1)) <= 1.11) {
                ArrayList<Integer> parent1 = parents.get((parents.size() / 2) + rand.nextInt(parents.size() / 2));      // selects from bottom 50%
                ArrayList<Integer> parent2 = parents.get(rand.nextInt(parents.size() / 2));     // selects from top 50%

                // Loops if parents are equal until it chooses 2 parents that are different
                int counter = 0;
                while (parent1.equals(parent2)) {
                    parent1 = parents.get((parents.size() / 2) + rand.nextInt(parents.size() / 2));      // selects from bottom 50%
                    parent2 = parents.get(rand.nextInt(parents.size() / 2));     // selects from top 50%
                    if (counter >= 100) {
                        System.out.println("had to break!\nparents were the SAME");
                        break;
                    }
                    counter++;
                }

                // 1 in 3 chance of swapping positions of parent1 and 2
                if (rand.nextInt(3) == 2) {
                    offSpring.add(breedChromosomes(parent2, parent1));
                }
                else {
                    offSpring.add(breedChromosomes(parent1, parent2));
                }
            }

            // This takes care middle 80% of chromosomes
            else {
                // initially set both parents to the same chromosome so that it will enter the while loop
                ArrayList<Integer> parent1 = parents.get(0);
                ArrayList<Integer> parent2 = parents.get(0);

                // if parents are equal, then keep looking for a different pair
                int counter = 0;
                while (parent1.equals(parent2)) {

                    parent1 = parents.get(rand.nextInt(parents.size() / 4));      // randomly selects one of the top 25%
                    parent2 = parents.get(rand.nextInt(parents.size()));     // randomly selects from all chromosomes

                    if (rand.nextInt(10) == 5) {
                        parent1 = parents.get(rand.nextInt(parents.size()));        // randomly selects from all chromosomes
                        parent2 = parents.get(rand.nextInt(parents.size()));        // randomly selects from all chromosomes
                    }

                    else if (rand.nextInt(10) > 5) {
                        parent1 = parents.get((parents.size() / 4) + rand.nextInt(parents.size() / 2));     // selects from 25% - 75%
                        parent2 = parents.get(rand.nextInt(parents.size() / 4));        // selects from top 25%
                    }

                    if (counter >= 100) {
                        break;
                    }
                    counter++;
                }

                // 1 in 3 chance of swapping positions of parent 1 and 2
                if (rand.nextInt(3) == 2) {
                    offSpring.add(breedChromosomes(parent2, parent1));
                }
                else {
                    offSpring.add(breedChromosomes(parent1, parent2));
                }
            }
        }

        return offSpring;
    }





}
