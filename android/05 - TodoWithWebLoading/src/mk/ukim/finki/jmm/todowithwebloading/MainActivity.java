package mk.ukim.finki.jmm.todowithwebloading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import mk.ukim.finki.jmm.todowithwebloading.adapters.TodoItemsAdapter;
import mk.ukim.finki.jmm.todowithwebloading.model.TodoItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView mTodoItemsList;
	private TodoItemsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadViews();
		initList();

	}

	public void downloadJson(View view) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);

		Long lastUpdate = settings.getLong("last-update", 0);

		Date now = new Date();
		if (now.getTime() > lastUpdate + 5000) {
			Downloader downloader = new Downloader();
			downloader.execute(getString(R.string.url));

		} else {
			Toast.makeText(this, "Too soon for update!", Toast.LENGTH_LONG)
					.show();
		}

	}

	public void downloadXml(View view) {
		try {
			InputStream is = getStreamFromUrl(getString(R.string.url)
					+ "?format=xml");
			List<TodoItem> items = parseXml(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<TodoItem> loadData(String url) {
		try {
			if (url == null) {
				url = getString(R.string.url);
			}
			String content = getContentFromUrl(url);
			return parseJson(content);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void loadViews() {
		mTodoItemsList = (ListView) findViewById(R.id.todoItems);
	}

	private void initList() {
		mAdapter = new TodoItemsAdapter(this);
		mTodoItemsList.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public List<TodoItem> parseJson(String content) throws JSONException {
		List<TodoItem> items = new ArrayList<TodoItem>();

		JSONArray jsonItems = new JSONArray(content);

		for (int i = 0; i < jsonItems.length(); i++) {
			JSONObject jObj = (JSONObject) jsonItems.get(i);
			TodoItem item = new TodoItem();
			item.setName(jObj.getString("name"));
			item.setId(jObj.getLong("id"));
			item.setDone(jObj.getBoolean("done"));
			items.add(item);
		}
		return items;
	}

	public List<TodoItem> parseXml(InputStream in) throws SAXException,
			IOException, ParserConfigurationException {
		List<TodoItem> items = new ArrayList<TodoItem>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document dom = db.parse(in);
		in.close();

		Element docEle = dom.getDocumentElement();

		NodeList nl = docEle.getElementsByTagName("models.TodoItem");

		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				try {
					// TODO: adapt to the new structure of the xml
					Element entry = (Element) nl.item(i);

					Long id = Long.parseLong(getElementValue(entry, "id"));
					String name = getElementValue(entry, "name");
					Boolean done = Boolean.parseBoolean(getElementValue(entry,
							"done"));

					TodoItem item = new TodoItem(name, done, new Date());
					items.add(item);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return items;
	}

	private String getElementValue(Element entry, String name) {
		NodeList nl = entry.getElementsByTagName(name);
		String res = "";
		if (nl != null && nl.getLength() > 0) {
			Element e = (Element) nl.item(0);
			if (e.hasChildNodes()) {
				res = e.getFirstChild().getNodeValue();
			}
		}

		return res;
	}

	public String getContentFromUrl(String url) throws Exception {
		InputStream is = getStreamFromUrl(url);
		String content;
		if (is != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			content = sb.toString();

			return content;
		}
		return null;

	}

	public InputStream getStreamFromUrl(String url) throws Exception {
		HttpResponse httpResponse = null;
		InputStream is = null;

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		is = httpEntity.getContent();

		if (httpResponse != null
				&& httpResponse.getStatusLine().getStatusCode() == 200) {
			return is;
		}
		return null;

	}

	class Downloader extends AsyncTask<String, Void, List<TodoItem>> {

		private ProgressDialog loadingDialog;

		private void createDialog() {
			loadingDialog = new ProgressDialog(MainActivity.this);
			loadingDialog.setTitle("Downloading");
			loadingDialog
					.setMessage("Please wait while we are downloading the todo items.");
			loadingDialog.setIndeterminate(true);
			loadingDialog.setCancelable(false);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			createDialog();
			loadingDialog.show();
		}

		@Override
		protected List<TodoItem> doInBackground(String... params) {
			List<TodoItem> result = null;
			if (params.length == 1) {
				String url = params[0];
				result = loadData(url);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<TodoItem> result) {
			super.onPostExecute(result);
			mAdapter.addAll(result);
			loadingDialog.dismiss();

			Editor editor = PreferenceManager.getDefaultSharedPreferences(
					MainActivity.this).edit();
			editor.putLong("last-update", new Date().getTime());
			editor.commit();
		}

	}

}
