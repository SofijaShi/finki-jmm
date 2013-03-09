package mk.ukim.finki.jmm.todolist;

import java.util.Date;

import mk.ukim.finki.jmm.todolist.adapters.TodoItemsAdapter;
import mk.ukim.finki.jmm.todolist.model.TodoItem;
import mk.ukim.finki.jmm.todolist.utils.DatePickerUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TodoList extends Activity {

	static final String ACTION_TODO_DETAILS = "mk.ukim.finki.jmm.todolist.ACTION_TODO_DETAILS";

	private EditText mItemName;
	private DatePicker mItemDueDate;
	private ListView mTodoItemsList;

	private TodoItemsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list);
		loadViews();
		initList();
	}

	/**
	 * Inflates the views from the xml layout
	 */
	private void loadViews() {
		mItemName = (EditText) findViewById(R.id.todoName);
		mItemDueDate = (DatePicker) findViewById(R.id.todoDueDate);
		mTodoItemsList = (ListView) findViewById(R.id.todoItems);
	}

	private void initList() {
		mAdapter = new TodoItemsAdapter(this);
		mTodoItemsList.setAdapter(mAdapter);
		mTodoItemsList.setOnItemClickListener(mAdapter);
		mTodoItemsList.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(TodoList.this, "Item long click",
						Toast.LENGTH_LONG).show();
				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.todo_list, menu);
		return true;
	}

	public void explicit(View view) {
		startActivity(new Intent(this, TodoDetails.class));
	}

	public void implicit(View view) {
		Intent detailsIntent = new Intent(ACTION_TODO_DETAILS);
		detailsIntent.putExtra("time", (new Date()).getTime());
		startActivity(detailsIntent);
	}

	public void addTodoItem(View view) {

		TodoItem item = new TodoItem(mItemName.getText().toString(), false,
				DatePickerUtils.getDate(mItemDueDate));

		mAdapter.add(item);
	}
}
