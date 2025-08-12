package edu.pdx.cs.joy.tk24;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;

public class PrettyPrintActivity extends AppCompatActivity {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pretty_print);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        String owner = getIntent().getStringExtra("owner");
        if (owner == null || owner.isEmpty()) {
            owner = getSharedPreferences("prefs", MODE_PRIVATE).getString("lastOwner", "");


        }
        if (owner.isEmpty()) {
            new AlertDialog.Builder(this).setTitle("Error").setMessage("No owner specified.").setPositiveButton("OK", (d, w) -> finish()).show();
            return;
        }

        try{

        AppointmentBook book = new AppointmentRepository(this, owner).load(owner);

        StringWriter sw = new StringWriter();
        new PrettyPrinter(new PrintWriter(sw, true)).dump(book);

        TextView out = findViewById(R.id.output);
        out.setText(sw.toString());
    } catch (Exception e) {
        showMessage("Error loading/printing appointments:\n" + e.getMessage());
    }

}

    private void showMessage(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage(msg)
                .setPositiveButton("OK", (d, w) -> finish())
                .show();
    }



}