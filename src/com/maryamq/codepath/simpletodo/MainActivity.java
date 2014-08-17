package com.maryamq.codepath.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ArrayList<TaskDetails> todoItems;
	private ArrayAdapter<TaskDetails> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvItems = (ListView) this.findViewById(R.id.lvItems);
		etNewItem = (EditText) this.findViewById(R.id.etNewItem);

		readItems();
		todoAdapter = new TasksAdapter(getBaseContext(), todoItems);
		// Attach the adapter
		lvItems.setAdapter(todoAdapter);
		setupListViewListener();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK
				&& requestCode == EditItemActivity.UPDATE_CODE) {
			// Extract name value from result extras
			int position = data.getIntExtra("position", -1);
			if (position == -1) {
				// error
				throw new RuntimeException("Position is -1");
			}
			String json = data.getStringExtra("task");
			TaskDetails newTask = new TaskDetails(json);
			this.todoItems.remove(position);
			this.todoItems.add(position, newTask);
			
			this.todoAdapter.notifyDataSetChanged();
			this.writeItems();
			this.showToast("Item updated.");
		}
	}

	private void showToast(String text) {
		Toast toast = Toast.makeText(this.getBaseContext(), text,
				Toast.LENGTH_SHORT);
		toast.show();
	}

	private void setupListViewListener() {
		lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(MainActivity.this, EditItemActivity.class);
				i.putExtra("position", position);
				// Pass the encoded task object.
				try {
					i.putExtra("task", todoItems.get(position).asJsonObject().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivityForResult(i, EditItemActivity.UPDATE_CODE);
			}
		});
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View item,
					int position, long id) {
				todoItems.remove(position);
				writeItems();
				todoAdapter.notifyDataSetChanged();
				showToast("Item deleted");
				return true;
			}

		});
	}

	private void readItems() {
		todoItems = new ArrayList<TaskDetails>();
		File fileDir = getFilesDir();
		File todoFile = new File(fileDir, "todo.json");
		ArrayList<String> jsonStrings = new ArrayList<String>();
		try { 
			jsonStrings = new ArrayList<String>(FileUtils.readLines(todoFile));
			todoItems = TaskDetails.fromJson(jsonStrings);
		}  catch (IOException e) { 
			todoItems.clear();
		}
	}

	private void writeItems() {
		File fileDir = getFilesDir();
		File todoFile = new File(fileDir, "todo.json");
	    try { 
	    	FileUtils.writeLines(todoFile, todoItems); 
	    }catch (IOException e) {
	    	e.printStackTrace(); 
	    	this.showToast("Unable to save changes");
	    }
	}

	public void onAddedItem(View v) {
		String text = etNewItem.getText().toString();
		etNewItem.setText("");
		TaskDetails task = new TaskDetails();
		task.setTask(text);
		task.setPriority(1);
		todoAdapter.add(task);
		writeItems();
		this.showToast("Item added");
	}

}
