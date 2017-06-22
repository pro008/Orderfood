package com.example.levanduc.orderfood.Quan_Ly;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.levanduc.orderfood.Mon;
import com.example.levanduc.orderfood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class Them_mon extends AppCompatActivity {
    DatabaseReference mData;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    final int REQUEST_CODE_GALLERY = 999;
    EditText ettmon,ettgia;
    ImageView img;
    Button bttmon;
    TextView tv;

    Bundle bundle;
    String x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_mon);
        mData = FirebaseDatabase.getInstance().getReference();
        final StorageReference storageRef = storage.getReference();
        ettmon = (EditText) findViewById(R.id.ettmon);
        ettgia = (EditText) findViewById(R.id.etgia);
        img = (ImageView) findViewById(R.id.imageView);
        bttmon = (Button) findViewById(R.id.bttmon);
        tv = (TextView) findViewById(R.id.tvdl);


        bundle = getIntent().getExtras();

        x = bundle.getString("KEY");

        if(!x.equals("1")){
            tv.setText("Sua mon an");
            bttmon.setText("Sua");
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        Them_mon.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });


        bttmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                final StorageReference mountainsRef = storageRef.child("Mon"+ calendar.getTimeInMillis()+"png");
                img.setDrawingCacheEnabled(true);
                img.buildDrawingCache();
                String tenmon = ettmon.getText().toString();
                final String gia = ettgia.getText().toString();
                if (TextUtils.isEmpty(tenmon)) {
                    Toast.makeText(Them_mon.this, "Nhập Tên Món", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(gia)) {
                    Toast.makeText(Them_mon.this, "Nhập Giá", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bitmap bitmap = img.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.d("AAA", downloadUrl + "");


                        if(!x.equals("1")){

                            Mon mon = new Mon(x,ettmon.getText().toString(), Integer.valueOf(ettgia.getText().toString()), String.valueOf(downloadUrl));

                            Toast.makeText(Them_mon.this, "sua thanh cong",Toast.LENGTH_SHORT).show();
                            mData.child("Mon").child(x).setValue(mon , new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    if(databaseError== null){
                                        Toast.makeText(Them_mon.this, "Thêm Món Thành Công",Toast.LENGTH_SHORT).show();
                                        img.setImageResource(R.drawable.themmon);
                                        ettgia.setText("");
                                        ettmon.setText("");
                                        Intent intent = new Intent(Them_mon.this,Quan_ly.class);
                                        startActivity(intent);

                                    }else {
                                        Toast.makeText(Them_mon.this, "Thêm Món Thất Bại",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {

                            String idt =  mData.child("Mon").push().getKey();

                            Mon mon = new Mon(idt,ettmon.getText().toString(), Integer.valueOf(ettgia.getText().toString()), String.valueOf(downloadUrl));



                            mData.child("Mon").child(idt).setValue(mon, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    if (databaseError == null) {
                                        Toast.makeText(Them_mon.this, "Thêm Món Thành Công", Toast.LENGTH_SHORT).show();
                                        img.setImageResource(R.drawable.themmon);
                                        ettgia.setText("");
                                        ettmon.setText("");
                                        Intent intent = new Intent(Them_mon.this, Quan_ly.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Them_mon.this, "Thêm Món Thất Bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
            private byte[] imageViewToByte(ImageView anh) {
                Bitmap bitmap = ((BitmapDrawable)anh.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                return byteArray;
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "Không thể truy cập vào tệp tin", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
