package zemris.fer.hr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zemris.fer.hr.addressbook.R;
import zemris.fer.hr.models.Contact;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddContactActivity extends Activity {
	
	private static final String TAG = "AddressBook | AddContactActivity";
	
	private List<String> assetsImages = new ArrayList<String>();
	private Contact contact = new Contact();
	
	/* LAYOUT RESOURCES */
	private EditText editTextName;
	private EditText editTextEmail;
	private EditText editTextPhone;
	private EditText editTextNote;
	private EditText editTextFacebookProfile;
	private Button buttonImage;
	private Button buttonSave;
	private Button buttonCancel;
	
	private void getUIElements() {
		editTextName = (EditText) findViewById(R.id.name);
		editTextEmail = (EditText) findViewById(R.id.email);
		editTextPhone = (EditText) findViewById(R.id.phone);
		editTextNote = (EditText) findViewById(R.id.note);
		buttonImage = (Button) findViewById(R.id.image);
		editTextFacebookProfile = (EditText) findViewById(R.id.facebook_profile);

		buttonSave = (Button) findViewById(R.id.button_save);
		buttonCancel = (Button) findViewById(R.id.button_cancel);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		getUIElements();
		
		// Get images file names
		final AssetManager assetManager = this.getAssets();
		retrieveAssetsFileNames(assetManager, "", ".jpeg", assetsImages);
		
		// Image chooser
		buttonImage.setOnClickListener(new View.OnClickListener() {
			
			Dialog dialog;
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
				
				builder.setTitle("Odaberi sliku...");

				final String[] imagesArray = new String[assetsImages.size()];
				assetsImages.toArray(imagesArray);
				
				ListView listView = new ListView(AddContactActivity.this);
				ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(AddContactActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, imagesArray);
				listView.setAdapter(modeAdapter);
				
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						try {
							contact.setImage(BitmapFactory.decodeStream(assetManager.open(imagesArray[position] + ".jpeg")));
							buttonImage.setText(imagesArray[position]);
			    		} catch (IOException ignorable) {}
						
						dialog.dismiss();
					}
					
				});
				
				builder.setView(listView);

				dialog = builder.create();
				dialog.show();
			}
			
		});
		
		// Save contact and close the activity
		buttonSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = editTextName.getText().toString();
				String email = editTextEmail.getText().toString();
				String phone = editTextPhone.getText().toString();
				String note = editTextNote.getText().toString();
				String facebookProfile = editTextFacebookProfile.getText().toString();
				
				if(!checkRequiredFields()) {
					return;
				}
				
				contact.setName(name);
				contact.setPhone(phone);
				contact.setEmail(email);
				contact.setFacebookProfile("http://www.facebook.com/" + facebookProfile);
				
				if(!note.trim().isEmpty()) contact.setNote(note);
				
				HomeActivity.persons.add(contact);
				
				finish();
			}
			
		});

		// Just close the activity 
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		
	}
	
	/**
	 * Retrieves all file names with <code>extension</code> extension
	 * from the <code>path</code> and stores them into <code>fileNames</code> list.
	 * 
	 * @param assetManager	Asset manager of your application.
	 * @param path			Path from which you want to list out the images.
	 * @param extension		Filter out by extension (or ignore it if set to <code>null</code>).
	 * @param fileNames		List where to store the retrievet file names.
	 */
	private void retrieveAssetsFileNames(AssetManager assetManager, String path, String extension, List<String> fileNames) {
		try {
			String list[] = assetManager.list(path);
			if (list != null)
				for (int i = 0; i < list.length; ++i) {
					String fileName = list[i];
					if(extension == null || fileName.endsWith(extension)) {
						fileNames.add(fileName.replace(extension, ""));
					}
					
					retrieveAssetsFileNames(assetManager, path + "/" + fileName, extension, fileNames);
				}
		} catch (IOException e) {
			Log.d(TAG, "Error while listing '" + path + "' folder.");
		}
	}
	
	/**
	 * @return	Returns <code>true</code> if all the required fields are filled,
	 * 			<code>false</code> otherwise with {@link Toast} popup.
	 */
	private boolean checkRequiredFields() {
		// Name
		if(isEditTextEmpty(editTextName)) {
			showRequiredFieldError(editTextName);
			return false;
		}
		
		// Email
		if(isEditTextEmpty(editTextEmail)) {
			showRequiredFieldError(editTextEmail);
			return false;
		}
		
		// Phone
		if(isEditTextEmpty(editTextPhone)) {
			showRequiredFieldError(editTextPhone);
			return false;
		}
		
		// Facebook
		if (isEditTextEmpty(editTextFacebookProfile)) {
			showRequiredFieldError(editTextFacebookProfile);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check if provided {@link EditText} is empty or not.
	 * 
	 * @param editText	{@link EditText} you want to check.
	 * @return			<code>true</code> if it's empty, <code>false</code> otherwise.
	 */
	private boolean isEditTextEmpty(EditText editText) {
		String text = editText.getText().toString();
		return text.trim().isEmpty();
	}
	
	/**
	 * Display the {@link Toast} popup with predefined message +
	 * the hint from {@link EditText}.
	 * 
	 * @param editText	{@link EditText} hint you want to include in the message.
	 */
	private void showRequiredFieldError(EditText editText) {
		Toast.makeText(this, "Molimo ispunite '" + editText.getHint().toString() + "' kao obavezno polje", Toast.LENGTH_LONG).show();
	}

}