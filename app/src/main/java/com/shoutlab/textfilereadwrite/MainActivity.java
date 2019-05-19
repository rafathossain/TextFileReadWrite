package com.shoutlab.textfilereadwrite;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    List<String> data;

    EditText item;

    Button sendButton;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item = findViewById(R.id.itemAdder);
        sendButton = findViewById(R.id.sendBtn);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        listView = findViewById(R.id.listView);

        data = new ArrayList<String>();

        data.add("This is set by system");

        arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);

        listView.setAdapter(arrayAdapter);

        getData();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = item.getText().toString();

                if(input.equals("")){
                    Toast.makeText(MainActivity.this, "Please write something to add", Toast.LENGTH_SHORT).show();
                } else {
                    if(input.equals("CLEAR")){
                        item.setText("");
                        TextManager.clearFile();
                        Toast.makeText(MainActivity.this, "Text file cleared", Toast.LENGTH_SHORT).show();

                        data.clear();
                        data.add("This is set by system");
                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        item.setText("");
                        TextManager.saveToFile(input);
                        data.add(input);
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "New item added", Toast.LENGTH_SHORT).show();
                    }
                }
                closeKeyboard();
            }
        });
    }

    private void getData(){
        String dataSet = TextManager.ReadFile(MainActivity.this);

        String[] lines = dataSet.split("\\r?\\n");

        data.addAll(Arrays.asList(lines).subList(1, lines.length));
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Voila! You are good to go.", Toast.LENGTH_SHORT).show();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(MainActivity.this, "Permission denied to write your External storage", Toast.LENGTH_SHORT).show();
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
}
