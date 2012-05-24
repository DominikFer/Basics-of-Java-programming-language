package hr.fer.zemris.java.tecaj_10.local.swing;

import hr.fer.zemris.java.tecaj_10.local.ILocalizationListener;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationProvider;

import javax.swing.JToolBar;

public class LJToolBar extends JToolBar {

	private static final long serialVersionUID = -4238307957974845144L;

	private ILocalizationProvider provider;
	private String key;
	
	ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			updateText();
		}
	};
	
	public LJToolBar(ILocalizationProvider provider, String key) {
		this.provider = provider;
		this.key = key;
		this.provider.addLocalizationListener(listener);
		updateText();
	}
	
	private void updateText() {
		this.setName(this.provider.getString(key));
	}
	
}
