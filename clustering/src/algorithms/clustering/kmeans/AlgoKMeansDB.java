package algorithms.clustering.kmeans;

import algorithms.clustering.clustering.kmeans.AlgoKMeans;
import algorithms.clustering.distanceFunctions.DistanceFunction;
import algorithms.clustering.patterns.cluster.ClusterWithMean;
import algorithms.clustering.patterns.cluster.ClustersEvaluation;
import algorithms.clustering.patterns.cluster.DoubleArray;
import algorithms.clustering.services.Database;
import algorithms.clustering.services.MyDBServices;
import algorithms.clustering.tools.MemoryLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgoKMeansDB {
    /**
     * Default constructor
     */
    
    public AlgoKMeansDB() {
        super();
    }
    
    // The list of clusters generated
    protected List<ClusterWithMean> clusters = null;
    
    // A random number generator because K-Means is a randomized algorithm
    protected final static Random random = new Random(System.currentTimeMillis());
    protected MyDBServices myDBsrvice = new MyDBServices();
    protected Database db = new Database();
    // For statistics
    protected long startTimestamp; // the start time of the latest execution
    protected long endTimestamp;  // the end time of the latest execution
    long iterationCount; // the number of iterations that was performed
    
    /* The distance function to be used for clustering */
    protected DistanceFunction distanceFunction = null;

 
    
    /**
     * Run the K-Means algorithm
     * @param inputFile an input file path containing a list of vectors of double values
     * @param k the parameter k
     * @param distanceFunction 
     * @return a list of clusters (some of them may be empty)
     * @throws IOException exception if an error while writing the file occurs
     */
    public List<ClusterWithMean> runAlgorithm(List<DoubleArray> vectors, int k, DistanceFunction distanceFunction) throws NumberFormatException, IOException {
            // record the start time
            startTimestamp =  System.currentTimeMillis();
            // reset the number of iterations
            iterationCount =0;
            
            this.distanceFunction = distanceFunction;
            
      
            // variables to store the minimum and maximum values in vectors
            double minValue = Integer.MAX_VALUE;
            double maxValue = 0;
            
  
     
            
            // Get the size of vectors
            int vectorsSize = vectors.get(0).data.length;
            
            // if the user ask for only one cluster!
            if(k == 1) {
                    // Create a single cluster and return it 
                    clusters = new ArrayList<ClusterWithMean>();
                    ClusterWithMean cluster = new ClusterWithMean(vectorsSize);
                    for(DoubleArray vector : vectors) {
                            cluster.addVector(vector);
                    }
                    cluster.setMean(new DoubleArray(new double[vectorsSize]));
                    cluster.recomputeClusterMean();
                    clusters.add(cluster);
                    
                    // check memory usage
                    MemoryLogger.getInstance().checkMemory();
                    
                    // record end time
                    endTimestamp =  System.currentTimeMillis();
                    return clusters;
            }
            
            // SPECIAL CASE: If only one vector
            if (vectors.size() == 1) {
                    // Create a single cluster and return it 
                    clusters = new ArrayList<ClusterWithMean>();
                    DoubleArray vector = vectors.get(0);
                    ClusterWithMean cluster = new ClusterWithMean(vectorsSize);
                    cluster.addVector(vector);
                    cluster.recomputeClusterMean();
                    cluster.setMean(new DoubleArray(new double[vectorsSize]));
                    clusters.add(cluster);
                    
                    // check memory usage
                    MemoryLogger.getInstance().checkMemory();
                    
                    // record end time
                    endTimestamp =  System.currentTimeMillis();
                    return clusters;
            }
            
            // if the user asks for more cluster then there is data,
            // we set k to the number of data points.
            if(k > vectors.size()) {
                    k = vectors.size();
            }

            applyAlgorithm(k, distanceFunction, vectors, minValue, maxValue,
                            vectorsSize); 

            // check memory usage
            MemoryLogger.getInstance().checkMemory();
            
            // record end time
            endTimestamp =  System.currentTimeMillis();
            
            // return the clusters
            return clusters;
    }

    /**
     * Apply the K-means algorithm
     * @param k the parameter k
     * @param distanceFunction a distance function
     * @param vectors the list of initial vectors
     * @param minValue the min value
     * @param maxValue the max value
     * @param vectorsSize  the vector size
     */
    void applyAlgorithm(int k, DistanceFunction distanceFunction,
                    List<DoubleArray> vectors, double minValue, double maxValue,
                    int vectorsSize) {
            // apply kmeans
            clusters = applyKMeans(k, distanceFunction, vectors, minValue, maxValue, vectorsSize);
    }
    
    /**
     * Apply the K-means algorithm
     * @param k the parameter k
     * @param distanceFunction a distance function
     * @param vectors the list of initial vectors
     * @param minValue the min value
     * @param maxValue the max value
     * @param vectorsSize  the vector size
     */
    List<ClusterWithMean> applyKMeans(int k, DistanceFunction distanceFunction,
                    List<DoubleArray> vectors, double minValue, double maxValue,
                    int vectorsSize) {
            List<ClusterWithMean> newClusters = new ArrayList<ClusterWithMean>();
            
            // SPECIAL CASE: If only one vector
            if (vectors.size() == 1) {
                    // Create a single cluster and return it 
                    DoubleArray vector = vectors.get(0);
                    ClusterWithMean cluster = new ClusterWithMean(vectorsSize);
                    cluster.addVector(vector);
                    newClusters.add(cluster);
                    return newClusters;
            }
            
            // (1) Randomly generate k empty clusters with a random mean (cluster
            // center)
            for(int i=0; i< k; i++){
                    DoubleArray meanVector = generateRandomVector(minValue, maxValue, vectorsSize);
                    ClusterWithMean cluster = new ClusterWithMean(vectorsSize);
                    cluster.setMean(meanVector);
                    newClusters.add(cluster);
            }

            // (2) Repeat the two next steps until the assignment hasn't changed
            boolean changed;
            while(true) {
                    iterationCount++;
                    changed = false;
                    // (2.1) Assign each point to the nearest cluster center.

                    // / for each vector
                    for (DoubleArray vector : vectors) {
                            // find the nearest cluster and the cluster containing the item
                            ClusterWithMean nearestCluster = null;
                            ClusterWithMean containingCluster = null;
                            double distanceToNearestCluster = Double.MAX_VALUE;

                            // for each cluster
                            for (ClusterWithMean cluster : newClusters) {
                                    // calculate the distance of the cluster mean to the vector
                                    double distance = distanceFunction.calculateDistance(cluster.getmean(), vector);
                                    // if it is the smallest distance until now, record this cluster
                                    // and the distance
                                    if (distance < distanceToNearestCluster) {
                                            nearestCluster = cluster;
                                            distanceToNearestCluster = distance;
                                    }
                                    // if the cluster contain the vector already,
                                    // remember that too!
                                    if (cluster.contains(vector)) {
                                            containingCluster = cluster;
                                    }
                            }

                            // if the nearest cluster is not the cluster containing
                            // the vector
                            if (containingCluster != nearestCluster) {
                                    // remove the vector from the containing cluster
                                    if (containingCluster != null) {
                                            containingCluster.remove(vector);
                                    }
                                    // add the vector to the nearest cluster
                                    nearestCluster.addVector(vector);
                                    changed = true;
                            }
                    }

                    // check the memory usage
                    MemoryLogger.getInstance().checkMemory();
                    
                    if(!changed){     // exit condition for main loop
                            break;
                    }
                    
                    // (2.2) Recompute the new cluster means
                    for (ClusterWithMean cluster : newClusters) {
                            cluster.recomputeClusterMean();
                    }
            }
            
            return newClusters;
    }

    /**
     * Generate a random vector.
     * @param minValue  the minimum value allowed
     * @param maxValue  the maximum value allowed
     * @param vectorsSize the desired vector size
     * @return the random vector
     */
    DoubleArray generateRandomVector(double minValue, double maxValue,
                    int vectorsSize) {
            // create a new vector
            double[] vector = new double[vectorsSize];
            // for each position generate a random number
            for(int i=0; i < vectorsSize; i++){
                    vector[i] = (random.nextDouble() * (maxValue - minValue)) + minValue;
            }
            // return the vector
            return new DoubleArray(vector);
    }


    /**
     * Save the clusters to an output file
     * @param output the output file path
     * @throws IOException exception if there is some writing error.
     */
    public void saveToDB() throws IOException {
        //Stoped by Abeer Mousa 
            /*BufferedWriter writer = new BufferedWriter(new FileWriter(output));
            // for each cluster
            for(int i=0; i< clusters.size(); i++){
                    // if the cluster is not empty
                    if(clusters.get(i).getVectors().size() >= 1){
                            // write the cluster
                            writer.write(clusters.get(i).toString());
                            // if not the last cluster, add a line return
                            if(i < clusters.size()-1){
                                    writer.newLine();
                            }
                    }
            }
            // close the file
            writer.close();*/
        
        //Writen by Abeer Mousa to DB
        // for each cluster
        
        for(int i=0; i< clusters.size(); i++){
                // if the cluster is not empty
                if(clusters.get(i).getVectors().size() >= 1){
                        // write the cluster
                    for(int j=0; j< clusters.get(i).getVectors().size(); j++){
                        //put cluster ID ---> i
                        //put student ID ---> clusters.get(i).getVectors().get(j).studentID
                        //put reff student  ----> dummy 10
                        
                        myDBsrvice.writeClustersToDB(db.con,i+"",clusters.get(i).getVectors().get(j).studentID+"","10");
                        
                    }
                      
                        }
                }
    }
    
    /**
     * Print statistics of the latest execution to System.out.
     */
    public void printStatistics() {
            System.out.println("========== Notes  ============");
            System.out.println(" Distance function: " + distanceFunction.getName());
            System.out.println(" time in ms ~: " + (endTimestamp - startTimestamp)
                            + "");
            System.out.println(" Sum of Squared Errors : " + ClustersEvaluation.calculateSSE(clusters, distanceFunction));
            //System.out.println(" Max memory:" + MemoryLogger.getInstance().getMaxMemory() + " mb ");
            //System.out.println(" Iteration count: " + iterationCount);
            System.out.println("=====================================");
    }

}
