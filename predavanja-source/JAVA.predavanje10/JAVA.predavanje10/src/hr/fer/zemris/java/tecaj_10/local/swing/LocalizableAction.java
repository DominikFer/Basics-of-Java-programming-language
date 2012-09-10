package hr.fer.zemris.java.tecaj_10.local.swing;

import hr.fer.zemris.java.tecaj_10.local.ILocalizationListener;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationProvider;

import javax.swing.AbstractAction;
import javax.swing.Action;

public abstract class LocalizableAction extends AbstractAction {

	private static final long serialVersionUID = 948728206148712733L;
	
	private ILocalizationProvider provider;
	private String key;
	
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			updateName();
		}
	};
	
	public LocalizableAction(ILocalizationProvider provider, String key) {
		super();
		this.provider = provider;
		this.key = key;
		this.provider.addLocalizationListener(listener);
		updateName();
	}	
	
	public void updateName() {
		putValue(Action.NAME, provider.getText(key));
	}

}
