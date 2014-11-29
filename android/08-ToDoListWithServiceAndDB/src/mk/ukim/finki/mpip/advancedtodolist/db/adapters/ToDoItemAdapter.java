package mk.ukim.finki.mpip.advancedtodolist.db.adapters;

import java.util.Date;

import mk.ukim.finki.mpip.advancedtodolist.db.DatabaseItemsAdapter;
import mk.ukim.finki.mpip.advancedtodolist.db.ToDoDbOpenHelper;
import mk.ukim.finki.mpip.advancedtodolist.model.TodoItem;
import android.content.ContentValues;
import android.database.Cursor;

public class ToDoItemAdapter implements DatabaseItemsAdapter<TodoItem> {

	@Override
	public ContentValues itemToContentValues(TodoItem item) {
		ContentValues values = new ContentValues();
		if (item.getId() != null) {
			values.put(ToDoDbOpenHelper.COLUMN_ID, item.getId());
		}
		values.put(ToDoDbOpenHelper.COLUMN_NAME, item.getName());
		values.put(ToDoDbOpenHelper.COLUMN_DONE, item.isDone() ? 1 : 0);
		if (item.getDueDate() != null) {
			values.put(ToDoDbOpenHelper.COLUMN_DUE_DATE, item.getDueDate()
					.getTime());
		} else {
			values.put(ToDoDbOpenHelper.COLUMN_DUE_DATE, new Date().getTime());
		}
		return values;
	}

	@Override
	public TodoItem cursorToItem(Cursor cursor) {
		TodoItem item = new TodoItem();
		item.setId(cursor.getLong(cursor
				.getColumnIndex(ToDoDbOpenHelper.COLUMN_ID)));

		item.setName(cursor.getString(cursor
				.getColumnIndex(ToDoDbOpenHelper.COLUMN_NAME)));

		item.setDone(1 == cursor.getInt((cursor
				.getColumnIndex(ToDoDbOpenHelper.COLUMN_DONE))));

		item.setDueDate(new Date(cursor.getLong(cursor
				.getColumnIndex(ToDoDbOpenHelper.COLUMN_DUE_DATE))));

		return item;
	}

}
