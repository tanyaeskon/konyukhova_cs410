package edu.pdx.cs.joy.tk24;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class SearchActivity extends AppCompatActivity {

    private EditText ownerEditText;
    private TextView resultsTextView;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ownerEditText = findViewById(R.id.edittext_owner);
        resultsTextView = findViewById(R.id.textview_results);

        // Load last owner
        String lastOwner = getSharedPreferences("prefs", MODE_PRIVATE)
                .getString("lastOwner", "");
        if (!lastOwner.isEmpty()) {
            ownerEditText.setText(lastOwner);
        }

    }

    public void searchAppointments(View view) {
        String owner = ownerEditText.getText().toString().trim();

        if (owner.isEmpty()) {
            showError("Please enter an owner name");
            return;
        }

        try {
            AppointmentRepository repo = new AppointmentRepository(this, owner);
            AppointmentBook appointmentBook = repo.load(owner);

            if (appointmentBook == null || appointmentBook.getAppointments().isEmpty()) {
                resultsTextView.setText("No appointments found for " + owner);
                return;
            }

            Collection<Appointment> appointments = appointmentBook.getAppointments();
            StringBuilder results = new StringBuilder();
            results.append("Appointments for ").append(owner).append(":\n\n");

            int count = 1;
            for (Appointment appointment : appointments) {
                results.append(count++).append(". ")
                        .append(appointment.getDescription()).append("\n")
                        .append("   Start: ").append(appointment.getBeginTime().format(FORMATTER)).append("\n")
                        .append("   End: ").append(appointment.getEndTime().format(FORMATTER)).append("\n\n");
            }

            resultsTextView.setText(results.toString());
            Toast.makeText(this, "Found " + appointments.size() + " appointments", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            showError("Error searching appointments: " + e.getMessage());
        }
    }

    public void clearSearch(View view) {
        ownerEditText.setText("");
        resultsTextView.setText("");
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }










}