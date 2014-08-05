package com.maryamq.codepath.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	private EditText etEditItem;
	private int position;
	
	static final int UPDATE_CODE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		String text = getIntent().getStringExtra("text");
		position = getIntent().getIntExtra("position", -1);
		etEditItem = (EditText) this.findViewById(R.id.etEditItem);
		etEditItem.setText(text);
		
	}
	

	public void onSave(View v) {
	  Intent data = new Intent();
	  String newText = etEditItem.getText().toString();
	  data.putExtra("text", newText);
	  data.putExtra("position", position);
	 
	  setResult(RESULT_OK, data); // set result code and bundle data for response
	  finish(); // closes the activity, pass data to parent
	} 
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
