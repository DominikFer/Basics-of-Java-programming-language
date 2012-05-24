package hr.fer.zemris.java.tecaj_10.local.swing;

import hr.fer.zemris.java.tecaj_10.local.ILocalizationListener;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationProvider;

import javax.swing.JFileChooser;

public class LJFileChooser extends JFileChooser {

	private static final long serialVersionUID = -4238307957974845144L;

	private ILocalizationProvider provider;
	private String key;
	
	ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			updateText();
		}
	};
	
	public LJFileChooser(ILocalizationProvider provider, String key) {
		this.provider = provider;
		this.key = key;
		this.provider.addLocalizationListener(listener);
		updateText();
	}
	
	private void updateText() {
		this.setDialogTitle(this.provider.getString(key));
	}
	
}
