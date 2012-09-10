package zemris.fer.hr.adapters;

import java.util.List;

import zemris.fer.hr.addressbook.R;
import zemris.fer.hr.models.Contact;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {

	private List<Contact> contacts;
	private int layoutId;

	public ContactAdapter(Context context, List<Contact> persons) {
		super(context, 0);
		this.contacts = persons;
		this.layoutId = R.layout.list_item;
	}

	@Override
	public int getCount() {
		return contacts.size();
	}

	@Override
	public Contact getItem(int position) {
		return contacts.get(position);
	}
	
	@Override
	public void remove(Contact object) {
		if(contacts.contains(object)) {
			contacts.remove(object);
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Contact contact = getItem(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutId, parent, false);
		}
		
		TextView firstRow = (TextView) convertView.findViewById(R.id.first_row);
		firstRow.setText(contact.getName());
		TextView secondRow = (TextView) convertView.findViewById(R.id.second_row);
		secondRow.setText(contact.getPhone() + ", " + contact.getEmail());

		return convertView;
	}
	
}
