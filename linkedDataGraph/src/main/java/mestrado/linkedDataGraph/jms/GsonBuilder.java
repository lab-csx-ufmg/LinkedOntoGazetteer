package mestrado.linkedDataGraph.jms;

import com.google.gson.Gson;

public class GsonBuilder {
	private static final Gson gson = new Gson();

	public static Gson getGson() {
		return GsonBuilder.gson;
	}
	
}
