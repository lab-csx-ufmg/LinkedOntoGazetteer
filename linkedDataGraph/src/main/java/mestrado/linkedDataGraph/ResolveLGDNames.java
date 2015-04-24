package mestrado.linkedDataGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.util.structures.Row;

public class ResolveLGDNames {
	private static final Logger LOGGER = Logger.getLogger(ResolveLGDNames.class);
//	static String[] namesArray = new String[]{"","*","06","1220","17217 Stadt Penzlin","23 August;Constan\u021Ba;Rom\u00E2nia","240","24-H\u00F6fe","2\u00E8me Sources Chaudes","3053 M\u00FCnchenbuhsee","33950","45110","4\u03BF \u0394\u03B7\u03BC\u03BF\u03C4\u03B9\u03BA\u03CC \u0394\u03B9\u03B1\u03BC\u03AD\u03C1\u03B9\u03C3\u03BC\u03B1 \u0398\u03B5\u03C3\u03C3\u03B1\u03BB\u03BF\u03BD\u03AF\u03BA\u03B7\u03C2,Thessaloniki municipality,Thessaloniki prefecture,Kentriki Makedonia,Greece,EU","6 October, Egypt","6 Oktober, Egypt","7600","93220","Aabenraa, Jylland","Aabenraa, Jylland, Danmark","Aabenraa;S\u00F8nderjylland;Danmark","Aachen-Mitte,Aachen,St\u00E4dteregion Aachen,Regierungsbezirk K\u00F6ln,Nordrhein-Westfalen,Bundesrepublik Deutschland,Europe","Aachen,Regierungsbezirk K\u00F6ln,Nordrhein-Westfalen,Bundesrepublik Deutschland,Europe","Aachen,St\u00E4dteregion Aachen,Regierungsbezirk K\u00F6ln,Nordrhein-Westfalen,Bundesrepublik Deutschland,Europe","Aadorf","Aadorf, Switzerland,CH","A Agualada;Coristanco;A Coru\u00F1a;Galicia;Espa\u00F1a;Europe","(A Aguarda), A Pastoriza, Lugo, Galicia, Spain, Europe","A Albergueria (Santa Mar\u00EDa), Vilar de Barrio, Ourense, Galicia, Spain, Europe","Aalborg Kommune,Nordjylland,Danmark,Europe","Aalen,Ostalbkreis,Stuttgart,Baden-W\u00FCrttemberg,Bundesrepublik Deutschland,Europe","Aalst","Aalst,Aalst,Oost-Vlaanderen,Oost-Vlaanderen,Vlaanderen,Vlaanderen,Belgique,Belgique,Europe","Aalst, Oost-Vlaanderen","A Ameixeira (San Bernabeu), Crecente, Pontevedra, Galicia, Spain, Europe","A Ameixenda;Cee;A Coru\u00F1a;Galicia;Espa\u00F1a;Europe","A Arnoia, Ourense, Galicia, Spain","A Az\u00FAmara (San Xo\u00E1n), Castro de Rei, Lugo, Galicia, Spain, Europe","Aba","Abades (San Paio), Baltar, Ourense, Galicia, Spain, Europe","Abadla, B\u00E9char, Algeria","Abad\u00EDn, Lugo, Galiza, Spain","Abai kist\u00E9rs\u00E9g; Fej\u00E9r megye; K\u00F6z\u00E9p-Dun\u00E1nt\u00FAl; Magyarorsz\u00E1g","AB; Alberta; Canada","A Balsa (San Breixo), Triacastela, Lugo, Galicia, Spain, Europe","Abaltzisketa, Tolosaldea, Gipuzkoa, Euskal Herria / Pa\u00EDs Vasco, Spain, Europe","Abano Terme, Padova, Veneto, Italy","Abanto","Abaran;Murcia;Region de Murcia;Espa\u00F1a;Europe","A Barcia do Seixo (Santa Ana), A Lama, Pontevedra, Galicia","A BArcia do Seixo (Santa Ana), A Lama, Pontevedra, Galicia","Abashi, Kvemo Kartli, Georgia, Asia","Abashis Raioni, Georgia, Asia","Abashis Raioni, Kvemo, Samegrelo-Zemo Svanet'i, Georgia, Asia","(A Bastida), A Fonsagrada, Lugo, Galicia, Spain, Europe","Aba\u00FAj-Hegyk\u00F6zi kist\u00E9rs\u00E9g; Borsod-Aba\u00FAj-Zempl\u00E9n megye; \u00C9szak-Magyarorsz\u00E1g; Magyarorsz\u00E1g","Aba\u00FAj-Hegyk\u00F6zi kist\u00E9rs\u00E9g;Borsod-Aba\u00FAj-Zempl\u00E9n megye;\u00C9szak-Magyarorsz\u00E1g;Magyarorsz\u00E1g","Aba\u00FAjk\u00E9r","Abavides (San Marti\u00F1o), Trasmiras, Ourense, Galicia, Spain, Europe","Abbehausern, Nordenham","Abbeville,South Carolina,S.C.,SC,USA","Abbottabad","Abda","Abejones, Oaxaca, M\u00E9xico","Abelenda, Avi\u00F3n, Ourense, Galicia, Spain, Europe","Abelenda das Penas (Santo Andr\u00E9), Carballeda de Avia, Ourense, Galicia, Spain, Europe","Abella de la Conca","Abenberg,Roth,Mittelfranken,Bayern,Bundesrepublik Deutschland,Europe","Abensberg,Kelheim,Niederbayern,Bayern,Bundesrepublik Deutschland,Europe","Aber-banc","Abercarn","Aberdeen","Aberdeen, Aberdeen City, Scotland, United Kingdom","Aberdeenshire","Aberdeenshire, Scotland","Aberdeenshire, Scotland, UK","Abfaltersbach,Lienz,Tirol,\u00D6sterreich,Europe","Abhiramapuram","Abidjan","Abidjan,C\u00F4te d'Ivoire","Abidjan;R\u00E9gion des Lagunes;C\u00F4te d\u2019Ivoire;Africa","Abingdon","Abitibi-T\u00E9miscamingue, Qu\u00E9bec, Canada","Abitibi-T\u00E9miscamingue,Qu\u00E9bec, Canada","Abkhazeti, Georgia, Abkhasia, Asia","Abou El Hassen, Chlef, Algeria","Abrag\u00E1n (San Bartolomeu), O Corgo, Lugo, Galicia, Spain, Europe","Abrag\u00E1n (Santa Mar\u00EDa), O Corgo, Lugo, Galicia, Spain, Europe","Abram,Bihor,Rom\u00E2nia","Abrantes","Abrantes; Santarem","A Bra\u00F1a (San Miguel), Baleira, Lugo, Galicia, Spain, Europe","(Abrence), A Pobra do Broll\u00F3n, Lugo, Galicia, Spain, Europe","Abres (Santiago), Vegadeo, Asturias, Spain, Europe","A Broza (Santo Tom\u00E9), O Savi\u00F1ao, Lugo, Galicia, Spain, Europe","Abr\u0103mu\u021B,Bihor,Rom\u00E2nia","Abrud,Alba,Rom\u00E2nia","Abrud;Alba;Rom\u00E2nia","Abruzzo","Absberg,Wei\u00DFenburg-Gunzenhausen,Mittelfranken,Bayern,Bundesrepublik Deutschland,Europa","Absberg,Wei\u00DFenburg-Gunzenhausen,Mittelfranken,Bayern,Bundesrepublik Deutschland,Europe"};
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		PropertyConfigurator.configure("log4jfilter.properties");

