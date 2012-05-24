package hr.fer.zemris.java.tecaj_10.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {

	private static LocalizationProvider instance = new LocalizationProvider();
	private String language;
	private ResourceBundle bundle;
	
	private LocalizationProvider() {
		setLanguage("en");
	}
	
	public static LocalizationProvider getInstance() { 
		return instance;
	}
	
	public void setLanguage(String language) {
		this.language = language;
		Locale locale = Locale.forLanguageTag(this.language);
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.tecaj_10.local.Poruke", locale);
		fire();
	}
	
	@Override
	public String getLanguage() {
		return this.language;
	}
	
	@Override
	public String getString(String key) {
		try {
			return bundle.getString(key);			
		} catch (Exception ex) {
			return "?" + key;
		}
	}

	@Override
	public String getString(String key, String[] data) {
		
		String text = this.getString(key);
		for (int i = 0; i < data.length; ++i) {
			text = text.replaceAll("\\%"+i+"\\%", data[i]);
		}
		
		return text;
		
	}

}
