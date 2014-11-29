package mk.ukim.finki.mpip.advancedtodolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDbOpenHelper extends SQLiteOpenHelper {

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DONE = "done";
	public static final String COLUMN_DUE_DATE = "due_date";
	public static final String TABLE_NAME = "ToDoItems";

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME_EXPRESSION = "ToDoDatabase.db";

	private static final String DATABASE_CREATE = String
			.format("create table %s (%s  integer primary key autoincrement, "
					+ "%s text not null, %s integer default 0, %s datetime not null);",
					TABLE_NAME, COLUMN_ID, COLUMN_NAME, COLUMN_DONE,
					COLUMN_DUE_DATE);

	public ToDoDbOpenHelper(Context context) {
		super(context, String.format(DATABASE_NAME_EXPRESSION), null,
				DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
		onCreate(db);
	}

}
