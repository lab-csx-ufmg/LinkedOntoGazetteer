package br.ufmg.dcc.linkedontogazetteer.bean;

import java.util.List;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

public class GMapModelBean {

	private final MapModel model = new DefaultMapModel();
	private String center;
	
	public GMapModelBean(List<Marker> markers) {
		Double avgLat = 0.0, avgLong = 0.0;
				
		for(Marker m : markers) {
			avgLat += m.getLatlng().getLat();
			avgLong += m.getLatlng().getLng();
			
			this.model.addOverlay(m); 
		}
		
		this.center = String.valueOf(avgLat/markers.size()) + "," + String.valueOf(avgLong/markers.size());
	}
	
	public MapModel getModel() { return this.model; } 

	public String getCenter() {
		return this.center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

}
