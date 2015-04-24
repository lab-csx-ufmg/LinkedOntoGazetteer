package mestrado.linkedDataGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		String names = "Aalst,Aalst,Oost-Vlaanderen,Oost-Vlaanderen,Vlaanderen,Vlaanderen,Belgique,Belgique,Europe";
		String[] split = names.split("[,;]");
		
		if(split.length == 0) {
			System.exit(1);;
		}
		
		List<String> asList = new ArrayList<String>(Arrays.asList(split));
		Iterator<String> it = asList.iterator();
		String last = it.hasNext()? it.next(): null, 
				actual;
		
		while(it.hasNext()) {
			actual = it.next();
			if(actual.equals(last)) {
				it.remove();
			} else {
				last = actual;
			}
		}
		
		split = asList.toArray(new String[0]);

		System.out.println(split);
		
	}

}
