package hr.infinum.fer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends Activity {

	public static final String EXTRAS_NAME = "extras_name";
	public static final String DATA_NAME = "data_name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);

		String name = getIntent().getExtras().getString(EXTRAS_NAME);

		TextView textName = (TextView) findViewById(R.id.textName);
		textName.setText(name);

		final TextView editText = (TextView) findViewById(R.id.editText);

		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra(DATA_NAME, editText.getText() + "");
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
}
