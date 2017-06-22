package com.example.levanduc.orderfood.Khach_Hang;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Dell on 4/18/2017.
 */

public class Mon_Chon{
    public int Id;
    public String TenMonC;
    public int GiaC;
    public int SoLuongC;
    public String GhiChuC;
    public int ThanhTienC;
    public int trangthai =0;

    public Mon_Chon(){}

    public Mon_Chon(int id, String tenMon, int gia, int soLuong, String ghiChu, int thanhTienC) {
        Id = id;
        TenMonC = tenMon;
        GiaC = gia;
        SoLuongC = soLuong;
        GhiChuC = ghiChu;
        ThanhTienC = thanhTienC;
    }
    public Mon_Chon(int id, String tenMon, int gia, int soLuong, String ghiChu, int thanhTienC, int trangthai) {
        Id = id;
        TenMonC = tenMon;
        GiaC = gia;
        SoLuongC = soLuong;
        GhiChuC = ghiChu;
        ThanhTienC = thanhTienC;
        this.trangthai = trangthai;
    }
}
