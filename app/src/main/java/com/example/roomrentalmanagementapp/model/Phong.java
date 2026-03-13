package com.example.roomrentalmanagementapp.model;

public class Phong {
    private String maPhong;
    private String tenPhong;
    private double giaThue;
    private boolean daThue;
    private String tenNguoiThue;
    private String soDienThoai;
    private double chiSoDienCu;   // thêm mới
    private double chiSoDienMoi;  // thêm mới
    private static final double DON_GIA_DIEN = 3500; // đồng/kWh

    public Phong(String maPhong, String tenPhong, double giaThue,
                 boolean daThue, String tenNguoiThue, String soDienThoai) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.giaThue = giaThue;
        this.daThue = daThue;
        this.tenNguoiThue = tenNguoiThue;
        this.soDienThoai = soDienThoai;
        this.chiSoDienCu = 0;
        this.chiSoDienMoi = 0;
    }

    public double tinhTienDien() {
        return (chiSoDienMoi - chiSoDienCu) * DON_GIA_DIEN;
    }

    public double tinhTongTienThang() {
        return giaThue + tinhTienDien();
    }

    // Getters & Setters cũ
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    public double getGiaThue() { return giaThue; }
    public void setGiaThue(double giaThue) { this.giaThue = giaThue; }
    public boolean isDaThue() { return daThue; }
    public void setDaThue(boolean daThue) { this.daThue = daThue; }
    public String getTenNguoiThue() { return tenNguoiThue; }
    public void setTenNguoiThue(String tenNguoiThue) { this.tenNguoiThue = tenNguoiThue; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    // Getters & Setters mới
    public double getChiSoDienCu() { return chiSoDienCu; }
    public void setChiSoDienCu(double chiSoDienCu) { this.chiSoDienCu = chiSoDienCu; }
    public double getChiSoDienMoi() { return chiSoDienMoi; }
    public void setChiSoDienMoi(double chiSoDienMoi) { this.chiSoDienMoi = chiSoDienMoi; }
    public static double getDonGiaDien() { return DON_GIA_DIEN; }
}
