package com.example.roomrentalmanagementapp.controller;
import com.example.roomrentalmanagementapp.model.Phong;
import java.util.ArrayList;
import java.util.List;

public class PhongController {
    private List<Phong> danhSachPhong;
    public PhongController() {
        danhSachPhong = new ArrayList<>();
        danhSachPhong.add(new Phong("P01", "Phòng 101", 2500000, false, "", ""));
        danhSachPhong.add(new Phong("P02", "Phòng 102", 3000000, true, "Nguyễn Văn A", "0901234567"));
        danhSachPhong.add(new Phong("P03", "Phòng 201", 2800000, true, "Trần Thị B", "0912345678"));
        danhSachPhong.add(new Phong("P04", "Phòng 202", 2600000, false, "", ""));
    }

    public List<Phong> getDanhSachPhong() { return danhSachPhong; }

    public void themPhong(Phong phong) { danhSachPhong.add(phong); }

    public void suaPhong(int viTri, Phong phong) { danhSachPhong.set(viTri, phong); }

    public void xoaPhong(int viTri) { danhSachPhong.remove(viTri); }

    public boolean kiemTraMaPhong(String ma, int boQuaViTri) {
        for (int i = 0; i < danhSachPhong.size(); i++) {
            if (i == boQuaViTri) continue;
            if (danhSachPhong.get(i).getMaPhong().equalsIgnoreCase(ma)) return false;
        }
        return true;
    }

}
