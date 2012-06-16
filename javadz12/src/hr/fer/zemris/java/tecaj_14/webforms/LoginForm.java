package hr.fer.zemris.java.tecaj_14.webforms;

import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class LoginForm {
	
	private String _nick;
	private String _password;
	
	private String nick;
	private String password;
	
	private Map<String, String> errors;
	
	public LoginForm() {
		errors = new HashMap<String, String>();
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean hasError() {
		return !errors.isEmpty();
	}
	
	public void clearErrors() {
		errors.clear();
	}
	

    public boolean hasErrorFor(String key) {
    	return errors.containsKey(key);
    }

    public String getErrorFor(String key) {
    	return errors.get(key);
    }

    public void clearErrorFor(String key) {
    	if(errors.containsKey(key)) errors.remove(key);
    }
    
	public void fill(HttpServletRequest request) {
		nick = request.getParameter("nick").toString();
		password = request.getParameter("password").toString();
	}
	
	public boolean checkErrors() {
		if(checkNick()) _nick = nick.toString();
		if(checkPassword()) _password = password.toString();
		
		return hasError();
	}
	
	private boolean checkNick() {
		if(nick == null || nick.length() == 0) {
			errors.put("nick", "Nick value cannot be empty!");
			return false;
		}
		
		clearErrorFor("nick");
		return true;
	}
	
	private boolean checkPassword() {
		if(password == null || password.length() == 0) {
			errors.put("password", "Password value cannot be empty!");
			return false;
		}
		
		clearErrorFor("password");
		return true;
	}
	
	public void fromDomainObject(BlogUser register) {
		_nick = register.getNick();
		_password = register.getPassword();
		
		if(_nick != null)
			nick = new String(_nick);
		if(_password != null)
			password = new String(_password);
	}
	
	public void toDomainObject(BlogUser user) {
		user.setNick(_nick);
		user.setPassword(_password);
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}

	@Override
	public String toString() {
		return "User nick = " + nick;
	}
}
