package mestrado.linkedDataGraph.business;

public class LDIterables {

	
	public static int countEqualsElements(Iterable<?> it1, Iterable<?> it2) {
		int count = 0;
		for(Object obj : it1) {
			for(Object obj2 : it2) {
				if(obj.equals(obj2)) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	public static boolean disjoint(Iterable<?> it1, Iterable<?> it2) {
		for(Object obj : it1) {
			for(Object obj2 : it2) {
				if(obj.equals(obj2)) {
					return false;
				}
			}
		}

		return true;
	}
	
	private LDIterables() {
		
	}
	
	
}
