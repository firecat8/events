package com.events.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.events.dao.EventDao;
import com.events.database.EventDatabase;
import com.events.entity.Event;

import java.util.List;

public class EventRepository {

    private EventDao eventDao;

    private LiveData<List<Event>> allEvents;

    public EventRepository(Application app) {
        EventDatabase database = EventDatabase.getInstance(app);
        eventDao = database.getEventDao();
        allEvents = eventDao.loadAll();
    }

    public void add(Event e) {

        new AddEventTask(eventDao).execute(e);
    }

    public void update(Event e) {

        new UpdateEventTask(eventDao).execute(e);
    }


    public void remove(Event... e) {

        new RemoveEventTask(eventDao).execute(e);
    }

    public LiveData<List<Event>> loadAll() {
        return allEvents;
    }

    public LiveData<List<Event>> loadEvents(String text) {
        return eventDao.loadEvents(text);

    }


    private static class AddEventTask extends AsyncTask<Event, Void, Void> {

        private EventDao eventDao;

        public AddEventTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        protected Void doInBackground(Event... events) {
            eventDao.add(events[0]);
            return null;
        }
    }

    private static class UpdateEventTask extends AsyncTask<Event, Void, Void> {

        private EventDao eventDao;

        public UpdateEventTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        protected Void doInBackground(Event... events) {
            eventDao.update(events[0]);
            return null;
        }
    }

    private static class RemoveEventTask extends AsyncTask<Event, Void, Void> {

        private EventDao eventDao;

        public RemoveEventTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        protected Void doInBackground(Event... events) {
            eventDao.remove(events);
            return null;
        }
    }
}
