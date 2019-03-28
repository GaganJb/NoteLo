package com.example.asus.notelo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Objects;

public class addNotes extends AppCompatActivity {

    int noteID;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        EditText editText = findViewById(R.id.editText);//we have to setText of editText to that note that the user tapped on
                                                        //that is done by putExtra and getIntextra getIntent we get the id of that note from the mainactivity
        Intent intent = getIntent();
        noteID = intent.getIntExtra("noteId",-1);//if the options of list value is not selected value of noteId will be -1
                                                                        //i.e when menuItem is selected from the menu noteId will be -1 leading to crash thus conditions need to be given

        if(noteID!=-1) {
            if(note.notes.get(noteID)=="Add a new note...")
                editText.setText("");
            else
            editText.setText(note.notes.get(noteID));//editText will be set to the note that was there in note activity
            //now when editText is changed note in Listview needs to be changed  done by addtextchangelistener
        }
        else{// if noteId is -1 i.e when Listview is not selected when it is selected value cant be -1 ever

            note.notes.add("");//suppose new note is created from menu and there is one from before only. so new node'r id will be 1 since starts from zero. So noteID will be size()-1. size is 2.
            noteID=note.notes.size()-1;//menu re add note korile agor juntu note ase heitur agot jabo so agote jiman size asil heitu -1
            note.arrayAdapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    note.notes.set(noteID, String.valueOf(s));//set the note of the MainActivity that was tapped on to that note which the user typed
                    //now update the listView via the arrayadapter
                    note.arrayAdapter.notifyDataSetChanged();//updates the data in listView

                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);

                    HashSet<String> set = new HashSet<>(note.notes);//set contains all the notes that will be entered by the user//hashset used to store the strings i.e notes one by one
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putStringSet("Note", set);
                    edit.apply();
                }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}