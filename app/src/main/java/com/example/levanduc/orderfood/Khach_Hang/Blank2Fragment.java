package com.example.levanduc.orderfood.Khach_Hang;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.levanduc.orderfood.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;


/**
 * A simple {@link Fragment} subclass.
 */
public class Blank2Fragment extends Fragment {

    ListView lv;
    Button bt_send, bt_bill;
    TextView tv_money;
    int indexList, tong_tien;
    String code;
    ArrayList<Mon_Chon> dsMonChon = new ArrayList<Mon_Chon>();
    ArrayList<Mon_Chon> dsMonChon_send = new ArrayList<Mon_Chon>();
    Mon_Chon_Adapter adapter;

    // tham so doanh thu
    int thamso =0;
    Calendar cl;
    String day1;
    private DatabaseReference mDatabase3;
    FirebaseDatabase firebase;
    ///



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank2,container,false);
        bt_send=(Button)view.findViewById(R.id.bt_foodchoice_send);
        bt_bill=(Button)view.findViewById(R.id.bt_foodchoice_bill);
        tv_money=(TextView)view.findViewById(R.id.tv_foodchoice_tongtien);
        lv = (ListView)view.findViewById(R.id.lvkh);


        //doanh thu
        firebase = FirebaseDatabase.getInstance();
        cl = Calendar.getInstance();
        day1 = cl.getTime().getDate()+""+cl.getTime().getMonth()+""+cl.getTime().getYear();



        mDatabase3 = firebase.getReference();

        ///////////////////////





        registerForContextMenu(lv);
        this.registerForContextMenu(lv);

        // ten rieng cua moi may
        code = Settings.System.getString(getActivity().getContentResolver(),
                Settings.System.ANDROID_ID);

        ConnectivityManager connect = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo net = connect.getActiveNetworkInfo();

        if (net.isConnected()) {
            create();
        } else {
            Toast.makeText(getContext(), "Bạn chưa kết nối internet", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    void create(){
        adapter=new Mon_Chon_Adapter(getActivity(), dsMonChon);
        lv.setAdapter(adapter);
        tong_tien=0;



        mDatabase3.child("Danh sach order").addChildEventListener(new ChildEventListener() {
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

        tv_money.setText(tong_tien+"");
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0; i<dsMonChon.size(); i++){
                    mDatabase3.child("Danh sach order final").push().setValue(dsMonChon.get(i));
                }
                mDatabase3.child("Danh sach order").removeValue();
                reset(dsMonChon);
                reset(dsMonChon_send);


                adapter=new Mon_Chon_Adapter(getActivity(), dsMonChon_send);
                lv.setAdapter(adapter);
                mDatabase3.child("Danh sach order final").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Mon_Chon monchon=dataSnapshot.getValue(Mon_Chon.class);
                        tong_tien=tong_tien +monchon.ThanhTienC;
                        dsMonChon_send.add(monchon);
                        adapter.notifyDataSetChanged();


                        tv_money.setText(tong_tien+"");
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








                Toast.makeText(getActivity(), "Gửi thành công", Toast.LENGTH_SHORT).show();


            }
        });

        bt_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase3.child("revenue").child(day1).setValue(thamso);
                mDatabase3.child("Danh sach order final").removeValue();
            }
        });



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu_chon_mon_an, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        indexList=info.position;

        if(item.getItemId() == R.id.xoa_mot_mon){
            mDatabase3.child("Danh sach order").child(code + dsMonChon.get(indexList).Id).removeValue();

            adapter=new Mon_Chon_Adapter(getActivity(), dsMonChon);
            dsMonChon.remove(indexList);
            lv.setAdapter(adapter);
            lv.deferNotifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }

    public void reset(ArrayList<Mon_Chon> ds){
        adapter=new Mon_Chon_Adapter(getActivity(), ds);
        ds.clear();
        lv.setAdapter(adapter);
        lv.deferNotifyDataSetChanged();
    }


}
