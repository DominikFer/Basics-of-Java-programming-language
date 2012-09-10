package zemris.fer.hr;

import zemris.fer.hr.addressbook.R;
import zemris.fer.hr.models.Contact;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	
	public static final String EXTRAS_CONTACT_POSITION = "contact";
	
	private Contact contact;
	
	/* LAYOUT RESOURCES */
	private TextView textViewName;
	private TextView textViewPhone;
	private TextView textViewEmail;
	private TextView textViewNote;
	private ImageView imageViewPicture;
	private Button buttonCall;
	private Button buttonFacebook;
	
	private void getUIElements() {
		textViewName = (TextView) findViewById(R.id.name);
		textViewPhone = (TextView) findViewById(R.id.phone);
		textViewEmail = (TextView) findViewById(R.id.email);
		textViewNote = (TextView) findViewById(R.id.note);
		imageViewPicture = (ImageView) findViewById(R.id.contact_picture);
		buttonCall = (Button) findViewById(R.id.button_call);
		buttonFacebook = (Button) findViewById(R.id.button_facebook);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		getUIElements();
		
		// Retrieve the contact you need to display
		Bundle b = getIntent().getExtras();
		if(b != null) {
			contact = HomeActivity.persons.get(b.getInt(EXTRAS_CONTACT_POSITION));
		}
		
		textViewName.setText(contact.getName());
		textViewPhone.setText(contact.getPhone());
		textViewEmail.setText(contact.getEmail());
		textViewNote.setText(contact.getNote());
		imageViewPicture.setImageBitmap(contact.getImage());
		
		// Open phone dial
		buttonCall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getPhone()));
				startActivity(i);
			}
			
		});
		
		// Open Facebook profile
		buttonFacebook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(contact.getFacebookProfile()));
				startActivity(i);
			}
			
		});
	}
	
}
