package hr.infinum.fer;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button btnDetails = (Button) findViewById(R.id.btnDetails);
		btnDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
				intent.putExtra(DetailsActivity.EXTRAS_NAME, "nixa");
				startActivityForResult(intent, 100);
			}
		});

		Button btnCall = (Button) findViewById(R.id.btnCall);
		btnCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0917700"));
				startActivity(intent);
			}
		});

		Button btnList = (Button) findViewById(R.id.btnList);
		btnList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ListActivity.class);
				startActivity(intent);
			}
		});

		Button btnCamera = (Button) findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivity(intent);
			}
		});

		Button btnAssets = (Button) findViewById(R.id.btnAssets);
		btnAssets.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AssetsActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100 && resultCode == RESULT_OK) {

			Builder b = new Builder(MainActivity.this);
			b.setMessage("Bok " + data.getStringExtra(DetailsActivity.DATA_NAME));
			b.show();
		}
	}

}