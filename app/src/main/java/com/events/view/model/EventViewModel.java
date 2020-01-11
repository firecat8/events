package com.events.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.events.entity.Event;
import com.events.repository.EventRepository;

import java.io.Serializable;
import java.util.List;

/**
 * @author gdimitrova
 */
public class EventViewModel extends AndroidViewModel implements Serializable {

    private EventRepository repository;

    private LiveData<List<Event>> events;

    public EventViewModel(@NonNull Application application) {
        super(application);
        repository = new EventRepository(application);
        events = repository.loadAll();
    }

    public void add(Event e) {
        repository.add(e);
    }

    public void update(Event e) {
        repository.update(e);
    }

    public void remove(Event... e) {
        repository.remove(e);
    }

    public LiveData<List<Event>> getAllEvents() {
        return events;
    }

    public LiveData<List<Event>> loadEvents(String text) {
        return repository.loadEvents(text);
    }


}