		Properties p = new Properties(System.getProperties());
		p.load(new FileInputStream(new File("app.properties")));
		System.setProperties(p);

		BaseConfiguration config = new BaseConfiguration();
		config.setProperty("storage.backend", "cassandrathrift");
		config.setProperty("storage.hostname", System.getProperty("storage.hostname"));
		config.setProperty("storage.index.search.backend", "elasticsearch");
		config.setProperty("storage.index.search.directory", "../db/es");
		config.setProperty("storage.index.search.client-only", false);
		config.setProperty("storage.index.search.local-mode", true);

		FramedGraph<TitanGraph> g = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);
		
		BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("isin.fpath")));
    	
    	String names;
		
    	while((names = reader.readLine()) != null) {
			Map<String, Set<Long>> result = new HashMap<String, Set<Long>>();
			
			String[] split = names.split("[,;]");
			
			if(split.length == 0) {
				continue;
			}
			
			List<String> asList = new ArrayList<String>(Arrays.asList(split));
			Iterator<String> it = asList.iterator();
			String last = it.hasNext()? it.next(): null, 
					actual;
			
			while(it.hasNext()) {
				actual = it.next();
				if(actual.equals(last)) {
					ResolveLGDNames.LOGGER.info("DUPLICADO:\t" + actual);
					it.remove();
				} else {
					last = actual;
				}
			}
			
			split = asList.toArray(new String[0]);
			
			List<String> listVar = Arrays.asList(new String[]{"from","to"});
			GremlinPipeline pipe;

			for(String name : split) {
				result.put(name, new HashSet<Long>());
			}

			//Se a lista de nomes possui apenas um elemento, só retorna um id associado se ele não for ambiguo
			if(split.length == 1) {
				pipe = new GremlinPipeline();
				List list = pipe.start(g.getVertices("name", split[0].trim())).out("isName").as("from").select(listVar).dedup().toList();
				
				if(list.size() == 1) {
					Row r = (Row) list.get(0);
					Vertex from = (Vertex) r.getColumn("from");
					result.get(split[0]).add((Long) from.getId());
				}
			} else if(split.length > 1) {
				int i=1;
				List list;
				
				//Inicia a análise par a par com o objetivo de achar o lugar i-1 que está contido no lugar i.
				//Essa verificação é feita até obter um par que retorne algo
				for(;i<split.length; i++) {
					pipe = new GremlinPipeline();
					list = pipe.start(g.getVertices("name", split[i-1].trim())).out("isName").as("from").out("containedBy").as("to").in("isName").has("name", split[i].trim()).select(listVar).dedup().toList();
					
					for(Object obj : list) {
						Row r = (Row) obj;
						Vertex from = (Vertex) r.getColumn("from");
						Vertex to = (Vertex) r.getColumn("to");
						result.get(split[i-1]).add((Long) from.getId());
						result.get(split[i]).add((Long) to.getId());
					}
					
					if(list.size() > 0) {
						i++;
						break;
					}
				}
				
				//A partir do momento em que foi localizado um par de lugares que estão contindo um no outro
				//inicia-se a verificação dos próximos.
				for(; i<split.length; i++) {
					for(Long id : result.get(split[i-1])) {
						pipe = new GremlinPipeline();
						list = pipe.start(g.getVertex(id)).out("containedBy").as("to").in("isName").has("name", split[i].trim()).select(listVar).dedup().toList();
						
						if(list.size() > 0) {
							for(Object obj : list) {
								Row r = (Row) obj;
								Vertex to = (Vertex) r.getColumn("to");
								result.get(split[i]).add((Long) to.getId());
							}
						} 
					}
					
					//Se apartir do lugar atual não for possivel identificar nenhum relacionamento com 
					//outros lugares que possam contê-lo. Ignora-se o relacionamento e busca-se pelo nome apenas.
					if(result.get(split[i]).isEmpty()) {
						pipe = new GremlinPipeline();
						list = pipe.start(g.getVertices("name", split[i].trim())).out("isName").as("to").select(listVar).dedup().toList();
						
						for(Object obj : list) {
							Row r = (Row) obj;
							Vertex to = (Vertex) r.getColumn("to");
							result.get(split[i]).add((Long) to.getId());
						}
					}
				}
				
			}
			
			String output = "OUTPUT:" + names + "\t";
			String statistics = "";
			
			for(String name : split) {
				statistics += "\nSTATISTICS:\t" + name.trim() + "\t" + result.get(name).size();
				 output += name.trim() + ":" + (result.get(name).isEmpty() ? "[###]" : result.get(name)) + " ";
			}
			ResolveLGDNames.LOGGER.info(output);
			ResolveLGDNames.LOGGER.info(statistics);
		}
	}
}
