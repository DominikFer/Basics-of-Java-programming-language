package hr.fer.zemris.java.tecaj_10.local.swing;

import hr.fer.zemris.java.tecaj_10.local.ILocalizationListener;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationProvider;

import javax.swing.JLabel;

public class LJLabel extends JLabel {

	private static final long serialVersionUID = -4238307957974845144L;

	private ILocalizationProvider provider;
	private String key;
	
	ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			updateText();
		}
	};
	
	public LJLabel(ILocalizationProvider provider, String key) {
		this.provider = provider;
		this.key = key;
		this.provider.addLocalizationListener(listener);
		updateText();
	}
	
	public LJLabel(ILocalizationProvider provider, String key, int horizontalAligment) {
		super(key, horizontalAligment);
		this.provider = provider;
		this.key = key;
		this.provider.addLocalizationListener(listener);
		updateText();
	}
	
	private void updateText() {
		this.setText(this.provider.getString(key));
	}
	
}
