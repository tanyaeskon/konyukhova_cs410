package edu.pdx.cs.joy.tk24;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddAppointmentActivity extends AppCompatActivity {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void addAppointment(View view){
       EditText ownerField = findViewById(R.id.owner);
       EditText descField = findViewById(R.id.desc);
       EditText beginField = findViewById(R.id.begin);
       EditText endField = findViewById(R.id.end);

        String owner = ownerField.getText().toString().trim();
        String description = descField.getText().toString().trim();
        String beginStr = beginField.getText().toString().trim();
        String endStr = endField.getText().toString().trim();

        if (owner.isEmpty() || description.isEmpty() || beginStr.isEmpty() || endStr.isEmpty()) {
           new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Error").setMessage("All fields are required.")
                    .setPositiveButton("OK", null).show();
            return;
       }

       try {
           LocalDateTime beginTime = LocalDateTime.parse(beginStr, FORMATTER);
           LocalDateTime endTime = LocalDateTime.parse(endStr, FORMATTER);

           Appointment appointment = new Appointment(description, beginTime, endTime);

            AppointmentRepository repo = new AppointmentRepository(this, owner);
            AppointmentBook book = repo.load(owner);
            book.addAppointment(appointment);
            repo.save(book);

           getSharedPreferences("prefs", MODE_PRIVATE)
                   .edit().putString("lastOwner", owner).apply();


           new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Appointment added successfully!")
                    .setPositiveButton("OK", (dialog, which) -> finish()) // closes activity
                    .show();

        } catch (DateTimeParseException e) {
            showError("Invalid date/time format. Use MM/dd/yyyy h:mm AM/PM");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage()); // e.g., End time before begin time
        } catch (Exception e) {
            showError("Error saving appointment: " + e.getMessage());
        }

    }

    private void showError(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

}
