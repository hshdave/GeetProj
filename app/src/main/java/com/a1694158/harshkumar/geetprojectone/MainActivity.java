package com.a1694158.harshkumar.geetprojectone;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    ArrayList<Books> booksarray = new ArrayList<>();
    ArrayList<String> catarray = new ArrayList<>();

    DatabaseReference db;
    FirebaseDatabase fd;
    ListAdapter ls;


    EditText search_txt;
    Spinner spn_choice,spn_cat;
    Button btn_search;

    ListView listme;

    SpinnerAdapt spina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent i = getIntent();

        String catkey = i.getStringExtra("cat");

//        System.out.println("MainAct Cat "+catkey);


        spn_cat = (Spinner) findViewById(R.id.spin_cat);

        listme = (ListView) findViewById(R.id.lst_main);

        fd  = FirebaseDatabase.getInstance();
        db = fd.getReference("books");

        getCat();

        getCatData();



        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot db  : dataSnapshot.getChildren())
                {

                    Boolean ca = db.child("shipToCanada").getValue(Boolean.class);
                    Boolean us = db.child("shipToUSA").getValue(Boolean.class);

                    if(isCanada(getLoc()))
                    {
                        if (ca.equals(true) || us.equals(false))
                        {
                            String bnm = db.child("title").getValue().toString();
                            booksarray.add(new Books(bnm));
                        }

                    }else if (isUSA(getLoc()))
                    {
                         if (us.equals(true) || ca.equals(false))
                         {
                             String bnm = db.child("title").getValue().toString();
                             booksarray.add(new Books(bnm));
                         }
                    }


                }

                ls = new ListAdapter(MainActivity.this,booksarray);
                listme.setAdapter(ls);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case  R.id.menu_search  :
                Toast.makeText(getApplicationContext(),"Search Menu pressed", Toast.LENGTH_LONG).show();
                searchDisplay();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }


    public void searchDisplay()
    {
       final Dialog ds = new Dialog(this,R.style.CustomDialog);
        ds.setTitle("Search");
        ds.setContentView(R.layout.seachlayout);


        search_txt = (EditText) ds.findViewById(R.id.edt_search);
        spn_choice = (Spinner) ds.findViewById(R.id.spin_search);
        btn_search = (Button) ds.findViewById(R.id.btn_search);


        final String searchkey = search_txt.getText().toString();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Pressed On "+searchkey,Toast.LENGTH_LONG).show();

                String keyme = search_txt.getText().toString().toLowerCase();

                if (spn_choice.getSelectedItem().toString().equals("Book Name"))
                {
                    searchbyName(keyme);
                }
                else if (spn_choice.getSelectedItem().toString().equals("Author Name"))
                {
                    searchbyAuthor(keyme);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Select Choice!",Toast.LENGTH_LONG).show();
                }
                ds.dismiss();

            }
        });

        ds.show();
    }


    public void searchbyName(final String key)
    {

        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference db = fd.getReference("books");

        booksarray.clear();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String tit = ds.child("title").getValue().toString().toLowerCase();

                    if(tit.contains(key))
                    {


                        String test = ds.child("title").getValue().toString();
                        System.out.println("After key Search  "+ds.child("title").getValue().toString());


                        booksarray.add(new Books(test));

                    }
                }
                setAdapter();

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void searchbyAuthor(final String autnm)
    {
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference db = fd.getReference("authors");

        booksarray.clear();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String nm = ds.child("name").getValue().toString().toLowerCase();
                    if(nm.contains(autnm))
                    {
                        final String autid = ds.getKey();

                        FirebaseDatabase fd2 = FirebaseDatabase.getInstance();
                        DatabaseReference db2 = fd2.getReference("books");

                        db2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot ds : dataSnapshot.getChildren())
                                {
                                    String id = ds.child("author_id").getValue().toString();

                                    if (id.equals(autid))
                                    {
                                        booksarray.add(new Books(ds.child("title").getValue().toString()));
                                    }
                                }

                                setAdapter();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setAdapter() {
        ls = new ListAdapter(MainActivity.this,booksarray);
        listme.setAdapter(ls);
        ls.notifyDataSetChanged();
    }



    public Boolean isCanada(String cn)
    {
        if(cn.equals("CA"))
        {
            return true;
        }else {
            return  false;
        }
    }

    public Boolean isUSA(String cn)
    {
        if(cn.equals("US"))
        {
            return true;
        }else {
            return  false;
        }
    }

    public String getLoc()
    {
        return Locale.getDefault().getCountry();
    }


    public void getCat()
    {

        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference db = fb.getReference("category");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                 //   System.out.println(ds.getValue().toString());

                    catarray.add(ds.getValue(String.class));
                }

                spina = new SpinnerAdapt(MainActivity.this,catarray);
                spn_cat.setAdapter(spina);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCatData()
    {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference ds = fb.getReference("books");

        ds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {

                    if(ds.child("category").equals("Children Book"))
                    {
                        System.out.println("Category lst Check this "+ds.child("title").getValue());
                    }


                   /* if(ds.getKey().equals("1001"))
                    {
                        for(DataSnapshot ds1 : ds.child("category").getChildren())
                        {
                            System.out.println("Category lst Check this "+ds1.getValue());
                        }
                    }*/

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
