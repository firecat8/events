package com.events.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.events.R;
import com.events.adapter.EventAdapter;
import com.events.adapter.OnItemClickListener;
import com.events.entity.Event;
import com.events.utils.CommonUtils;
import com.events.view.model.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_EVENT_REQUEST = 1;

    public static final int EDIT_EVENT_REQUEST = 2;

    private EventViewModel eventViewModel;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new EventAdapter(new ArrayList<Event>());
        recyclerView.setAdapter(adapter);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                adapter.notifyDataSetChanged();
                adapter.setEvents(events);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                eventViewModel.remove(adapter.getEventAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Event deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        FloatingActionButton buttonAddEvent = findViewById(R.id.button_add_event);
        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditEventActivity.class);
                startActivityForResult(intent, ADD_EVENT_REQUEST);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                Intent intent = new Intent(MainActivity.this, AddEditEventActivity.class);
                intent.putExtra(AddEditEventActivity.EXTRA_ID, event.getId());
                intent.putExtra(AddEditEventActivity.EXTRA_EVENT, event);;
                startActivityForResult(intent, EDIT_EVENT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Long id = data.getLongExtra(AddEditEventActivity.EXTRA_ID, -1);
        Event event = (Event) data.getSerializableExtra(AddEditEventActivity.EXTRA_EVENT);
        if (requestCode == ADD_EVENT_REQUEST && resultCode == RESULT_OK) {

            eventViewModel.add(event);

            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == EDIT_EVENT_REQUEST && resultCode == RESULT_OK) {
            if (id == -1) {
                Toast.makeText(this, "Event can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            eventViewModel.update(event);

            Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "No event for the event", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchEvents(s);
                return false;
            }


        });
        return true;
    }


    private void searchEvents(String searchString) {

        eventViewModel.loadEvents("%" + searchString + "%").observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                adapter.setEvents(events);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_events:
                eventViewModel.remove(CommonUtils.convertToArray(adapter.getEvents()));
                Toast.makeText(this, "Events are deleted", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
