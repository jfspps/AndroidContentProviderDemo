package com.example.androidcontentproviderdemo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView contactNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactNames = (ListView) findViewById(R.id.contact_names);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: floating action button started");
                String[] projection = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};

                // data retrieval using a Content Resolver provided below only works for API 22 or earlier

                // content resolver provides access to data for the client (MainActivity)
                // and is responsible for requesting data on the client's behalf
                // The content resolver gets the data from the appropriate Content Provider, which
                // ultimately knows how and where to get data from a data source, e.g. a
                // database; these connections tend to be more efficient for large numbers of clients

                // the content resolver (a singleton) uses an Authority as part of the URI to decide
                // which content provider to query
                ContentResolver contentResolver = getContentResolver();

                // a cursor provides read-write access to a data retrieved from a query
                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                        projection, null, null,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

                if (cursor != null){
                    List<String> contacts = new ArrayList<>();

                    // cursor is currently positioned before the first entry
                    while (cursor.moveToNext()){
                        contacts.add(cursor.getString(cursor.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
                    }
                    cursor.close();
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MainActivity.this, R.layout.contact_detail, R.id.name, contacts);
                    contactNames.setAdapter(adapter);
                }
                Log.d(TAG, "onClick: FAB ended");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}