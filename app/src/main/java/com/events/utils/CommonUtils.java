package com.events.utils;

import com.events.entity.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CommonUtils {

    public static Event[] convertToArray(List<Event> events) {
        return convertToArray(events, new Event[events.size()]);
    }

    public static <T> T[] convertToArray(Collection<T> entities, T[] arr) {
        int i = 0;
        for (T e : entities) {
            arr[i] = e;
            i++;
        }
        return arr;
    }

    public static String formatDate(Long milliseconds) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date(milliseconds));
    }
    public static Long parseDate(String date) {
        try {
            return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}