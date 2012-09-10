package zemris.fer.hr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zemris.fer.hr.adapters.ContactAdapter;
import zemris.fer.hr.addressbook.R;
import zemris.fer.hr.models.Contact;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

public class HomeActivity extends Activity {
	
	private static final String TAG = "AddressBook | HomeActivity";
	public static List<Contact> persons = new ArrayList<Contact>();
	
	private ContactAdapter contactAdapter;
	
	/* LAYOUT RESOURCES */
	private Button addContact;
	private ListView list;
	
	private void getUIElements() {
		addContact = (Button) findViewById(R.id.button_add);
		list = (ListView) findViewById(R.id.list);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getUIElements();
        
        addContact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomeActivity.this, AddContactActivity.class);
				startActivityForResult(i, 100);
			}
		});
        
        // Set adapter and fill the list
        initList();
    }
    
    private void initList() {
    	if(persons.isEmpty())
        	populateListFromFile();
        
        contactAdapter = new ContactAdapter(this, persons);
    	list.setAdapter(contactAdapter);
        
    	// Show contact details
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
				i.putExtra(ProfileActivity.EXTRAS_CONTACT_POSITION, position);
				startActivity(i);
			}
			
		});
        
        // Delete the contact?
        list.setOnItemLongClickListener(new OnItemLongClickListener() {
        	
        	Dialog dialog;
        	
        	@Override
        	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
				AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(HomeActivity.this);
				
				deleteBuilder.setTitle("Brisanje");
				deleteBuilder.setMessage("Želite li izbrisati kontakt '" + persons.get(position).getName() + "'?");
				
				deleteBuilder.setPositiveButton("Da",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								contactAdapter.remove(persons.get(position));
								dialog.dismiss();
							}
						});
				
				deleteBuilder.setNegativeButton("Ne",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								dialog.dismiss();
							}
						});
				
				dialog = deleteBuilder.create();
				dialog.show();
        		
        		return false;
        	}
        	
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	contactAdapter.notifyDataSetChanged();
    }
    
    /**
     * Parses the 'people.json' file from the assests folder and
     * populates a <code>persons</code> list.
     * 
     * @return					<code>true</code> if everything went OK,
     * 							<code>false</code> otherwise.
     * @throws JSONException
     * @throws IOException
     */
    private boolean populateListFromFile() {
    	AssetManager assetManager = this.getAssets();
		
    	String fileString = openFileAsString(assetManager, "people.json");
    	if(fileString == null) return false; // Some error happened
    	
    	JSONObject people;
		try {
			people = new JSONObject(fileString);
			JSONArray peopleArray = people.getJSONArray("people");
	    	
	    	for(int i = 0; i < peopleArray.length(); i++) {
	    		JSONObject personJson = peopleArray.getJSONObject(i).getJSONObject("person");
	    		
	    		// Create new person and parse the JSON object
	    		Contact person = new Contact().parseJson(personJson);
	    		
	    		// Try to import the image
	    		try {
	    			person.setImage(BitmapFactory.decodeStream(assetManager.open(personJson.getString("image"))));
	    		} catch (IOException ignorable) {
	    			Log.d(TAG, "Could not retrieve an '" + personJson.getString("image") + "' image from the assets folder in 'people.json' file.");
	    		}
	    		
	    		// Add it to the list
	    		persons.add(person);
	    	}
		} catch (JSONException e) {
			Log.d(TAG, "Could not parse JSON in 'people.json' file.");
			return false; // Invalid JSON?
		}
    	
    	return true;
    }
    
    /**
     * Opens the file from the <code>assets</code> and parses it into
     * {@link String}.
     * 
     * @param assetManager		Activity asset manager.
     * @param fileName			File you want to open.
     * @return					Returns file as a {@link String}, if some
     * 							error happens returns <code>null</code>.
     */
    private String openFileAsString(AssetManager assetManager, String fileName) {
    	String fileString = "";
    	
    	InputStream peopleInputStream;
		try {
			peopleInputStream = assetManager.open(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(peopleInputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				fileString += line;
			}
		} catch (IOException e) {
			return null;
		}
		
		return fileString;
    }
    
}