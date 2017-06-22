package com.example.levanduc.orderfood.Quan_Ly;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.levanduc.orderfood.Dau_Bep.Dau_bep;
import com.example.levanduc.orderfood.Nhan_Vien.Nhan_vien;
import com.example.levanduc.orderfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Blank3Fragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    Button login;
    AutoCompleteTextView mEmailView;
    EditText mPasswordView;
    private FirebaseAuth firebaseAuth;
    SliderLayout sliderLayout;
    HashMap<String, Integer> imgslide ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank3, container, false);
        login = (Button) view.findViewById(R.id.loginall);
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        mPasswordView = (EditText) view.findViewById(R.id.password);
        Toast.makeText(getActivity(), "Mục Quản Lý Không Dành Cho Khách Hàng", Toast.LENGTH_SHORT).show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mEmailView.getText().toString().equals("quanly") && mPasswordView.getText().toString().equals("1234")) {
                    Intent i = new Intent(getActivity(), Quan_ly.class);
                    startActivity(i);
                } else if (mEmailView.getText().toString().equals("daubep") && mPasswordView.getText().toString().equals("1234")) {
                    Intent i = new Intent(getActivity(), Dau_bep.class);
                    startActivity(i);
                } else {

                    firebaseAuth = FirebaseAuth.getInstance();
                    String email = mEmailView.getText().toString();
                    final String password = mPasswordView.getText().toString();
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getActivity(), "Nhập Email", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getActivity(), "Nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Vui Lòng Chờ...", "Đang Xử Lý...", true);
                    (firebaseAuth.signInWithEmailAndPassword(email, password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Đăng Nhập Thành Công", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getActivity(), Nhan_vien.class);
                                i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                startActivity(i);
                            } else {
                                Log.e("Lỗi", task.getException().toString().trim());
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });


                }
            }
        });




        imgslide = new HashMap<>();

        sliderLayout = (SliderLayout)view.findViewById(R.id.slider);

        imgslide.put("Nước cam",R.drawable.nuoc);
        imgslide.put("Nước cam",R.drawable.nuoc);
        imgslide.put("Nước cam",R.drawable.nuoc);
        imgslide.put("Nước cam",R.drawable.nuoc);
        imgslide.put("Nước cam",R.drawable.nuoc);

        for(String name : imgslide.keySet()){

            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .description(name)
                    .image(imgslide.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);







        return view;
    }

    @Override
    public void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }
    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
