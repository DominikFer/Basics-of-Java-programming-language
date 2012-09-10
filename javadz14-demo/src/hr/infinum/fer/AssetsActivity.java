package hr.infinum.fer;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class AssetsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.assets);

		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		AssetManager am = getAssets();
		try {
			InputStream open = am.open("image1.jpeg");
			Bitmap bmpImage = BitmapFactory.decodeStream(open);
			imageView.setImageBitmap(bmpImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
