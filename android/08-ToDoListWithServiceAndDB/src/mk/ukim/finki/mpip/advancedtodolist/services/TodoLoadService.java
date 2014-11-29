package mk.ukim.finki.mpip.advancedtodolist.services;

import java.util.List;

import mk.ukim.finki.mpip.advancedtodolist.R;
import mk.ukim.finki.mpip.advancedtodolist.db.adapters.ToDoItemAdapter;
import mk.ukim.finki.mpip.advancedtodolist.model.TodoItem;
import mk.ukim.finki.mpip.advancedtodolist.parser.Parser;
import mk.ukim.finki.mpip.advancedtodolist.parser.impl.TodoItemParser;
import mk.ukim.finki.mpip.advancedtodolist.providers.ToDoContentProvider;
import mk.ukim.finki.mpip.advancedtodolist.tasks.DownloadResultListener;
import mk.ukim.finki.mpip.advancedtodolist.tasks.DownloadTask;
import android.app.IntentService;
import android.content.Intent;

public class TodoLoadService extends IntentService implements
		DownloadResultListener<List<TodoItem>> {

	private Parser<List<TodoItem>> parser;
	private ToDoItemAdapter dbAdapter;

	public TodoLoadService() {
		super("todo-service");

		parser = new TodoItemParser();
		dbAdapter = new ToDoItemAdapter();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		DownloadTask<List<TodoItem>> downloadTodoItemTask = new DownloadTask<List<TodoItem>>(
				this, parser, this);

		downloadTodoItemTask.execute(getString(R.string.url));
	}

	@Override
	public void onDownloadResult(List<TodoItem> result) {
		if (result != null) {
			for (TodoItem item : result) {
				item.setId(null);
				getContentResolver().insert(ToDoContentProvider.CONTENT_URI,
						dbAdapter.itemToContentValues(item));
			}
		}

	}

}
