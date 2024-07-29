package fpt.vulq.ass2adr2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StepCountFragment extends Fragment {

    private TextView stepCountTextView, goalTextView, statusTextView;
    private Button updateGoalButton;
    private EditText goalEditText;
    private MyDatabaseHelper dbHelper;
    private int userId;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private boolean isCounterSensorPresent;
    private int stepCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_count, container, false);

        stepCountTextView = view.findViewById(R.id.stepCountTextView);
        goalTextView = view.findViewById(R.id.goalTextView);
        statusTextView = view.findViewById(R.id.statusTextView);
        updateGoalButton = view.findViewById(R.id.updateGoalButton);
        goalEditText = view.findViewById(R.id.goalEditText);

        dbHelper = new MyDatabaseHelper(getActivity());
        userId = getArguments().getInt("UserID");

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent = true;
        } else {
            statusTextView.setText("Step Counter Sensor is not available!");
            isCounterSensorPresent = false;
        }

        updateGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGoal();
            }
        });

        loadStepCount();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCounterSensorPresent) {
            sensorManager.registerListener(stepCounterListener, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isCounterSensorPresent) {
            sensorManager.unregisterListener(stepCounterListener);
        }
    }

    private SensorEventListener stepCounterListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            stepCount = (int) event.values[0];
            stepCountTextView.setText(String.valueOf(stepCount));
            updateStepCountInDb(stepCount);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do nothing
        }
    };

    private void loadStepCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("StepCount", null, "UserID=?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            stepCount = cursor.getInt(cursor.getColumnIndexOrThrow("Count"));
            int goal = cursor.getInt(cursor.getColumnIndexOrThrow("Goal"));

            stepCountTextView.setText(String.valueOf(stepCount));
            goalTextView.setText(String.valueOf(goal));
            updateStatus(goal);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void updateStepCountInDb(int stepCount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Count", stepCount);
        values.put("Date", System.currentTimeMillis());
        values.put("UserID", userId);

        long id = db.insertWithOnConflict("StepCount", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (id != -1) {
            int goal = Integer.parseInt(goalTextView.getText().toString());
            updateStatus(goal);
        }
    }

    private void updateGoal() {
        String goalStr = goalEditText.getText().toString().trim();
        if (goalStr.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a goal", Toast.LENGTH_SHORT).show();
            return;
        }

        int goal = Integer.parseInt(goalStr);
        goalTextView.setText(String.valueOf(goal));
        goalEditText.setText("");

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Goal", goal);
        values.put("UserID", userId);

        long id = db.insertWithOnConflict("StepCount", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (id != -1) {
            updateStatus(goal);
        }
    }

    private void updateStatus(int goal) {
        if (stepCount >= goal) {
            statusTextView.setText("Goal achieved!");
        } else {
            statusTextView.setText("Keep going!");
        }
    }
}