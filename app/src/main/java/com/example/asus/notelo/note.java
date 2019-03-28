package com.example.asus.notelo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class note extends AppCompatActivity {



    static ArrayList<String> notes = new ArrayList<>();
    static  ArrayAdapter<String> arrayAdapter;
    ListView listView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.add:{
                Intent intent = new Intent(getApplicationContext(), addNotes.class);
                startActivity(intent);
                return true;
            }
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


                SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        listView = findViewById(R.id.listView);

        HashSet<String> set = (HashSet<String>) prefs.getStringSet("Note",null);

        if(set == null){
            notes.add("Add a new note...");
        }
        else{
            notes = new ArrayList<>(set);
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);//adds the note to listview

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), addNotes.class);//links this activity to addNotes activity
                intent.putExtra("noteId", position);//noteId't position tu thakibo note tur so that we can use that id in addnotes activity
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(note.this).setIcon(R.drawable.ic_launcher_foreground)
                                                    .setTitle("Are you sure?")
                                                    .setMessage("Do you want to delete it?")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                                notes.remove(position);
                                                                arrayAdapter.notifyDataSetChanged();//note is removed
                                                            //now save this updated note in prefs
                                                            SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                            HashSet<String> set = new HashSet<>(note.notes);
                                                            SharedPreferences.Editor edit = prefs.edit();
                                                            edit.putStringSet("Note", set);
                                                            edit.apply();//notes are updated
                                                        }
                                                    }).setNegativeButton("No", null).show();//if no then do nothing so null in listener

                return true;//change false to true otherwise short will also be done before long click. true mean only long click is eligible in the items
            }
        });
    }
}
