package com.events.database;

import android.content.Context;
import android.icu.util.GregorianCalendar;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.events.EventFactory;
import com.events.dao.EventDao;
import com.events.entity.Event;

@Database(entities = {Event.class}, version = 7, exportSchema = false)
public abstract class EventDatabase extends RoomDatabase {

    private static EventDatabase instance;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    public static synchronized EventDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), EventDatabase.class, "events_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public abstract EventDao getEventDao();


    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao eventDao;

        private PopulateDbAsyncTask(EventDatabase db) {
            eventDao = db.getEventDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eventDao.add(EventFactory.makeEvent("LinkinPark tribute", "LinkinPark tribute...", "Rubik, Varna", getTime(2012, 12, 1, 21, 0), getTime(2012, 12, 1, 23, 59)));
            eventDao.add(EventFactory.makeEvent("Varna LIVE Tributes", "LinkinPark, Metallica and more tributes", "Varna LIVE, Varna", getTime(2015, 6, 1, 9, 0), getTime(2015, 6, 1, 21, 0)));
            eventDao.add(EventFactory.makeEvent("LinkinPark tribute", "LinkinPark tribute...", "Varna LIVE, Varna", getTime(2017, 10, 1, 21, 0), getTime(2012, 12, 1, 23, 59)));
            eventDao.add(EventFactory.makeEvent("Art gallery open", "Art gallery open...", "Graffit gallery, Varna", getTime(2015, 6, 1, 9, 0), getTime(2015, 6, 1, 21, 0)));
            return null;
        }

        private Long getTime(int year, int month, int date, int hour, int minute) {
            return new GregorianCalendar(year, month, date, hour, minute, 0).getTimeInMillis();
        }
    }

}
