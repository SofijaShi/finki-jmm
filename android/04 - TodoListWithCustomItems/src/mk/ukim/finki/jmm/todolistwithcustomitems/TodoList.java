package mk.ukim.finki.jmm.todolistwithcustomitems;

import mk.ukim.finki.jmm.todolistwithcustomitems.adapters.TodoItemsAdapter;
import mk.ukim.finki.jmm.todolistwithcustomitems.model.TodoItem;
import mk.ukim.finki.jmm.todolistwithcustomitems.utils.DatePickerUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TodoList extends Activity {

	private EditText mItemName;
	private DatePicker mItemDueDate;
	private ListView mTodoItemsList;
	private TodoItemsAdapter mAdapter;
	private boolean done;

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
		mTodoItemsList
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View parent, int position, long id) {
						Toast.makeText(TodoList.this, "Item long click",
								Toast.LENGTH_LONG).show();
						return true;
					}

				});

	}

	public void selectColor(View view) {
		switch (view.getId()) {
		case R.id.red:
			done = false;
			Toast.makeText(this, "set not as finished", Toast.LENGTH_LONG).show();
			break;
		case R.id.green:
			done = true;
			Toast.makeText(this, "set as finished", Toast.LENGTH_LONG)
					.show();
			break;
		default:
			break;
		}
	}

	public void addTodoItem(View view) {

		TodoItem item = new TodoItem(mItemName.getText().toString(), done,
				DatePickerUtils.getDate(mItemDueDate));

		mItemName.setText("");

		mAdapter.add(item);
	}

}
