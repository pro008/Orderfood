package com.example.levanduc.orderfood.Khach_Hang;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.levanduc.orderfood.Mon;
import com.example.levanduc.orderfood.R;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Dell on 4/18/2017.
 */

public class Mon_Chon_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Mon_Chon> ds;

    public Mon_Chon_Adapter(Context context, ArrayList<Mon_Chon> ds) {
        this.context=context;
        this.ds=ds;
    }

    public static class View_Mot_O{
        public TextView tenmon;
        public TextView gia;
        public TextView soluong;
        public TextView tien;
        public TextView ghichu;
        public TextView trangthai;
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
            convertView =inf.inflate(R.layout.food_choice_item, null);

            mot_o = new View_Mot_O();
            mot_o.tenmon=(TextView) convertView.findViewById(R.id.tv_foodchoice_name);
            mot_o.gia=(TextView) convertView.findViewById(R.id.tv_foodchoice_price);
            mot_o.soluong=(TextView) convertView.findViewById(R.id.tv_foodchoice_quatity);
            mot_o.tien=(TextView) convertView.findViewById(R.id.tv_foodchoice_tien);
            mot_o.ghichu=(TextView) convertView.findViewById(R.id.tv_foodchoice_note);
            mot_o.trangthai= (TextView) convertView.findViewById(R.id.tv_trangthai);

            convertView.setTag(mot_o);
        }

        else {
            mot_o = (View_Mot_O) convertView.getTag();
        }
        mot_o.tenmon.setText(ds.get(position).TenMonC+"");
        mot_o.soluong.setText(ds.get(position).SoLuongC+"");
        mot_o.ghichu.setText(ds.get(position).GhiChuC+"");
//        mot_o.txtgia.setText(ds.get(position).Gia+ "");
        if(ds.get(position).trangthai != 0)
            mot_o.trangthai.setText("True");

        int myGia = ds.get(position).GiaC;
        String formattedPrice = new DecimalFormat("###,###").format(myGia);
        mot_o.gia.setText(formattedPrice+" ₫");

        int myTien = ds.get(position).ThanhTienC;
        String formattedMoney = new DecimalFormat("###,###").format(myTien);
        mot_o.tien.setText(formattedMoney+" ₫");

        return convertView;
    }
}
