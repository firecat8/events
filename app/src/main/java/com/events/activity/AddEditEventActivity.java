/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.events.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.events.EventFactory;
import com.events.R;
import com.events.entity.Event;
import com.events.utils.CommonUtils;
import com.events.view.model.EventViewModel;

/**
 * @author gdimitrova
 */
public class AddEditEventActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.events.EXTRA_ID";
    public static final String EXTRA_EVENT =
            "com.events.EXTRA_EVENT";
    private static final String ADD_EVENT =
            "Add Event";
    private static final String EDIT_EVENT =
            "Edit Event";
    private Context context;
    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextLocation;
    private EditText textStartEventTime;
    private EditText textEndEventTime;
    private Event selectedEvent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        context = this;
        startTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.SECOND, 0);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextLocation = findViewById(R.id.edit_text_location);
        textStartEventTime = findViewById(R.id.text_start_event_time);
        textEndEventTime = findViewById(R.id.text_end_event_time);


        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle(EDIT_EVENT);
            fillEditTexts((Event) intent.getSerializableExtra(EXTRA_EVENT));
            return;
        }
        selectedEvent = null;
        textStartEventTime.setText(CommonUtils.formatDate(startTime.getTimeInMillis()));
        textEndEventTime.setText(CommonUtils.formatDate(endTime.getTimeInMillis()));
        setTitle(ADD_EVENT);
    }

    private void fillEditTexts(Event selectedEvent) {
        editTextTitle.setText(selectedEvent.getTitle());
        editTextDescription.setText(selectedEvent.getDescription());
        editTextLocation.setText(selectedEvent.getLocation());
        textStartEventTime.setText(CommonUtils.formatDate(selectedEvent.getStartEventTime()));
        textEndEventTime.setText(CommonUtils.formatDate(selectedEvent.getEndEventTime()));
    }


    private Event makeEvent(String title, String description, String location, Long startEventTime, Long endEventTime) {
        return EventFactory.makeEvent(title, description, location, startEventTime, endEventTime);
    }

    private Long convertToDate(EditText editText) {
        String date = editText.getText().toString();
        Long dateLong = CommonUtils.parseDate(date);
        return dateLong;
    }

    private Calendar convertToCalendar(EditText editText) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(convertToDate(editText));
        return cal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ready:
                onReadyClickEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onReadyClickEvent() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String location = editTextLocation.getText().toString();

        Long start =  convertToDate(textStartEventTime);
        Long end =  convertToDate(textEndEventTime);
        if (title.trim().isEmpty() || description.trim().isEmpty() || location.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title, description and location.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (start > end) {
            Toast.makeText(this, "Start date must be before end date.", Toast.LENGTH_SHORT).show();
            return;
        }

        Event e = makeEvent(
                editTextTitle.getText().toString(),
                editTextDescription.getText().toString(),
                editTextLocation.getText().toString(),
                start,
                end);
        createIntent(e);
    }

    private void createIntent(Event event) {
        Intent data = new Intent();
        data.putExtra(EXTRA_EVENT, event);
        Long id = getIntent().getLongExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
            event.setId(id);
        }

        setResult(RESULT_OK, data);
        finish();
    }


    public void selectStartEventTime(View v) {
        selectEventTime(convertToCalendar(textEndEventTime), textStartEventTime);
    }

    public void selectEndEventTime(View v) {
        selectEventTime(convertToCalendar(textEndEventTime), textEndEventTime);
    }

    public void selectEventTime(final Calendar eventTime, final EditText editText) {
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                eventTime.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        eventTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        eventTime.set(Calendar.MINUTE, minute);
                        eventTime.set(Calendar.SECOND, 0);
                        editText.setText(CommonUtils.formatDate(eventTime.getTimeInMillis()));
                    }
                }, eventTime.get(Calendar.HOUR_OF_DAY), eventTime.get(Calendar.MINUTE), false).show();
            }
        }, eventTime.get(Calendar.YEAR), eventTime.get(Calendar.MONTH), eventTime.get(Calendar.DATE)).show();
    }
}