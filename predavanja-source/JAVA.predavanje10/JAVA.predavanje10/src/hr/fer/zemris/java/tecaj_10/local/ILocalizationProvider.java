package hr.fer.zemris.java.tecaj_10.local;

public interface ILocalizationProvider {

	public void addLocalizationListener(ILocalizationListener l);
	
	public void removeLocalizationListener(ILocalizationListener l);
	
	public String getText(String key);
	
}
