package mk.ukim.finki.jmm.intentsshowcase;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String MY_KEY = "myKey";
	public static final String ACTION_IMPLICIT = "mk.ukim.finki.jmm.intentsshowcase.ACTION_IMPLICIT";

	private EditText mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mData = (EditText) findViewById(R.id.data);
	}

	public void explicit(View view) {
		String data = mData.getText().toString();
		Intent intent = new Intent(this, ExplicitActivity.class);
		intent.putExtra(MY_KEY, data);

		startActivity(intent);
	}

	public void implicit(View view) {
		String data = mData.getText().toString();

		Intent intent = new Intent(ACTION_IMPLICIT);
		intent.putExtra(MY_KEY, data);

		startActivity(intent);

	}

	public void shareIntent(View view) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(android.content.Intent.EXTRA_TEXT, "News for you!");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject!");
		startActivity(intent);
	}

	public void showFinkiHomePage(View view) {
		String finkiUrl = "http1://finki.ukim.mk";
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(finkiUrl));
		if (isIntentAvailable(this, intent)) {
			startActivity(intent);
		} else {
			Toast.makeText(this, "No activity for intent: " + intent,
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Action for retrieving a picture for the gallery
	 * 
	 * @param view
	 */
	public void selectPicture(View view) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				1010);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1010) {
				Uri selectedImageUri = data.getData();
				String selectedImagePath = selectedImageUri.toString();
				Toast.makeText(this, selectedImagePath, Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	public static boolean isIntentAvailable(Context ctx, Intent intent) {
		final PackageManager mgr = ctx.getPackageManager();
		List<ResolveInfo> list = mgr.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo info : list) {
			Log.i("IntentShowcase", info.activityInfo.name);
		}
		return list.size() > 0;
	}
}
