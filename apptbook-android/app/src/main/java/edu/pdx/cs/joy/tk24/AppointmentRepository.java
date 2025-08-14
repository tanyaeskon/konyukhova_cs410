package edu.pdx.cs.joy.tk24;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AppointmentRepository {

    private final Context context;
    private final String owner;

    public AppointmentRepository(Context context, String owner) {
        this.context = context;
        this.owner = owner;
    }

    public AppointmentBook load(String owner) {
        try {
            FileInputStream fis = context.openFileInput(fileName(owner));
            ObjectInputStream ois = new ObjectInputStream(fis);
            AppointmentBook book = (AppointmentBook) ois.readObject();
            ois.close();
            return book;
        } catch (Exception e) {
            return new AppointmentBook(owner);
        }
    }

    public void save(AppointmentBook book) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName(owner), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(book);
            oos.close();
        } catch (Exception e) {
            throw new RuntimeException("Error saving appointment book", e);
        }
    }
    private String fileName(String owner) {
        return owner + "_appointments.ser";
    }
}
