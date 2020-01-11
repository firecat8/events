package com.events;

import com.events.entity.Event;

public class EventFactory {

    public static Event makeEvent(String title, String description, String location, Long startEventTime, Long endEventTime) {
        return new Event(title, description, location, startEventTime, endEventTime);
    }
}
