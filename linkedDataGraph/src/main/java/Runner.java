import mestrado.linkedDataGraph.core.Job;
import mestrado.linkedDataGraph.job.LoadGeonamesDataJob;


public class Runner {

	public static void main(String[] args) {
		Job j = new LoadGeonamesDataJob();
		
		j.execute();
		
	}
	
}
