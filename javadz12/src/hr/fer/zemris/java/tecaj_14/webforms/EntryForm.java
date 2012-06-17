package hr.fer.zemris.java.tecaj_14.webforms;

import hr.fer.zemris.java.tecaj_14.model.BlogEntry;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class EntryForm {
	
	private Long _id;
	private String _title;
	private String _text;
	
	private String id;
	private String title;
	private String text;
	
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private Map<String, String> errors;
	
	public EntryForm() {
		errors = new HashMap<String, String>();
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
		text = request.getParameter("text").toString();
	}
	
	public boolean checkErrors() {
		if(checkId()) _id = Long.parseLong(id);
		if(checkTitle()) _title = title.toString();
		if(checkText()) _text = text.toString();
		
		return hasError();
	}
	
	private boolean checkTitle() {
		if(title == null || title.length() == 0) {
			errors.put("title", "Title value cannot be empty!");
			return false;
		}
		
		clearErrorFor("title");
		return true;
	}
	
	private boolean checkText() {
		if(text == null || text.length() == 0) {
			errors.put("text", "Text value cannot be empty!");
			return false;
		}
		
		clearErrorFor("text");
		return true;
	}
	
	private boolean checkId() {
		if(id == null || id.length() == 0) {
			errors.put("id", "Id value cannot be empty!");
			return false;
		}
		
		try {
			Long.parseLong(id);
		} catch(NumberFormatException e) {
			errors.put("id", "Id value must be an integer!");
			return false;
		}
		
		clearErrorFor("id");
		return true;
	}
	
	public void fromDomainObject(BlogEntry entry) {
		_id = entry.getId();
		_title = entry.getTitle();
		_text = entry.getText();
		
		if(_id != null)
			id = Long.toString(_id);
		if(_title != null)
			title = new String(_title);
		if(_text != null)
			text = new String(_text);
	}
	
	public void toDomainObject(BlogEntry entry) {
		entry.setId(_id);
		entry.setTitle(_title);
		entry.setText(_text);
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}

	@Override
	public String toString() {
		return "Entry ID = " + id;
	}
}
