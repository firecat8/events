package com.events.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "events")
public class Event implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String title;
    private String description;
    private String location;
    @ColumnInfo(name = "start_event_time")
    private Long startEventTime;
    @ColumnInfo(name = "end_event_time")
    private Long endEventTime;

    public Event(Long id, String title, String description, String location, Long startEventTime, Long endEventTime) {
        this(title, description, location, startEventTime, endEventTime);
        this.id = id;
    }

    @Ignore
    public Event(String title, String description, String location, Long startEventTime, Long endEventTime) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.startEventTime = startEventTime;
        this.endEventTime = endEventTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStartEventTime() {
        return startEventTime;
    }

    public void setStartEventTime(Long startEventTime) {
        this.startEventTime = startEventTime;
    }

    public Long getEndEventTime() {
        return endEventTime;
    }

    public void setEndEventTime(Long endEventTime) {
        this.endEventTime = endEventTime;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(title, event.title) &&
                Objects.equals(description, event.description) &&
                Objects.equals(location, event.location) &&
                Objects.equals(startEventTime, event.startEventTime) &&
                Objects.equals(endEventTime, event.endEventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title,  description, location, startEventTime, endEventTime);
    }
}
