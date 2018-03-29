package br.ufmg.dcc.linkedontogazetteer;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.EnvironmentConfiguration;

public class AppConfiguration {
	
	private AppConfiguration() throws ConfigurationException {
		AppConfiguration.config = new CompositeConfiguration();
//		AppConfiguration.config.addConfiguration(new PropertiesConfiguration("app.properties"));
		AppConfiguration.config.addConfiguration(new EnvironmentConfiguration());
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
	
	public String getRexsterHost() {
		return AppConfiguration.config.getString("REXSTER_HOST");
	}
	
	public String getRexsterUser() {
//		return AppConfiguration.config.getString("rexster.user");
		return AppConfiguration.config.getString("REXSTER_USER");
	}
	
	public String getRexsterPassword() {
//		return AppConfiguration.config.getString("rexster.pass");
		return AppConfiguration.config.getString("REXSTER_PASS");
	}
}
