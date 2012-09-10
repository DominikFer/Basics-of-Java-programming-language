package hr.infinum.fer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		ArrayList<Person> persons = new ArrayList<ListActivity.Person>();
		persons.add(new Person("Nikola", "Kapraljevic"));
		persons.add(new Person("Seka", "Aleksic"));
		persons.add(new Person("Rade", "Lackovic"));
		persons.add(new Person("Severina", "Vuckovic"));

		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(new PeopleArrayAdapter(this, persons));
	}

	private class PeopleArrayAdapter extends ArrayAdapter<Person> {
		private ArrayList<Person> people;
		private int layoutId;

		public PeopleArrayAdapter(Context context, ArrayList<Person> persons) {
			super(context, 0);
			this.people = persons;
			this.layoutId = android.R.layout.simple_list_item_2;
		}

		@Override
		public int getCount() {
			return people.size();
		}

		@Override
		public Person getItem(int position) {
			return people.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(layoutId, parent, false);
			}

			TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
			TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);

			text1.setText(getItem(position).getFirstname());
			text2.setText(getItem(position).getLastname());

			return convertView;
		}
	}

	private class Person {
		private String firstname;
		private String lastname;

		public Person(String firstname, String lastname) {
			this.firstname = firstname;
			this.lastname = lastname;
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

	}

}
