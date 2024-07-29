package fpt.vulq.ass2adr2;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GratitudeAdapter extends RecyclerView.Adapter<GratitudeAdapter.GratitudeViewHolder> {

    private List<Gratitude> gratitudeList;

    public GratitudeAdapter(List<Gratitude> gratitudeList) {
        this.gratitudeList = gratitudeList;
    }

    @NonNull
    @Override
    public GratitudeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gratitude, parent, false);
        return new GratitudeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GratitudeViewHolder holder, int position) {
        Gratitude gratitude = gratitudeList.get(position);
        holder.contentTextView.setText(gratitude.getContent());
        holder.dateTextView.setText(DateFormat.format("MM/dd/yyyy", gratitude.getDate()));
    }

    @Override
    public int getItemCount() {
        return gratitudeList.size();
    }

    static class GratitudeViewHolder extends RecyclerView.ViewHolder {

        TextView contentTextView, dateTextView;

        public GratitudeViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
