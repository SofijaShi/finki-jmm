package mk.ukim.finki.jmm.hellotodolist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoList extends Activity {

	private EditText mNewTask = null;
	private ListView mTasks = null;
	private ArrayAdapter<String> mAdapter = null;

	private List<String> items = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list);

		mNewTask = (EditText) findViewById(R.id.newTask);
		mTasks = (ListView) findViewById(R.id.todoItems);
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);

		mTasks.setAdapter(mAdapter);

	}

	public void openFragmentActivity(View view) {
		Intent intent = new Intent(this, TodoListWithFragments.class);
		this.startActivity(intent);
	}

	public void addTask(View view) {

		String text = mNewTask.getText().toString();

		items.add(text);

		mAdapter.notifyDataSetChanged();

	}

}
