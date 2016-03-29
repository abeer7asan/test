package algorithms.clustering.test;

import algorithms.clustering.clustering.kmeans.AlgoKMeans;
import algorithms.clustering.distanceFunctions.DistanceCorrelation;
import algorithms.clustering.distanceFunctions.DistanceCosine;
import algorithms.clustering.distanceFunctions.DistanceEuclidian;
import algorithms.clustering.distanceFunctions.DistanceFunction;
import algorithms.clustering.distanceFunctions.DistanceJaccard;
import algorithms.clustering.distanceFunctions.DistanceManathan;

import algorithms.clustering.kmeans.AlgoKMeansDB;

import algorithms.clustering.services.Database;
import algorithms.clustering.services.MyDBServices;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URL;


/**
 *  Example of how to use the KMEans algorithm, in source code.
 */
public class KmeanTest {


    public static void main(String[] args) throws NumberFormatException, IOException {
        int k = 4; //number of clusters
        String output =
            "C:\\Users\\abeer\\Desktop\\desktop\\University\\performance\\Recomender\\clustering\\src\\algorithms\\clustering\\test\\output.txt";
        
        // Apply the algorithm to read from text file *****
        String input =
            "C:\\Users\\abeer\\Desktop\\desktop\\University\\performance\\Recomender\\clustering\\src\\algorithms\\clustering\\test\\configKmeans2.txt";
         // we request 3 clusters
       

        // Here we specify that we want to use the euclidian distance
        // DistanceFunction distanceFunction = new DistanceEuclidian();
        // Alternative distance functions are also available such as:
        // DistanceFunction distanceFunction = new DistanceManathan();
        // DistanceFunction distanceFunction = new DistanceCosine();
        //    DistanceFunction distanceFunction = new DistanceCorrelation();
        // DistanceFunction distanceFunction = new DistanceJaccard();

       

        for (int x=0;x<10 ; x++){
                    DistanceFunction distanceFunction = new DistanceCosine();
                      //DistanceFunction distanceFunction = new DistanceEuclidian();
                    //DistanceFunction distanceFunction = new DistanceCorrelation();
                    //DistanceFunction distanceFunction = new DistanceJaccard();
            //DistanceFunction distanceFunction = new DistanceManathan();
                    AlgoKMeans algoKMeans = new AlgoKMeans();
                    algoKMeans.runAlgorithm(input, k, distanceFunction);
                    algoKMeans.printStatistics();
                    algoKMeans.saveToFile(output);
                  }

        //by Abeer Mousa to read from database
        /*Database db = new Database();
        MyDBServices myDB = new MyDBServices();

        for (int x = 0; x < 1; x++) {
            //DistanceFunction distanceFunction = new DistanceCosine();
            //DistanceFunction distanceFunction = new DistanceEuclidian();
            DistanceFunction distanceFunction = new DistanceCorrelation();
            AlgoKMeansDB algoKMeans = new AlgoKMeansDB();
            algoKMeans.runAlgorithm(myDB.getMarksForTest(db.con, "1", 10), k, distanceFunction);
            algoKMeans.printStatistics();
            algoKMeans.saveToDB();
        }*/
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = KmeanTest.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }


}
