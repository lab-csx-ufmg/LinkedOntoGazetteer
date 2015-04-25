package br.ufmg.dcc.linkedontogazetteer;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class AppConfiguration {
	
	private AppConfiguration() throws ConfigurationException {
		AppConfiguration.config = new CompositeConfiguration();
		AppConfiguration.config.addConfiguration(new PropertiesConfiguration("app.properties"));
	}
	
	private static AppConfiguration instance;

	private static CompositeConfiguration config;
	
	public static AppConfiguration getConfiguration() {
		if(AppConfiguration.instance == null) {
			try {
				AppConfiguration.instance = new AppConfiguration();
			} catch (ConfigurationException e) {
				e.printStackTrace();
				return null;
			}
		}
		return AppConfiguration.instance;
	}
	
	public String getRexsterUser() {
		return AppConfiguration.config.getString("rexster.user");
	}
	
	public String getRexsterPassword() {
		return AppConfiguration.config.getString("rexster.pass");
	}
}
