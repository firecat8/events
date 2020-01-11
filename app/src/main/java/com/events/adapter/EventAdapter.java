package com.events.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.events.R;
import com.events.entity.Event;
import com.events.utils.CommonUtils;

import java.util.List;

/**
 * @author gdimitrova
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {

    private OnItemClickListener listener;
    private List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        Event currentEvent = getEventAt(position);
        holder.textViewTitle.setText(currentEvent.getTitle());
        holder.textViewDescription.setText(currentEvent.getDescription());
        holder.textViewLocation.setText(currentEvent.getLocation());
        holder.textViewStartEventTime.setText(CommonUtils.formatDate(currentEvent.getStartEventTime()));
    }

    public Event getEventAt(int pos) {
        return events.get(pos);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    class EventHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;

        private TextView textViewDescription;
        private TextView textViewLocation;
        private TextView textViewStartEventTime;


        public EventHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewLocation = itemView.findViewById(R.id.text_view_location);
            textViewStartEventTime = itemView.findViewById(R.id.text_view_start_event_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getEventAt(position));
                    }
                }
            });
        }
    }
}
