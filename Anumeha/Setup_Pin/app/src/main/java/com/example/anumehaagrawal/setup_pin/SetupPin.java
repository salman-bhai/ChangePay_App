package com.example.anumehaagrawal.setup_pin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.database.*;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import android.util.Log;


/*
Database looks like this
{
  "pin" : {
    "pin1" : {
      "value" : 1333
    },
    "pin2" : {
      "value" : 7888
    }
  }
}
 */

public class SetupPin extends AppCompatActivity {

    Button mButton;
    EditText mEdit;
    public int flag=0 ;

    private DatabaseReference mDatabase;
    final ArrayList<String> ids = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_pin);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEdit = (EditText) findViewById(R.id.editText2);

        mButton = (Button) findViewById(R.id.button1);
        mDatabase.child("pin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ids.add(childSnapshot.getValue().toString());
                    /*EditText mytextview = (EditText) findViewById(R.id.editText);
                    mytextview.setText(ids.get(0)); */
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                flag=0;

                for (String val : ids) {
                    /* EditText mytextview3 = (EditText) findViewById(R.id.editText3);
                    mytextview3.setText(val); */
                    String str=mEdit.getText().toString();
                   /* EditText mytextview4 = (EditText) findViewById(R.id.editText4);
                    mytextview4.setText(str); */



                    if (val.compareTo("{value="+str+"}")==0) {
                        flag=1;

                        EditText mytextview = (EditText) findViewById(R.id.editText);
                        mytextview.setText("Successful");




                    }
                    if(flag!=1){

                        EditText mytextview = (EditText) findViewById(R.id.editText);
                        mytextview.setText("Wrong Pin entered");



                    }


                }
            }
        });
    }


}