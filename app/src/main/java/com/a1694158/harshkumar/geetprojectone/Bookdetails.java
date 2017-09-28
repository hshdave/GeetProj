package com.a1694158.harshkumar.geetprojectone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a1694158.harshkumar.geetprojectone.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class Bookdetails extends AppCompatActivity {


    TextView bknm,bkpub,bkqty,bkaut;

    FirebaseDatabase database,database2;
    DatabaseReference myRef1,myaut;

    ImageView bookdis;

    String autid,coverpath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetails);

        bknm = (TextView) findViewById(R.id.dis_bknm);
        bkpub = (TextView) findViewById(R.id.dis_bkpub);
        bkqty = (TextView) findViewById(R.id.dis_bkqty);
        bkaut = (TextView) findViewById(R.id.dis_bkaut);
        bookdis = (ImageView) findViewById(R.id.img_book);

        Intent i = getIntent();
        String path = i.getStringExtra("key");

        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReferenceFromUrl(path);


        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bknm.setText(dataSnapshot.child("title").getValue().toString());
                bkpub.setText(dataSnapshot.child("publisher").getValue().toString());
                bkqty.setText(dataSnapshot.child("quantity").getValue().toString());
                autid =  dataSnapshot.child("author_id").getValue().toString();
                coverpath = dataSnapshot.child("cover").getValue().toString();

                System.out.println("Cover Path   "+coverpath);


                Picasso.with(Bookdetails.this).load(coverpath).into(bookdis);


                database2 = FirebaseDatabase.getInstance();
                myaut = database2.getReference("authors");

                myaut.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {

                           if(ds.getKey().equals(autid))
                           {
                               bkaut.setText(ds.child("name").getValue().toString());
                           }
                        }
                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
