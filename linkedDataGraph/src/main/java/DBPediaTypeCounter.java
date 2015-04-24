import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class DBPediaTypeCounter {

	public static void main(String[] args) throws IOException {

		String file1 = "/media/My Passport/linkedData/dbpedia/instance_types_en.nt";
		System.out.println("Started!");
		BufferedReader reader = new BufferedReader(new FileReader(file1));
		Map<String, Integer> counter = new  HashMap<String, Integer>();
		String line;
		while((line = reader.readLine()) != null) {
			String[] parsedLine = line.split(" ");
			if(!counter.containsKey(parsedLine[2])) {
				counter.put(parsedLine[2], 0);
			}
			counter.put(parsedLine[2], counter.get(parsedLine[2])+1);
		}
		
		for(String key : counter.keySet()) {
			System.out.println(key + "\t" +counter.get(key));
		}
		
		reader.close();
	}
	
}
