import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

import mestrado.linkedDataGraph.jms.EntityPair;
import mestrado.linkedDataGraph.jms.GsonBuilder;


public class Results {

	public static void main(String[] args) throws JMSException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader("/media/My Passport/linkedData/identityQueue_final"));
		
		String line;
		Map<String, EntityPair> map = new HashMap<String, EntityPair>(); 
		while((line = reader.readLine()) != null) {
			EntityPair e = GsonBuilder.getGson().fromJson(line, EntityPair.class);

			if(!map.containsKey(e.getGeonamesId())) {
				map.put(e.getGeonamesId(), e);
			} else {
				System.out.println(map.get(e.getGeonamesId()));
				System.out.println(e);
			}
		
		}
		
		reader.close();
		
		
	}
	
}
