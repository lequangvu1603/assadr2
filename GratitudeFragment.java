package fpt.vulq.ass2adr2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GratitudeFragment extends Fragment {

    private EditText gratitudeEditText;
    private Button saveButton;
    private RecyclerView recyclerView;
    private GratitudeAdapter gratitudeAdapter;
    private MyDatabaseHelper dbHelper;
    private List<Gratitude> gratitudeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gratitude, container, false);

        gratitudeEditText = view.findViewById(R.id.gratitudeEditText);
        saveButton = view.findViewById(R.id.saveButton);
        recyclerView = view.findViewById(R.id.recyclerView);

        dbHelper = new MyDatabaseHelper(getActivity());
        gratitudeAdapter = new GratitudeAdapter(gratitudeList);
        recyclerView.setAdapter(gratitudeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGratitude();
            }
        });

        loadGratitudeEntries();

        return view;
    }

    private void saveGratitude() {
        String content = gratitudeEditText.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter gratitude content", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Content", content);
        values.put("Date", System.currentTimeMillis());

        long id = db.insert("Gratitude", null, values);
        if (id != -1) {
            Toast.makeText(getActivity(), "Gratitude saved", Toast.LENGTH_SHORT).show();
            gratitudeEditText.setText("");
            loadGratitudeEntries();
        } else {
            Toast.makeText(getActivity(), "Failed to save gratitude", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadGratitudeEntries() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Gratitude", null, null, null, null, null, "Date DESC");

        gratitudeList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("GratitudeID"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                long date = cursor.getLong(cursor.getColumnIndexOrThrow("Date"));
                gratitudeList.add(new Gratitude(id, content, date, 1));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        gratitudeAdapter.notifyDataSetChanged();
    }
}