package com.a1694158.harshkumar.geetprojectone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Harsh on 10/12/2017.
 */

public class SpinnerAdapt extends BaseAdapter{

    Context c;
    ArrayList<String> catnm;


    LayoutInflater inflater;

    public SpinnerAdapt(Context c, ArrayList<String> catnm) {
        this.c = c;
        this.catnm = catnm;
    }

    @Override
    public int getCount() {
        return catnm.size();
    }

    @Override
    public Object getItem(int position) {
        return catnm.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (inflater == null)
        {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.contentlist,parent,false);
        }


        TextView txtv = (TextView) convertView.findViewById(R.id.txt_list);

        txtv.setText(catnm.get(position).toString());


        txtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Category Selected "+catnm.get(position).toString());

                Intent i = new Intent(c,MainActivity.class);
                i.putExtra("cat",catnm.get(position).toString());
                c.startActivity(i);
            }
        });


        return convertView;
    }
}
