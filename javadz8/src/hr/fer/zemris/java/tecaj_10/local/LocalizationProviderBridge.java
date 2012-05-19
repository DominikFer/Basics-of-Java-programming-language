package hr.fer.zemris.java.tecaj_10.local;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	ILocalizationProvider parent;
	ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			fire();
		}
	};
	
	private String disconnectLanguage;
	private boolean connected;
	
	public void connect() {
		this.connected = true;
		this.parent.addLocalizationListener(listener);
		
		if (!(this.parent.getLanguage().equals(disconnectLanguage))) {
			listener.localizationChanged();
		}
	}
	
	public void disconnect() {
		this.connected = false;
		this.disconnectLanguage = this.parent.getLanguage();
		this.parent.removeLocalizationListener(listener);
	}
	
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
	}
	
	@Override
	public String getLanguage() {
		return disconnectLanguage;
	}

	@Override
	public String getString(String key) {
		if (this.connected == true)
			return parent.getString(key);
		else
			return null;
	}
	
	@Override
	public String getString(String key, String[] data) {
		return parent.getString(key, data);
	}

}
