package com.example.levanduc.orderfood.Quan_Ly;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.levanduc.orderfood.Mon;
import com.example.levanduc.orderfood.Mon_Adapter;
import com.example.levanduc.orderfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Quan_ly extends AppCompatActivity {
    Button btdoanhthu, btaddnhanvien, btaddmon;
    FirebaseAuth firebaseAuth;
    ListView lvmon;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    ArrayList<Mon> dsMon;
    Mon_Adapter adapter;
    int indexlist;
    String id_tam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
        btdoanhthu = (Button) findViewById(R.id.btdoanhthu);
        btaddnhanvien = (Button) findViewById(R.id.btaadnhanvien);
        btaddmon = (Button) findViewById(R.id.btaadmon);
        firebaseAuth = FirebaseAuth.getInstance();
        lvmon = (ListView) findViewById(R.id.listviewmon);

        registerForContextMenu(lvmon);

        this.registerForContextMenu(lvmon);
        ConnectivityManager connect = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo net = connect.getActiveNetworkInfo();

        if (net.isConnected()) {

            create();
        } else {
            Toast.makeText(getApplicationContext(), "Bạn chưa kết nối internet", Toast.LENGTH_LONG).show();
        }

        return;
    }

    void create() {

        dsMon = new ArrayList<Mon>();
        adapter = new Mon_Adapter(this, dsMon);
        lvmon.setAdapter(adapter);

        myRef.child("Mon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mon mon = dataSnapshot.getValue(Mon.class);
                dsMon.add(mon);
                adapter.notifyDataSetChanged();
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

        btdoanhthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Quan_ly.this, Doanh_thu.class);
                startActivity(intent);
            }
        });

        btaddnhanvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Quan_ly.this);
                dialog.setContentView(R.layout.activity_dang_ky);
                firebaseAuth = FirebaseAuth.getInstance();
                final AutoCompleteTextView mEmailView = (AutoCompleteTextView) dialog.findViewById(R.id.email);
                final EditText mPasswordView = (EditText) dialog.findViewById(R.id.password);
                final EditText mPasswordView2 = (EditText) dialog.findViewById(R.id.password2);
                Button btdl = (Button) dialog.findViewById(R.id.email_sign_in_button);
                btdl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = mEmailView.getText().toString();
                        final String password = mPasswordView.getText().toString();
                        final String password2 = mPasswordView2.getText().toString();
                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(Quan_ly.this, "Nhập Email", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(password)) {
                            Toast.makeText(Quan_ly.this, "Nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(password2)) {
                            Toast.makeText(Quan_ly.this, "Nhập Mật Khẩu 2", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (password.equalsIgnoreCase(password2)) {
                            final ProgressDialog progressDialog = ProgressDialog.show(Quan_ly.this, "Vui Lòng Chờ...", "Đang Xử Lý...", true);
                            (firebaseAuth.createUserWithEmailAndPassword(email, password))
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressDialog.dismiss();

                                            if (task.isSuccessful()) {
                                                Toast.makeText(Quan_ly.this, "Đăng Ký Thành Công", Toast.LENGTH_LONG).show();

                                            } else {
                                                Log.e("Lỗi", task.getException().toString().trim());
                                                Toast.makeText(Quan_ly.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Quan_ly.this, "Các Mật Khẩu Không Khớp, Vui Lòng Thử Lại", Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        btaddmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Quan_ly.this, Them_mon.class);
                intent.putExtra("KEY","1");
                startActivity(intent);
            }


        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inf = this.getMenuInflater();
        inf.inflate(R.menu.context_menu_list_food, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        indexlist = info.position;
        id_tam = dsMon.get(indexlist).Id;

        if (item.getItemId() == R.id.delete_1_food) {
            xoaFood();
            reLoad();
        }
        if (item.getItemId() == R.id.repair_1_food) {
            suaFood();

        }

        return super.onContextItemSelected(item);
    }

    public void xoaFood() {
        Query foodQuery = myRef.child("Mon").orderByChild("Id").equalTo(id_tam);

        foodQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren())
                    foodSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void suaFood() {
//        Mon f1 = new Mon(id_tam,)
        Intent i = new Intent(Quan_ly.this, Them_mon.class);
        i.putExtra("KEY",id_tam);
        startActivity(i);
    }

    public void reLoad() {
        adapter = new Mon_Adapter(this, dsMon);
        dsMon.remove(indexlist);
        lvmon.setAdapter(adapter);
        lvmon.deferNotifyDataSetChanged();
    }

}



