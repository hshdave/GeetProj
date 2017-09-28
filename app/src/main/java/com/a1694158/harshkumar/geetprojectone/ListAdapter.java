package com.a1694158.harshkumar.geetprojectone;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Harsh on 9/18/2017.
 */

public class ListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context c;
    ArrayList<Books> bknm;

    DatabaseReference db;
    FirebaseDatabase fd;


    String ls =  "";

    public ListAdapter(Context c, ArrayList<Books> bknm) {
        this.c = c;
        this.bknm = bknm;
    }

    @Override
    public int getCount() {
        return bknm.size();
    }

    @Override
    public Object getItem(int i) {
        return bknm.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        fd  = FirebaseDatabase.getInstance();
        db = fd.getReference();


        if (inflater == null)
        {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null)
        {
            view = inflater.inflate(R.layout.contentlist,viewGroup,false);
        }


        TextView txtv = (TextView) view.findViewById(R.id.txt_list);
        txtv.setText(bknm.get(i).getBooknm());


        txtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bkn = bknm.get(i).getBooknm().toString();

               // Toast.makeText(c,bkn,Toast.LENGTH_LONG).show();

                db.child("books").orderByChild("title").equalTo(bkn).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        FirebaseDatabase db2 = FirebaseDatabase.getInstance();
                        DatabaseReference myref2 = db2.getReference();

                        ls = myref2.child("books").child(dataSnapshot.getKey()).toString();

                        //Toast.makeText(c,ls,Toast.LENGTH_LONG).show();
                        Intent i = new Intent(c,Bookdetails.class);
                        i.putExtra("key",ls);
                        c.startActivity(i);


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });



        return view;

    }
}
