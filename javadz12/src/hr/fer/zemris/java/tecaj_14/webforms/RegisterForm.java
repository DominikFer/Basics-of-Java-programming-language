package hr.fer.zemris.java.tecaj_14.webforms;

import hr.fer.zemris.java.tecaj_14.model.BlogUser;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RegisterForm {
	
	private String _firstName;
	private String _lastName;
	private String _email;
	private String _nick;
	private String _password;
	
	private String firstName;
	private String lastName;
	private String email;
	private String nick;
	private String password;
	
	private Map<String, String> errors;
	
	public RegisterForm() {
		errors = new HashMap<String, String>();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		firstName = request.getParameter("firstName").toString();
		lastName = request.getParameter("lastName").toString();
		email = request.getParameter("email").toString();
		nick = request.getParameter("nick").toString();
		password = request.getParameter("password").toString();
	}
	
	public boolean checkErrors() {
		if(checkFirstName()) _firstName = firstName.toString();
		if(checkLastName()) _lastName = lastName.toString();
		if(checkNick()) _nick = nick.toString();
		if(checkPassword()) _password = password.toString();
		if(checkEMail()) _email = email.toString();
		
		return hasError();
	}
	
	private boolean checkFirstName() {
		if(firstName == null || firstName.length() == 0) {
			errors.put("firstName", "First name value cannot be empty!");
			return false;
		}
		
		clearErrorFor("firstName");
		return true;
	}
	
	private boolean checkLastName() {
		if(lastName == null || lastName.length() == 0) {
			errors.put("lastName", "Last name value cannot be empty!");
			return false;
		}
		
		clearErrorFor("lastName");
		return true;
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
	
	private boolean checkEMail() {
		if(email == null || email.length() == 0) {
			errors.put("email", "User email value cannot be empty!");
			return false;
		}
		
		if(!email.contains("@")) {
			errors.put("email", "User email must have '@' sign!");
			return false;
		}
		
		clearErrorFor("email");
		return true;
	}
	
	public void fromDomainObject(BlogUser register) {
		_firstName = register.getFirstName();
		_lastName = register.getLastName();
		_nick = register.getNick();
		_email = register.getEmail();
		_password = register.getPassword();
		
		if(_firstName != null)
			firstName = new String(_firstName);
		if(_lastName != null)
			lastName = new String(_lastName);
		if(_nick != null)
			nick = new String(_nick);
		if(_password != null)
			password = new String(_password);
		if(_email != null)
			email = _email.toString();
	}
	
	public void toDomainObject(BlogUser user) {
		user.setFirstName(_firstName);
		user.setLastName(_lastName);
		user.setNick(_nick);
		user.setEmail(_email);
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
