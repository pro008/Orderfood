package com.example.levanduc.orderfood.Dau_Bep;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.levanduc.orderfood.Khach_Hang.Mon_Chon;
import com.example.levanduc.orderfood.Khach_Hang.Mon_Chon_Adapter;
import com.example.levanduc.orderfood.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Dau_bep extends AppCompatActivity {

    ListView lv;
    ArrayList<Mon_Chon> dsMonChon=new ArrayList<Mon_Chon>();
    Mon_Chon_Adapter adapter;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dau_bep);
        lv=(ListView)findViewById(R.id.lv_daubep);
        code = Settings.System.getString((Dau_bep.this).getContentResolver(),
                Settings.System.ANDROID_ID);

        adapter=new Mon_Chon_Adapter(this, dsMonChon);
        lv.setAdapter(adapter);

        myRef.child("Danh sach order final").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mon_Chon monchon=dataSnapshot.getValue(Mon_Chon.class);
                dsMonChon.add(monchon);
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mon_Chon mama = dsMonChon.get(position);
                mama.trangthai = 1;
                myRef.child("Danh sach order").child(code + position).setValue(mama);

                adapter=new Mon_Chon_Adapter(Dau_bep.this, dsMonChon);
                lv.setAdapter(adapter);

            }
        });
    }
}
