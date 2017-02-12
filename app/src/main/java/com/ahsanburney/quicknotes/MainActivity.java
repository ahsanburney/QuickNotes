package com.ahsanburney.quicknotes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.util.Xml;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView dateTime;
    private EditText notes;
    private Data_Saved data_saved;
    private String noChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d, h:mm a");
        String caldate = formatter.format(Calendar.getInstance().getTime());
        dateTime = (TextView) findViewById(R.id.lastupdated);
        dateTime.setText(caldate);
        notes = (EditText) findViewById(R.id.main_notes);
        notes.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onResume() {
        super.onStart();
        data_saved = loadFile();  // Load the JSON containing the product data - if it exists
        if (data_saved != null) { // null means no file was loaded
            if(data_saved.getdateTime().equals(""))
            {
                DateFormat formatter = new SimpleDateFormat("EEE MMM d, h:mm a");
                String caldate = formatter.format(Calendar.getInstance().getTime());
                dateTime.setText(caldate);

            }
            else
                dateTime.setText(data_saved.getdateTime());
            if(data_saved.getnotes().equals(""))
            {
                DateFormat formatter = new SimpleDateFormat("EEE MMM d, h:mm a");
                String caldate = formatter.format(Calendar.getInstance().getTime());
                dateTime.setText(caldate);

            }
            notes.setText(data_saved.getnotes());
            noChange = data_saved.getnotes();
        }
    }

    private Data_Saved loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        data_saved = new Data_Saved();
        try {
            InputStream is = getApplicationContext().openFileInput("data_saved.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is,"UTF-8"));

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("dateTime")) {
                    data_saved.setdateTime(reader.nextString());
                } else if (name.equals("notes")) {
                    data_saved.setnotes(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No file is Present", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data_saved;
    }

    @Override
    protected void onPause() {
        super.onPause();

        data_saved.setdateTime(dateTime.getText().toString());
        data_saved.setnotes(notes.getText().toString());

        saveNotes();
    }

    private void saveNotes() {


        if (noChange.equals(data_saved.getnotes())) {
            return;

        } else {



            Log.d(TAG, "saveNotes: Saving JSON File");
            try {
                StringWriter sw = new StringWriter();

                FileOutputStream fos = getApplicationContext().openFileOutput("data_saved.json", Context.MODE_PRIVATE);

                JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
                writer.setIndent("  ");
                writer.beginObject();
                DateFormat formatter = new SimpleDateFormat("EEE MMM d, h:mm a");
                String date = formatter.format(Calendar.getInstance().getTime());
                writer.name("dateTime").value(date);
                writer.name("notes").value(data_saved.getnotes());
                writer.endObject();
                writer.close();
                Log.d(TAG, "saveNotes: JSON:\n" + sw.toString());

                Toast.makeText(this, "Notes Saved!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

}