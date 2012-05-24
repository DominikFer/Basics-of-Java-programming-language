package hr.fer.zemris.java.tecaj_10.local;

public interface ILocalizationProvider {
	
	public String getLanguage();

	public void addLocalizationListener(ILocalizationListener l);
	
	public void removeLocalizationListener(ILocalizationListener l);
	
	public String getString(String key);
	
	public String getString(String key, String[] data);

	
}
