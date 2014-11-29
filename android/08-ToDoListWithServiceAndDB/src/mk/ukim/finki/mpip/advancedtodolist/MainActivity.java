package mk.ukim.finki.mpip.advancedtodolist;

import java.util.Date;

import mk.ukim.finki.mpip.advancedtodolist.db.DatabaseItemsAdapter;
import mk.ukim.finki.mpip.advancedtodolist.db.ToDoDbOpenHelper;
import mk.ukim.finki.mpip.advancedtodolist.db.adapters.ToDoItemAdapter;
import mk.ukim.finki.mpip.advancedtodolist.model.TodoItem;
import mk.ukim.finki.mpip.advancedtodolist.providers.ToDoContentProvider;
import mk.ukim.finki.mpip.advancedtodolist.services.TodoLoadService;
import mk.ukim.finki.mpip.advancedtodolist.tasks.DownloadTask;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		LoaderCallbacks<Cursor> {

	private ListView mTodoItemsList;
	private SimpleCursorAdapter adapter;
	private DatabaseItemsAdapter<TodoItem> dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadViews();
		fillData();

		dbAdapter = new ToDoItemAdapter();
	}

	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadTask.DOWNLOAD_COMPLETED);
		filter.addAction(DownloadTask.DOWNLOAD_STARTED);
		filter.addAction(DownloadTask.EXCEPTION_OCCURED);
		filter.addAction(DownloadTask.URL_NOT_PROVIDED);
		registerReceiver(statusReceiver, filter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(statusReceiver);
	}

	private void loadViews() {
		mTodoItemsList = (ListView) findViewById(R.id.todoItems);
	}

	public void createRandom(View view) {
		TodoItem item = new TodoItem();
		item.setDueDate(new Date());
		item.setName("Task : " + item.getDueDate().getTime() / 1000000);

		getContentResolver().insert(ToDoContentProvider.CONTENT_URI,
				dbAdapter.itemToContentValues(item));

		startService(new Intent(this, TodoLoadService.class));
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { ToDoDbOpenHelper.COLUMN_ID,
				ToDoDbOpenHelper.COLUMN_NAME };
		CursorLoader cursorLoader = new CursorLoader(this,
				ToDoContentProvider.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	private void fillData() {

		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] { ToDoDbOpenHelper.COLUMN_NAME };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.label };

		Bundle args = null;
		getLoaderManager().initLoader(0, args, this);
		adapter = new SimpleCursorAdapter(this, R.layout.todo_raw, null, from,
				to, 0);

		mTodoItemsList.setAdapter(adapter);
	}

	BroadcastReceiver statusReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG)
					.show();

		}

	};

}
