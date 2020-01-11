package com.events.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.events.entity.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void add(Event e);

    @Update
    void update(Event e);

    @Delete
    void remove(Event... e);

    @Query("SELECT * FROM  events ")
    LiveData<List<Event>> loadAll();

    @Query("SELECT * FROM events WHERE title LIKE :text OR description LIKE :text")
    LiveData<List<Event>> loadEvents(String text);
}
