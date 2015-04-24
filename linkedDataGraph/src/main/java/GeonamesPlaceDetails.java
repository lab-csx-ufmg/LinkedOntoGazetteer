import java.io.BufferedReader;
import java.io.FileReader;

import mestrado.linkedDataGraph.jms.GeonamesDetails;
import mestrado.linkedDataGraph.jms.JMSProducer;


public class GeonamesPlaceDetails {

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(args[0]));
		String line;
		JMSProducer<GeonamesDetails> producer = new JMSProducer<GeonamesDetails>("geonamesDetails");
		while((line = reader.readLine()) != null) {
			String[] decodedLine = line.split("\t");
			try{
				GeonamesDetails geonamesDetails = new GeonamesDetails(decodedLine[0], decodedLine[6].charAt(0), decodedLine[7]);
				producer.send(geonamesDetails);
			} catch(Exception e) {
				System.out.println("Erro ao processar: " + decodedLine[0] + " - " + e.getMessage());
			}
		}
	
		producer.close();
		reader.close();
	}
	
	
}
