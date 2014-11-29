package mk.ukim.finki.mpip.advancedtodolist.db;

import mk.ukim.finki.mpip.advancedtodolist.model.DatabaseItem;
import android.content.ContentValues;
import android.database.Cursor;

public interface DatabaseItemsAdapter<T extends DatabaseItem> {

	public ContentValues itemToContentValues(T item);

	public T cursorToItem(Cursor c);
}
