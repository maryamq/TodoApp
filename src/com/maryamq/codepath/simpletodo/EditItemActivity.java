package com.maryamq.codepath.simpletodo;

import java.util.Calendar;

import org.json.JSONException;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class EditItemActivity extends Activity {
	private EditText etEditItem;
	private int position;
	private RatingBar rtPriority;
	private TextView tvDate;
	private boolean hasDate;
	private DatePickerDialog dpDialog;
	private TaskDetails task;

	static final int UPDATE_CODE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		etEditItem = (EditText) this.findViewById(R.id.etEditItem);
		tvDate = (TextView) this.findViewById(R.id.tvDate);
		rtPriority = (RatingBar) this.findViewById(R.id.rtPriority);
		rtPriority.setStepSize(1);
		rtPriority.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
				int color;
				switch((int)rating) {
				case 2:
					color = Color.YELLOW;
					break;
				case 3:
					color = Color.RED;
					break;
				default:
					color = Color.BLUE;
					break;
				}
				LayerDrawable stars = (LayerDrawable) rtPriority.getProgressDrawable();
				stars.getDrawable(2).setColorFilter(color, Mode.SRC_ATOP);
			}
		});
	
		position = getIntent().getIntExtra("position", -1);
		// Extract Task details json.
		String jsonString = getIntent().getStringExtra("task");
		this.task = new TaskDetails(jsonString);
		
		etEditItem.setText(this.task.getTask());
		rtPriority.setRating(this.task.getPriority());
		
        Calendar calendar = this.task.hasDate() ? this.task.getDateTime() : Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        dpDialog= this.getDatePickerDialog(year, month, day, this.tvDate);
        
		if (this.task.hasDate()) {
		  tvDate.setText(getFormattedDateString(year, month, day));
		} else {
			tvDate.setHint("Due Date Unknown");
		}
	}

	private static String getFormattedDateString(int year, int month, int day) {
		return String.format("Due on: %s/%s/%s", year, month, day);
	}
	private DatePickerDialog getDatePickerDialog(int year, int month, int day, final TextView tvDate) {
		return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
		 
            @Override
            public void onDateSet(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
            	tvDate.setText(getFormattedDateString(year, monthOfYear, dayOfMonth));
            	Calendar calendar = task.getDateTime();
            	calendar.set(Calendar.YEAR, year);
            	calendar.set(Calendar.MONTH, monthOfYear);
            	calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            	task.setHasDate(true);
            }
        }, year, month, day);	
	}
	

	public void onSave(View v) {
		Intent data = new Intent();
		String newText = etEditItem.getText().toString();
		int p = (int) Math.ceil(this.rtPriority.getRating());
		task.setTask(newText);
		task.setPriority(p);
		data.putExtra("position", position);
		try {
			data.putExtra("task", task.asJsonObject().toString());
			setResult(RESULT_OK, data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setResult(12, data);
		}						
		finish(); // closes the activity, pass data to parent
	}

	public void onSetDueDate(View v) {
		dpDialog.show();
	}
	
	public void onRemoveDate(View v) {
		tvDate.setText("");
		tvDate.setHint("Due Date Unknown");
		task.setHasDate(false);
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
