/**
 * Author: Howen Anthony Fernando
 * Date: 12/09/20
 * Class: CS310
 */


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

    private GeneticAlgorithm myGenAlg;

    public Driver(String fileName) throws IOException {

        ArrayList<String> cities = readFile(fileName);

        myGenAlg = new GeneticAlgorithm(genCityCostList(cities), generateListOfCityNames(cities));

    }


    /**
     * readFile:
     *      Reads file and adds each line as a String to an ArrayList of Strings
     * @param fileName
     * @return ArrayList of Strings (lines of File)
     * @throws IOException
     */
    private ArrayList<String> readFile(String fileName) throws IOException {

        FileInputStream fInStream = new FileInputStream(fileName);
        Scanner scan = new Scanner(fInStream);

        ArrayList<String> linesOfFile = new ArrayList<>();

        // Scans file lines in as strings and stores in ArrayList of strings
        while (scan.hasNext()) {
            linesOfFile.add(scan.nextLine());
        }

        return linesOfFile;
    }


    /**
     * generateListOfCityNames:
     *      Takes linesOfFile and splits each line at each comma
     *      Then it takes each of those split up pieces and adds them
     *      to a String[]. Then takes first element of the array and adds it to an ArrayList
     *      of strings
     * @param cities
     * @return returns the ArrayList of strings with names of cities
     */
    private ArrayList<String> generateListOfCityNames(ArrayList<String> cities) {

        ArrayList<String> cityNames = new ArrayList<>();

        for (int i = 0; i < cities.size(); i++) {
            String splitBy = ",";
            String[] line = cities.get(i).split(splitBy);

            cityNames.add(line[0]);
        }

        return cityNames;
    }


    /**
     * genCityCostList:
     *      Takes the lines of the file (cities) and splits each line by the commas.
     *      Adds the split up strings into a String[]. Then takes the array elements and
     *      adds them to an ArrayList but excludes the first element which would just be the cityNames.
     * @param cities
     * @return ArrayList<ArrayList<Integer> with each element being the List of costs for a particular city
     */
    private ArrayList genCityCostList(ArrayList<String> cities) {

        ArrayList<ArrayList<Integer>> cityList = new ArrayList<>();
        ArrayList<Integer> cityCosts;

        for (int i = 0; i < cities.size(); i++) {
            String splitBy = ",";
            String[] line = cities.get(i).split(splitBy);

            cityCosts = new ArrayList<>();

            for (int j = 1; j < line.length; j++) {
                cityCosts.add(Integer.valueOf(line[j]));
            }

            cityList.add(cityCosts);
        }

        return cityList;
    }


    public static void main(String[] args) throws IOException {
        /* This try catch block creates an instance of
         * the Driver class and passes in a command line argument as
         * parameter for the Driver class
         */
        try {
            Driver d = new Driver(args[0]);
        }
        catch(FileNotFoundException e) {
            System.out.println("use: Driver.java <source>");
        }

    }
}
