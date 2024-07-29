package fpt.vulq.ass2adr2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
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

import fpt.vulq.ass2adr2.R;

public class PersonalInfoFragment extends Fragment {

    private EditText etHeight, etWeight;
    private TextView tvBmiResult, tvBmiAdvice;
    private Button btnCalculateBmi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        etHeight = view.findViewById(R.id.et_height);
        etWeight = view.findViewById(R.id.et_weight);
        tvBmiResult = view.findViewById(R.id.tv_bmi_result);
        tvBmiAdvice = view.findViewById(R.id.tv_bmi_advice);
        btnCalculateBmi = view.findViewById(R.id.btn_calculate_bmi);

        // Set up button click listener
        btnCalculateBmi.setOnClickListener(v -> calculateBmi());
    }

    private void calculateBmi() {
        String heightStr = etHeight.getText().toString();
        String weightStr = etWeight.getText().toString();

        if (TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(weightStr)) {
            tvBmiResult.setText("Please enter both height and weight");
            tvBmiAdvice.setText("");
            return;
        }

        try {
            float height = Float.parseFloat(heightStr) / 100; // Convert cm to meters
            float weight = Float.parseFloat(weightStr);
            float bmi = weight / (height * height);

            tvBmiResult.setText(String.format("Your BMI: %.2f", bmi));
            tvBmiAdvice.setText(getBmiAdvice(bmi));
        } catch (NumberFormatException e) {
            tvBmiResult.setText("Invalid input");
            tvBmiAdvice.setText("");
        }
    }

    private String getBmiAdvice(float bmi) {
        if (bmi < 18.5) {
            return "You are underweight. Consider a balanced diet with higher calorie intake.";
        } else if (bmi < 24.9) {
            return "You have a normal weight. Maintain a balanced diet and regular exercise.";
        } else if (bmi < 29.9) {
            return "You are overweight. Consider a diet and exercise plan to lose weight.";
        } else {
            return "You are obese. Seek advice from a healthcare provider for a suitable diet and exercise plan.";
        }
    }
}