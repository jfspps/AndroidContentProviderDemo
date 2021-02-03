package com.example.androidcontentproviderdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView contactNames;
    public static final int REQUEST_CODE_READ_CONTACTS = 1;
    FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactNames = (ListView) findViewById(R.id.contact_names);

        // check the version of Android with ContextCompat and on success, run checkSelfPermission
        int hasReadContactPermission = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CONTACTS);

        // the above can be replaced with a static import (READ_CONTACTS instead of
        // Manifest.permission.READ_CONTACTS)
        // use them very sparingly!; difficult to read and debug)
        // IDE should ask to import the required static
//        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, READ_CONTACTS);

        Log.d(TAG, "onCreate: checkSelfPermission: " + hasReadContactPermission);
        if (hasReadContactPermission != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "onCreate: requesting permission");
            // call appropriate request method based on Android version;
            // note that onCreate does not wait for a decision so a callback from
            // requestPermissions() is required, below
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: floating action button started");

                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                    Snackbar.make(view, "Contacts permissions required", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Grant access", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(TAG, "onClick: snackBar onClick()");

                                    // decide if we should show a UI element which rationalises why
                                    // permissions are required; returns false if the user clicked
                                    // "do not ask again" (in which case an Intent directs the user
                                    // to the Android settings menu
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                            Manifest.permission.READ_CONTACTS)){
                                        Log.d(TAG, "onClick: calling requestPermissions()");
                                        ActivityCompat.requestPermissions(MainActivity.this,
                                                new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
                                    } else {
                                        Log.d(TAG, "onClick: snackBar() onClick(); going to app settings");
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                                        // build this app's package URI needed for intent:
                                        // "package:com.example.androidcontentproviderdemo"
                                        Uri uri = Uri.fromParts(
                                                "package", MainActivity.this.getPackageName(), null);
                                        Log.d(TAG, "Intent URI: " + uri.toString());
                                        intent.setData(uri);
                                        MainActivity.this.startActivity(intent);
                                    }
                                    Log.d(TAG, "onClick: snackBar ended");
                                }
                            }).show();
                } else {
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
                }

                Log.d(TAG, "onClick: FAB ended");
            }
        });
        Log.d(TAG, "onCreate: ended");
    }

    // callback to requestPermissions()
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: started");
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: granted");
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: denied");
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: ended");
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