package mk.ukim.finki.jmm.todolistwithdb;

import java.util.Date;

import mk.ukim.finki.jmm.todolistwithdb.adapters.TodoItemsAdapter;
import mk.ukim.finki.jmm.todolistwithdb.contentprovider.ToDoContentProvider;
import mk.ukim.finki.jmm.todolistwithdb.db.ToDoDao;
import mk.ukim.finki.jmm.todolistwithdb.db.ToDoDbOpenHelper;
import mk.ukim.finki.jmm.todolistwithdb.model.TodoItem;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TodoList extends Activity implements LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;
	TodoItemsAdapter mAdapter;
	ListView mTodoItemsList;
	ToDoDao dao;

	private void loadViews() {
		mTodoItemsList = (ListView) findViewById(R.id.todoItems);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadViews();

		dao = new ToDoDao(this);

	}

	public void createRandom(View view) {
		TodoItem item = new TodoItem();
		item.setDueDate(new Date());
		item.setName("Task : " + item.getDueDate().getTime() / 1000000);
		dao.insert(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		dao.open();
		// loadData();
		fillData();
	}

	@Override
	protected void onStop() {
		super.onStop();
		dao.close();
	}

	private void loadData() {

		mAdapter = new TodoItemsAdapter(dao.getAllItems(), this);
		mTodoItemsList.setAdapter(mAdapter);

	}

	private void fillData() {

		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] { ToDoDbOpenHelper.COLUMN_NAME };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.label };

		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.todo_raw, null, from,
				to, 0);

		// setListAdapter(adapter);
		mTodoItemsList.setAdapter(adapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
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
	public void onLoaderReset(Loader<Cursor> arg0) {
		// data is not available anymore, delete reference
		adapter.swapCursor(null);
	}

}
