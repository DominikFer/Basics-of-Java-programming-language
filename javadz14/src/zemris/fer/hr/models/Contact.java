package zemris.fer.hr.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Model for contact objects. Keeps the information about the contact's name, email,
 * phone, Facebook profile link, some note and an image. 
 */
public class Contact {

	private static final String TAG = "AddressBook | Contact model";
	
	private String name;
	private String email;
	private String phone;
	private Bitmap image;
	private String note;
	private String facebookProfile;
	
	/**
	 * Default constructor.
	 */
	public Contact() {}
	
	/**
	 * Parses the provided {@link JSONObject} and fills the
	 * fields. Required attributes are <code>name</code>,
	 * <code>email</code>, <code>phone</code> and
	 * <code>facebook_profile</code>.
	 * 
	 * @param jsonContact	
	 * @return				Current instance.
	 */
	public Contact parseJson(JSONObject jsonContact) {
		try {
			this.name = jsonContact.getString("name");
			this.email = jsonContact.getString("email");
			this.phone = jsonContact.getString("phone");
			this.note = jsonContact.optString("note");
			this.facebookProfile = jsonContact.getString("facebook_profile");
		} catch (JSONException e) {
			Log.d(TAG, "Could not parse JSONObject 'person' in 'people.json' file.");
		}
		
		return this;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFacebookProfile() {
		return facebookProfile;
	}

	public void setFacebookProfile(String facebookProfile) {
		this.facebookProfile = facebookProfile;
	}

	@Override
	public String toString() {
		return "Contact [name=" + name + ", email=" + email + ", phone="
				+ phone + ", image=" + image + ", note=" + note
				+ ", facebookProfile=" + facebookProfile + "]";
	}
	
}
