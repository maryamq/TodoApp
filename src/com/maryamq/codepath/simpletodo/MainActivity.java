package com.maryamq.codepath.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

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
import android.widget.Toast;

public class MainActivity extends Activity {
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvItems = (ListView) this.findViewById(R.id.lvItems);
		etNewItem = (EditText) this.findViewById(R.id.etNewItem);
		readItems();
		todoAdapter = new ArrayAdapter<String>(getBaseContext(),
				android.R.layout.simple_list_item_1, todoItems);
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
			String text = data.getStringExtra("text");
			this.todoItems.set(position, text);
			this.todoAdapter.notifyDataSetChanged();
			this.writeItems();
			this.showToast("Item updated.");

			// Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
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
				i.putExtra("text", todoItems.get(position));
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
		File fileDir = getFilesDir();
		File todoFile = new File(fileDir, "todo.txt");
		try {
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			todoItems = new ArrayList<String>();
		}
	}

	private void writeItems() {
		File fileDir = getFilesDir();
		File todoFile = new File(fileDir, "todo.txt");
		try {
			FileUtils.writeLines(todoFile, todoItems);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onAddedItem(View v) {
		String text = etNewItem.getText().toString();
		etNewItem.setText("");
		todoAdapter.add(text);
		writeItems();
		this.showToast("Item added");
	}

}
