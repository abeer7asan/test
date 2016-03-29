package librecexample.Recomender.main;



public class MainExamp {
    
    public MainExamp() {
        super();
    }
    
    
    public void main(String[] args) throws Exception {

       
        // config recommender
        String configFile = "librec.conf"; 

        // run algorithm
        LibRec librec = new LibRec();
        librec.setConfigFiles(configFile);
        librec.execute(args);
    }
}
