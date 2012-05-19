package hr.fer.zemris.java.tecaj_10.local.swing;

import hr.fer.zemris.java.tecaj_10.local.ILocalizationListener;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationProvider;

import javax.swing.JMenu;

public class LJMenu extends JMenu {

	private static final long serialVersionUID = 7340150473541536291L;
	
	private ILocalizationProvider provider;
	private String key;
	
	ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			updateText();
		}
	};
	
	public LJMenu(ILocalizationProvider provider, String key) {
		this.provider = provider;
		this.key = key;
		this.provider.addLocalizationListener(listener);
		updateText();
	}
	
	private void updateText() {
		this.setText(this.provider.getString(key));
	}

}
