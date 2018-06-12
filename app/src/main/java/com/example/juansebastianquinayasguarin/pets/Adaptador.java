package com.example.juansebastianquinayasguarin.pets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter{

    private ArrayList<Entidad> listItems;
    private Context context;

    public Adaptador(ArrayList<Entidad> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Entidad item = (Entidad) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        final TextView textView = convertView.findViewById(R.id.tvText);
        imageView.setImageResource(item.getImgFoto());
        textView.setText(item.getTitulo());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getTitulo() == "ALBA"){
                    Intent intent = new Intent(context,protectora_view.class);
                }
            }
        });


        return convertView;
    }
}
