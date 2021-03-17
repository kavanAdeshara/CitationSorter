import java.io.*;
import java.util.Comparator;
import java.util.Scanner;

public class Citation {

    /**
     * Attributes to be considered for citation
     *          Identifier(id) -- every citation contains it
     *          Year -- some citations might not contain it (if not, set year to 2021 by default)
     *          Rest -- contains all the information of the citation in a string format
     */

    private int year;
    private String id;
    private String rest;


    /**
     * Comparator for sorting citations based identifiers
     */
    // Implementing comparator to sort citations by identifiers (strings)
    private static class IdOrder implements Comparator<Citation> {
        public int compare(Citation a, Citation b) {
            return a.id.compareTo(b.id);
        }
    }

    // Getting the IdOrder Comparator
    private static Comparator<Citation> byIdOrder() {
        return new IdOrder();
    }

    /**
     * Comparator for sorting citations by year
     */
    // Implementing comparator to sort citations by year (int)
    private static class YearOrder implements Comparator<Citation> {
        public int compare(Citation a, Citation b) {
            return a.year - b.year;
        }
    }

    // Getting the YearOrder Comparator
    private static Comparator<Citation> byYearOrder() {
        return new YearOrder();
    }

    /**
     * Main Function where user bib file path will be read and sorted based on year or identifier
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {

        boolean cond1 = true;
        boolean cond2 = false;
        boolean cond3 = false;
        boolean cond4 = false;

        PrintStream console = System.out;
        PrintStream fileOut = new PrintStream("./SortedCitations.txt");

        // Array of citation objects, representing each citation in the bib text file
        Citation[] citations;

        // Reading the user input for bib text file path
        Scanner scan = new Scanner(System.in);
        System.out.print("Provide the file path: ");
        String filePath = scan.next();

        // Asking the user for sorting order
        System.out.print("Do you want to sort by identifier, then year (true/false): ");
        cond1 = scan.nextBoolean();
        if(!cond1){
            System.out.print("Do you want to sort by year, then identifier: ");
            cond2 = scan.nextBoolean();
            if(!cond2){
                System.out.print("Do you want to sort by identifier only: ");
                cond3 = scan.nextBoolean();
                if(!cond3){
                    System.out.print("Do you want to sort by year only: ");
                    cond4 = scan.nextBoolean();
                }
            }
        }

        System.setOut(fileOut);


        // Acquiring the actual file from the provided file path
        File file = new File(filePath);
        Scanner sc = new Scanner(new FileReader(file));

        // String container variable to read everything from the text as one line of string
        String fileContents = null;

        // Storing every line from text file into the container string
        while(sc.hasNextLine()){
            fileContents += sc.nextLine();
        }

        // Splitting the string from the regular expression @, storing each citation line an array
        String[] strCitations = fileContents.split("@");
        citations = new Citation[strCitations.length - 1];

        // Iterating through the array created from splitting the fileContents string
        for(int i = 1; i < strCitations.length; i++) {

            // Create new citation at every iteration, representing a new citation object
            Citation ct = new Citation();

            // Get the id, year, and the remaining contents of the citation and save it in the citation object
            ct.id = extractId(strCitations[i]);
            ct.year = extractYear(strCitations[i]);
            ct.rest = strCitations[i];

            // Store each individual citation object in the citation array
            citations[i - 1] = ct;
        }

        if(cond1){
            sortByID(citations);
            sortByYear(citations);
        }
        else if(cond2){
            sortByYear(citations);
            sortByID(citations);
        }
        else if(cond3){
            sortByID(citations);

        }
        else if(cond4){
            sortByYear(citations);
        }
        else{
            System.out.println("Based on your input you do not want to sort the citation");
        }


        System.setOut(console);

    }

    /**
     * Helper method to extract identifier from a citation string line
     * @param str
     * @return
     */
    private static String extractId(String str){
        int startIndex = str.indexOf("{") + 1;
        int endIndex = str.indexOf(",");
        return str.substring(startIndex, endIndex);
    }

    /**
     * Helper method to extract year from a citation string line
     * @param str
     * @return
     */
    private static int extractYear(String str){
        int startIndex = str.indexOf("year");
        int endIndex = str.indexOf("year") + 10;
        if(startIndex != -1){
            return Integer.valueOf(str.substring(startIndex + 6, endIndex));
        }
        else {
            return 2021;
        }
    }

    /**
     * Implementing Insertion Sort for stability
     * @param citations
     * @param comparator
     */
    private static void insertionSort(Citation[] citations, Comparator comparator){
        int N = citations.length;
        for(int i = 0; i < citations.length; i++){
            for(int j = i; j > 0 && less(comparator, citations[j], citations[j -1]); j--){
                exch(citations, j, j-1);
            }
        }
    }

    /**
     * Helper method for comparing the two citation objects for inequality
     * @param comparator
     * @param v
     * @param w
     * @return
     */
    private static boolean less(Comparator comparator, Citation v, Citation w){
        return comparator.compare(v, w) < 0;
    }

    /**
     * Helper method to exchange position of two citations objects in an array
     * @param citations
     * @param i
     * @param j
     */
    private static void exch(Citation[] citations, int i, int j){
        Citation swap = citations[i];
        citations[i] = citations[j];
        citations[j] = swap;
    }

    /**
     * Helper method sort the citations by ID
     * @param citations
     */
    private static void sortByID(Citation[] citations){

        System.out.println(" ");
        System.out.println("Sorting by Identifier: ------");
        System.out.println(" ");

        insertionSort(citations, Citation.byIdOrder());

        for(int i = 0; i < citations.length; i++){
            System.out.print("@");
            System.out.println(citations[i].rest.replaceAll(",  ", "\n\t"));
            System.out.println('\n');
        }
    }

    /**
     * Helper method to sort the citations by Year
     * @param citations
     */
    private static void sortByYear(Citation[] citations){
        System.out.println(" ");
        System.out.println("Sorting by Year: -------");
        System.out.println(" ");

        insertionSort(citations, Citation.byYearOrder());

        for(int i = 0; i < citations.length; i++){
            System.out.print("@");
            System.out.println(citations[i].rest.replaceAll(",  ", "\n\t"));
            System.out.println('\n');
        }
    }

}
