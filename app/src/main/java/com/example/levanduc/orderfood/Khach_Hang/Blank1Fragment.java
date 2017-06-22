package com.example.levanduc.orderfood.Khach_Hang;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.levanduc.orderfood.Mon;
import com.example.levanduc.orderfood.Mon_Adapter;
import com.example.levanduc.orderfood.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Blank1Fragment extends Fragment {

    public StorageReference mStorageRef;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    ListView lvdsmon;
    ArrayList<Mon> dsMon=new ArrayList<Mon>();
    Mon_Adapter adapter;
    int indexList;
    String code;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        lvdsmon = (ListView) view.findViewById(R.id.lvdsmon);
        registerForContextMenu(lvdsmon);
        this.registerForContextMenu(lvdsmon);
        oncilckdatmon();
        // ten rieng cua moi may
        code = Settings.System.getString(getActivity().getContentResolver(),
                Settings.System.ANDROID_ID);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        ConnectivityManager connect = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo net = connect.getActiveNetworkInfo();

        if (net.isConnected()) {
            create();
        }

        return view;
    }
    void oncilckdatmon(){
        //set onclick trên lv////////////////////////////////////////////
        lvdsmon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog ad = new Dialog(getActivity());
                ad.setContentView(R.layout.dialog_datmon);

                ImageView imgDatMon = (ImageView)ad.findViewById(R.id.imagedatmon);
                final TextView tvTenMon = (TextView)ad.findViewById(R.id.txttendm);
                final TextView tvGia = (TextView)ad.findViewById(R.id.txtgiadm);
                final EditText etSoLuong = (EditText)ad.findViewById(R.id.etsoluongdm);
                final EditText etGhiChu = (EditText)ad.findViewById(R.id.etghichudm);
                Button btnDatMon = (Button)ad.findViewById(R.id.btnDatMon);
                Picasso.with(getActivity()).load(dsMon.get(position).Link).into(imgDatMon);
                tvTenMon.setText((CharSequence) dsMon.get(position).TenMon);
                tvGia.setText(dsMon.get(position).Gia+"");
                btnDatMon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etSoLuong.getText().toString().isEmpty())
                            Toast.makeText(getActivity(),"nhap lai",Toast.LENGTH_SHORT).show();
                        else {
                            int id = position;
                            String ten = tvTenMon.getText() + "";
                            int gia = Integer.parseInt(tvGia.getText().toString());
                            int soluong = Integer.parseInt(etSoLuong.getText().toString());
                            String ghichu = etGhiChu.getText() + "";
                            int tien = gia * soluong;

                            if (soluong > 0) {
                                Mon_Chon monchon = new Mon_Chon(id, ten, gia, soluong, ghichu, tien);
                                myRef.child("Danh sach order").child(code + position).setValue(monchon);
                                Toast.makeText(getActivity(), "Đặt món thành công", Toast.LENGTH_SHORT).show();
                                ad.cancel();
                            }


                        }
                    }
                });

                ad.show();
            }
        });
    }

    void create() {

        adapter=new Mon_Adapter(getActivity(), dsMon);
        lvdsmon.setAdapter(adapter);

        myRef.child("Mon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mon mon=dataSnapshot.getValue(Mon.class);
                dsMon.add(mon);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu_thucdon, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        indexList=info.position;

        if(item.getItemId() == R.id.datmon){
            DatMon();
        }
        return super.onContextItemSelected(item);
    }

    public void DatMon(){
        final Dialog ad = new Dialog(getActivity());
        ad.setContentView(R.layout.dialog_datmon);

        ImageView imgDatMon = (ImageView)ad.findViewById(R.id.imagedatmon);
        final TextView tvTenMon = (TextView)ad.findViewById(R.id.txttendm);
        final TextView tvGia = (TextView)ad.findViewById(R.id.txtgiadm);
        final EditText etSoLuong = (EditText)ad.findViewById(R.id.etsoluongdm);
        final EditText etGhiChu = (EditText)ad.findViewById(R.id.etghichudm);
        Button btnDatMon = (Button)ad.findViewById(R.id.btnDatMon);

        Picasso.with(getActivity()).load(dsMon.get(indexList).Link).into(imgDatMon);
        tvTenMon.setText((CharSequence) dsMon.get(indexList).TenMon);
        tvGia.setText(dsMon.get(indexList).Gia+"");

        btnDatMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etSoLuong.getText().toString().isEmpty())
                    Toast.makeText(getActivity(),"nhap lai",Toast.LENGTH_SHORT).show();
                else {
                    int id = indexList;
                    String ten = tvTenMon.getText() + "";
                    int gia = Integer.parseInt(tvGia.getText().toString());
                    int soluong = Integer.parseInt(etSoLuong.getText().toString());
                    String ghichu = etGhiChu.getText() + "";
                    int tien = gia * soluong;

                    if (soluong > 0) {
                        Mon_Chon monchon = new Mon_Chon(id, ten, gia, soluong, ghichu, tien);
                        myRef.child("Danh sach order").child(code + indexList).setValue(monchon);
                        ad.dismiss();
                    }

                }
            }
        });
        ad.show();
    }

}
