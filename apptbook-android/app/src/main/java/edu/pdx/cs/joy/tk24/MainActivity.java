package edu.pdx.cs.joy.tk24;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void addAppointment(View view) {
        try {
            startActivity(new Intent(this, AddAppointmentActivity.class));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Add screen not created yet", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchAppointments(View view) {
        try {
            startActivity(new Intent(this, SearchActivity.class));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Search screen not created yet", Toast.LENGTH_SHORT).show();
        }
    }

    public void PrettyPrint(View view) {
        try {
            startActivity(new Intent(this, PrettyPrintActivity.class));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Pretty Print screen not created yet", Toast.LENGTH_SHORT).show();
        }
    }

    public void showReadmeDialog(View view) {
        String readmeText = "Appointment Book Application\n\n" +
                "Project 5 - CS 410 - Summer 2025\n\n" +
                "This Android application manages appointment books for multiple owners.\n\n" +
                "FEATURES:\n" +
                "• Add new appointments with description and date/time\n" +
                "• Search appointments by owner name\n" +
                "• Pretty print formatted appointment lists\n" +
                "• Data persistence across app sessions\n\n" +
                "USAGE:\n" +
                "• Add Appointment: Create new appointments with owner, description, start and end times\n" +
                "• Search: Find all appointments for a specific owner\n" +
                "• Pretty Print: View formatted appointment lists with details\n\n" +
                "DATE/TIME FORMAT:\n" +
                "• Use MM/dd/yyyy h:mm AM/PM format\n" +
                "• Example: 12/25/2025 2:30 PM\n\n" +
                "DATA STORAGE:\n" +
                "• All appointment data is saved automatically\n" +
                "• Data persists when app is closed and reopened\n" +
                "• Each owner has their own appointment book\n\n" +
                "This application demonstrates Android development with activities, layouts, data persistence, and user interface design.";

        new AlertDialog.Builder(this)
                .setTitle("README - Appointment Book")
                .setMessage(readmeText)
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}