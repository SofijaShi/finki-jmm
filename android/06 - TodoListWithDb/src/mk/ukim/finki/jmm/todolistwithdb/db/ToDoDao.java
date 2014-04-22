package mk.ukim.finki.jmm.todolistwithdb.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mk.ukim.finki.jmm.todolistwithdb.model.TodoItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ToDoDao {
	// Database fields
	private SQLiteDatabase database;
	private ToDoDbOpenHelper dbHelper;
	private String[] allColumns = { ToDoDbOpenHelper.COLUMN_ID,
			ToDoDbOpenHelper.COLUMN_NAME, ToDoDbOpenHelper.COLUMN_DONE,
			ToDoDbOpenHelper.COLUMN_DUE_DATE };

	public ToDoDao(Context context) {
		dbHelper = new ToDoDbOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
		dbHelper.close();
	}

	public boolean insert(TodoItem item) {

		if (item.getId() != null) {
			return update(item);
		}

		long insertId = database.insert(ToDoDbOpenHelper.TABLE_NAME, null,
				itemToContentValues(item));

		if (insertId > 0) {
			item.setId(insertId);
			return true;
		} else {
			return false;
		}

	}

	public boolean update(TodoItem item) {
		long numRowsAffected = database.update(ToDoDbOpenHelper.TABLE_NAME,
				itemToContentValues(item), ToDoDbOpenHelper.COLUMN_ID + " = "
						+ item.getId(), null);
		return numRowsAffected > 0;
	}

	public List<TodoItem> getAllItems() {
		List<TodoItem> items = new ArrayList<TodoItem>();

		Cursor cursor = database.query(ToDoDbOpenHelper.TABLE_NAME, allColumns,
				null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				items.add(cursorToItem(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return items;
	}

	public TodoItem getById(long id) {

		Cursor cursor = database
				.query(ToDoDbOpenHelper.TABLE_NAME, allColumns,
						ToDoDbOpenHelper.COLUMN_ID + " = " + id, null, null,
						null, null);
		try {
			if (cursor.moveToFirst()) {
				return cursorToItem(cursor);
			} else {
				// no items found
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			cursor.close();
		}

	}

	protected TodoItem cursorToItem(Cursor cursor) {
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

	protected ContentValues itemToContentValues(TodoItem item) {
		ContentValues values = new ContentValues();
		if (item.getId() != null) {
			values.put(ToDoDbOpenHelper.COLUMN_ID, item.getId());
		}
		values.put(ToDoDbOpenHelper.COLUMN_NAME, item.getName());
		values.put(ToDoDbOpenHelper.COLUMN_DONE, item.isDone() ? 1 : 0);
		values.put(ToDoDbOpenHelper.COLUMN_DUE_DATE, item.getDueDate()
				.getTime());
		return values;
	}
}
