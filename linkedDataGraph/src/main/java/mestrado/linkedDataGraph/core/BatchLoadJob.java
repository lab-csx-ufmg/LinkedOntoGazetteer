package mestrado.linkedDataGraph.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class BatchLoadJob extends Job {

	protected BufferedReader reader;
	
	private double count = 0;
	private double startTime;
	private double endTime;
	private double lastTime;
	
	private int linesPerCommit = 1000;
	
	public BatchLoadJob(String fileNameKey) {
		super();
		try {
			this.reader = new BufferedReader(new FileReader(System.getProperty(fileNameKey)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to instantiate the BatchLoadJob. " + this.getClass(), e);
		}
	}
	
	public void setLinesPerCommit(int limit) {
		this.linesPerCommit = limit;
	}
	
	public String readLine() throws IOException {
		return this.reader.readLine();
	}
	
	@Override
	public void execute() {
    	this.count = 0;
    	this.startTime = System.currentTimeMillis();
    	this.lastTime = this.startTime;

    	String line;
    	
    	try {
			while((line = this.reader.readLine()) != null) {
				if(!this.processLine(line)) {
					continue;
				}
				
	    		if(this.count++ % this.linesPerCommit == 0) {
	    			this.graph.getBaseGraph().commit();

	    			this.endTime = System.currentTimeMillis();
	    			System.out.print(" - Lap time: " + (this.endTime - this.lastTime)/1000.0 + " s");
	    			System.out.println(" - Total time: " + (this.endTime - this.startTime)/1000.0 + " s");
	    			this.lastTime = this.endTime;
	    		}
				
			}
			
			this.graph.getBaseGraph().commit();
			this.graph.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		
	}

	public abstract boolean processLine(String line);
	
}
