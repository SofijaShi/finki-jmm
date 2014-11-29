package mk.ukim.finki.mpip.advancedtodolist.providers;

import java.util.Arrays;
import java.util.HashSet;

import mk.ukim.finki.mpip.advancedtodolist.db.ToDoDbOpenHelper;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ToDoContentProvider extends ContentProvider {

	private ToDoDbOpenHelper helper;
	// used for the UriMacher
	private static final int TODO_MULTIPLE = 10;
	private static final int TODO_SINGLE = 20;

	private static final String AUTHORITY = "mk.ukim.finki.mpip.advancedtodolist.contentprovider";

	private static final String BASE_PATH = "todos";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, TODO_MULTIPLE);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODO_SINGLE);

	}

	@Override
	public boolean onCreate() {
		helper = new ToDoDbOpenHelper(getContext());
		return false;
	}

	@Override
	public String getType(Uri uri) {
		switch (sURIMatcher.match(uri)) {
		case TODO_MULTIPLE:
			return "vnd.android.cursor.dir/vnd.finki.todo";
		case TODO_SINGLE:
			return "vnd.android.cursor.item/vnd.finki.todo";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(ToDoDbOpenHelper.TABLE_NAME);
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TODO_MULTIPLE:
			break;
		case TODO_SINGLE:
			// adding the ID to the original query
			queryBuilder.appendWhere(ToDoDbOpenHelper.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = helper.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case TODO_MULTIPLE:
			id = sqlDB.insert(ToDoDbOpenHelper.TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = helper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case TODO_MULTIPLE:
			rowsUpdated = sqlDB.update(ToDoDbOpenHelper.TABLE_NAME, values,
					selection, selectionArgs);
			break;
		case TODO_SINGLE:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(ToDoDbOpenHelper.TABLE_NAME, values,
						ToDoDbOpenHelper.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(ToDoDbOpenHelper.TABLE_NAME, values,
						ToDoDbOpenHelper.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = helper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case TODO_MULTIPLE:
			rowsDeleted = sqlDB.delete(ToDoDbOpenHelper.TABLE_NAME, selection,
					selectionArgs);
			break;
		case TODO_SINGLE:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(ToDoDbOpenHelper.TABLE_NAME,
						ToDoDbOpenHelper.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(ToDoDbOpenHelper.TABLE_NAME,
						ToDoDbOpenHelper.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	private void checkColumns(String[] projection) {
		String[] available = { ToDoDbOpenHelper.COLUMN_ID,
				ToDoDbOpenHelper.COLUMN_NAME, ToDoDbOpenHelper.COLUMN_DUE_DATE,
				ToDoDbOpenHelper.COLUMN_DONE };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}
}
