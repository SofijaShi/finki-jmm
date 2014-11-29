package mk.ukim.finki.mpip.advancedtodolist.tasks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mk.ukim.finki.mpip.advancedtodolist.parser.Parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class DownloadTask<T> extends AsyncTask<String, Void, T> {

	public static String DOWNLOAD_STARTED = "mk.ukim.finki.mpip.advancedtodolist.DOWNLOAD_STARTED";
	public static String DOWNLOAD_COMPLETED = "mk.ukim.finki.mpip.advancedtodolist.DOWNLOAD_COMPLETED";
	public static String URL_NOT_PROVIDED = "mk.ukim.finki.mpip.advancedtodolist.URL_NOT_PROVIDED";
	public static String EXCEPTION_OCCURED = "mk.ukim.finki.mpip.advancedtodolist.EXCEPTION_OCCURED";

	private Context context;
	private Parser<T> parser;
	private DownloadResultListener<T> listener;

	public DownloadTask(Context context, Parser<T> parser,
			DownloadResultListener<T> listener) {
		super();
		this.context = context;
		this.parser = parser;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		context.sendBroadcast(new Intent(DOWNLOAD_STARTED));
		super.onPreExecute();
	}

	@Override
	protected T doInBackground(String... params) {
		if (params.length > 0) {
			String url = params[0];
			try {
				String content = getContentFromUrl(url);
				return parser.parse(content);
			} catch (Exception e) {
				context.sendBroadcast(new Intent(EXCEPTION_OCCURED));
				e.printStackTrace();
				return null;
			}
		} else {
			context.sendBroadcast(new Intent(URL_NOT_PROVIDED));
			return null;
		}
	}

	@Override
	protected void onPostExecute(T result) {
		listener.onDownloadResult(result);
		super.onPostExecute(result);
		context.sendBroadcast(new Intent(DOWNLOAD_COMPLETED));
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
		if (httpResponse != null) {
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				is = httpEntity.getContent();

				return is;
			}
		}
		return null;

	}

}
