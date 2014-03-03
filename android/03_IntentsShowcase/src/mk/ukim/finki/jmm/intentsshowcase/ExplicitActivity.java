package mk.ukim.finki.jmm.intentsshowcase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ExplicitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other);

		TextView tv = (TextView) findViewById(R.id.sentData);

		Intent intent = getIntent();

		String data = intent.getExtras().
				getString(MainActivity.MY_KEY);
		tv.setText(data);

	}

}
