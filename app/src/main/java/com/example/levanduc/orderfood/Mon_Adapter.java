package com.example.levanduc.orderfood;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Mon_Adapter extends BaseAdapter{
    Context context;
    ArrayList<Mon> ds;

    public Mon_Adapter(Context context, ArrayList<Mon> ds) {
        this.context=context;
        this.ds=ds;
    }

    public static class View_Mot_O{
        public ImageView imgmon;
        public TextView txttenmon;
        public TextView txtgia;
        public TextView txtsoluong;
    }

    @Override
    public int getCount() {
        return ds.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = ((Activity)context).getLayoutInflater();
        View_Mot_O mot_o;
        if(convertView==null){
            convertView =inf.inflate(R.layout.food_items, null);

            mot_o = new View_Mot_O();
            mot_o.imgmon=(ImageView) convertView.findViewById(R.id.imgmon);
            mot_o.txttenmon=(TextView) convertView.findViewById(R.id.txttenmon);
            mot_o.txtgia=(TextView) convertView.findViewById(R.id.txtgia);
            mot_o.txtsoluong=(TextView) convertView.findViewById(R.id.txtsoluong);

            convertView.setTag(mot_o);
        }

        else {
            mot_o = (View_Mot_O) convertView.getTag();
        }
        mot_o.txttenmon.setText(ds.get(position).TenMon+ "");
//        mot_o.txtgia.setText(ds.get(position).Gia+ "");

        int myGia = ds.get(position).Gia;
        String formattedPrice = new DecimalFormat("###,###").format(myGia);
        mot_o.txtgia.setText(formattedPrice+" ₫");

        Picasso.with(context).load(ds.get(position).Link).into(mot_o.imgmon);
        return convertView;

    }
}
