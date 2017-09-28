package com.a1694158.harshkumar.geetprojectone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {


    ArrayList<Books> booksarray = new ArrayList<>();

    DatabaseReference db;
    FirebaseDatabase fd;
    ListAdapter ls;


    ListView listme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listme = (ListView) findViewById(R.id.lst_main);


        fd  = FirebaseDatabase.getInstance();
        db = fd.getReference("books");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot db  : dataSnapshot.getChildren())
                {
                   // System.out.println("Check me     "+db.child("title").getValue());

                    String bnm = db.child("title").getValue().toString();
                    booksarray.add(new Books(bnm));
                }

                ls = new ListAdapter(MainActivity.this,booksarray);
                listme.setAdapter(ls);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

}
