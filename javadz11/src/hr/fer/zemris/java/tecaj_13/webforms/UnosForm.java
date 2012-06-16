package hr.fer.zemris.java.tecaj_13.webforms;

import hr.fer.zemris.java.tecaj_13.model.Unos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Class to model form version of the records - for displaying on the JSP page.
 */
public class UnosForm {
	
	private long _id;
	private String _title;
	private String _message;
	private Date _createdOn;
	private String _userEMail;
	
	private String id;
	private String title;
	private String message;
	private String createdOn;
	private String userEMail;
	
	private Map<String, String> errors;
	
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public UnosForm() {
		errors = new HashMap<String, String>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUserEMail() {
		return userEMail;
	}

	public void setUserEMail(String userEMail) {
		this.userEMail = userEMail;
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
		id = request.getParameter("id").toString();
		title = request.getParameter("title").toString();
		message = request.getParameter("message").toString();
		createdOn = request.getParameter("createdOn").toString();
		userEMail = request.getParameter("userEMail").toString();
	}
	
	public boolean checkErrors() {
		if(checkTitle()) _title = title.toString();
		if(checkMessage()) _message = message.toString();
		if(checkEMail()) _userEMail = userEMail.toString();
		if(checkId()) _id = Integer.parseInt(id);
		if(checkCreatedOn())
			try {_createdOn = new SimpleDateFormat(DATE_FORMAT).parse(createdOn);
			} catch (ParseException ignorable) {}
		
		System.out.println("createdOn is " + createdOn);
		System.out.println("_createdOn is " + _createdOn);
		
		return hasError();
	}
	
	private boolean checkCreatedOn() {
		if(createdOn == null || createdOn.length() == 0) {
			errors.put("createdOn", "CreatedOn date field cannot be empty!");
			return false;
		}
		
		try {
			new SimpleDateFormat(DATE_FORMAT).parse(createdOn);
		} catch (ParseException e) {
			errors.put("createdOn", "CreateOn field is not a date!");
			return false;
		}
		
		clearErrorFor("createdOn");
		return true;
	}
	
	private boolean checkId() {
		if(id == null || id.length() == 0) {
			errors.put("id", "ID field cannot be empty!");
			return false;
		}
		
		try {
			Integer.parseInt(id);
		} catch (NumberFormatException e) {
			errors.put("id", "ID field is not an integer!");
			return false;
		}
		
		clearErrorFor("id");
		return true;
	}
	
	private boolean checkMessage() {
		if(message == null || message.length() == 0) {
			errors.put("message", "Message value cannot be empty!");
			return false;
		}
		
		clearErrorFor("message");
		return true;
	}
	
	private boolean checkEMail() {
		if(userEMail == null || userEMail.length() == 0) {
			errors.put("userEMail", "User email value cannot be empty!");
			return false;
		}
		
		if(!userEMail.contains("@")) {
			errors.put("userEMail", "User email must have '@' sign!");
			return false;
		}
		
		clearErrorFor("userEMail");
		return true;
	}
	
	private boolean checkTitle() {
		if(title == null || title.length() == 0) {
			errors.put("title", "Title value cannot be empty!");
			return false;
		}
		
		clearErrorFor("title");
		return true;
	}
	
	public void fromDomainObject(Unos unos) {
		_id = unos.getId();
		_title = unos.getTitle();
		_message = unos.getMessage();
		_createdOn = unos.getCreatedOn();
		_userEMail = unos.getUserEMail();
		
		id = Long.toString(_id);
		if(_title != null)
			title = new String(_title);
		if(_message != null)
			message = new String(_message);
		if(_userEMail != null)
			userEMail = _userEMail.toString();
		if(_createdOn != null)
			createdOn = new SimpleDateFormat(DATE_FORMAT).format(_createdOn);
	}
	
	public void toDomainObject(Unos unos) {
		unos.setId(_id);
		unos.setTitle(_title);
		unos.setMessage(_message);
		unos.setUserEMail(_userEMail);
		unos.setCreatedOn(_createdOn);
	}

	@Override
	public String toString() {
		return "Unos id="+id;
	}
}
