import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


public class DBPediaNotGeonamesType {

	public static void main(String[] args) throws IOException {

		String file1 = "/media/My Passport/linkedData/dbpedia/instance_types_en.nt";
		String file2= "/media/My Passport/linkedData/dbpedia_notgeonames_places";
		
		BufferedReader reader = new BufferedReader(new FileReader(file2));
		SortedSet<String> dbpediaplaces = new TreeSet<String>();
		String line;
		while((line = reader.readLine()) != null) {
			dbpediaplaces.add(line);
		}
		reader.close();
		
		System.out.println("Prefetch done");
		
		reader = new BufferedReader(new FileReader(file1));
		Map<String, Integer> counter = new  HashMap<String, Integer>();
		while((line = reader.readLine()) != null) {
			String[] parsedLine = line.split(" ");
			String id = parsedLine[0];
			id = id.replace("<http://dbpedia.org/resource/", "");
			id = id.substring(0, id.length()-1);

			if(dbpediaplaces.contains(id)) {
				if(!counter.containsKey(parsedLine[2])) {
					counter.put(parsedLine[2], 0);
				}
				counter.put(parsedLine[2], counter.get(parsedLine[2])+1);
			}
		}
		
		for(String key : counter.keySet()) {
			System.out.println(key + "\t" +counter.get(key));
		}
	}

}
