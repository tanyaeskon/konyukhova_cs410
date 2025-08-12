package edu.pdx.cs.joy.tk24;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
}