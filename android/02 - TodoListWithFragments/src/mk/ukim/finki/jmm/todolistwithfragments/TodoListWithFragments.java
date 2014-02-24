package mk.ukim.finki.jmm.todolistwithfragments;

import java.util.ArrayList;

import mk.ukim.finki.jmm.todolistwithfragments.R;
import mk.ukim.finki.jmm.todolistwithfragments.fragments.ToDoListFragment;
import mk.ukim.finki.jmm.todolistwithfragments.listeners.OnNewItemAddedListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ArrayAdapter;

public class TodoListWithFragments extends FragmentActivity implements
		OnNewItemAddedListener {

	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> mTodoItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate your view
		setContentView(R.layout.activity_todo_list_with_fragments);

		// Get references to the Fragments
		// for API versions >= 11 use getFragmentManager()
		FragmentManager fm = getSupportFragmentManager();

		ToDoListFragment todoListFragment = (ToDoListFragment) fm
				.findFragmentById(R.id.TodoListFragment);

		// Create the array list of to do items
		mTodoItems = new ArrayList<String>();

		// Create the array adapter to bind the array to the listview
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mTodoItems);

		// Bind the array adapter to the listview.
		todoListFragment.setListAdapter(mAdapter);
	}

	public void onNewItemAdded(String newItem) {
		mTodoItems.add(newItem);
		mAdapter.notifyDataSetChanged();
	}
}
