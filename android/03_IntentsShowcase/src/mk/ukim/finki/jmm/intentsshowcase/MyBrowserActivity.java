package mk.ukim.finki.jmm.intentsshowcase;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MyBrowserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView tv = new TextView(this);

		tv.setText(getIntent().getDataString());

		setContentView(tv);
	}

}
