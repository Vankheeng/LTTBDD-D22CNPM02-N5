package com.example.roomrentalmanagementapp.view.adapter;


import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roomrentalmanagementapp.MainActivity;
import com.example.roomrentalmanagementapp.model.Phong;
import com.example.roomrentalmanagementapp.R;
public class AddEditPhongActivity extends AppCompatActivity {

    private EditText etMaPhong, etTenPhong, etGiaThue, etTenNguoiThue, etSoDienThoai;
    private RadioGroup rgTrangThai;
    private RadioButton rbDaThue;
    private LinearLayout layoutNguoiThue;
    private Button btnLuu;

    private int viTri = -1; // -1 = thêm mới

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_phong);

        etMaPhong = findViewById(R.id.etMaPhong);
        etTenPhong = findViewById(R.id.etTenPhong);
        etGiaThue = findViewById(R.id.etGiaThue);
        etTenNguoiThue = findViewById(R.id.etTenNguoiThue);
        etSoDienThoai = findViewById(R.id.etSoDienThoai);
        rgTrangThai = findViewById(R.id.rgTrangThai);
        rbDaThue = findViewById(R.id.rbDaThue);
        layoutNguoiThue = findViewById(R.id.layoutNguoiThue);
        btnLuu = findViewById(R.id.btnLuu);
        Button btnHuy = findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(v -> finish());

        // Hiện/ẩn form người thuê
        rgTrangThai.setOnCheckedChangeListener((group, checkedId) -> {
            layoutNguoiThue.setVisibility(
                    checkedId == R.id.rbDaThue ? View.VISIBLE : View.GONE
            );
        });

        viTri = getIntent().getIntExtra(MainActivity.EXTRA_POSITION, -1);

        if (viTri >= 0) {
            // Chế độ sửa
            setTitle("Sửa Phòng");
            Phong p = MainActivity.controller.getDanhSachPhong().get(viTri);
            etMaPhong.setText(p.getMaPhong());
            etTenPhong.setText(p.getTenPhong());
            etGiaThue.setText(String.valueOf(p.getGiaThue()));
            if (p.isDaThue()) {
                rbDaThue.setChecked(true);
                layoutNguoiThue.setVisibility(View.VISIBLE);
                etTenNguoiThue.setText(p.getTenNguoiThue());
                etSoDienThoai.setText(p.getSoDienThoai());
            }
        } else {
            setTitle("Thêm Phòng Mới");
        }

        btnLuu.setOnClickListener(v -> luuPhong());
    }

    private void luuPhong() {
        String ma = etMaPhong.getText().toString().trim();
        String ten = etTenPhong.getText().toString().trim();
        String giaStr = etGiaThue.getText().toString().trim();

        // Validate
        if (ma.isEmpty()) { etMaPhong.setError("Vui lòng nhập mã phòng"); return; }
        if (ten.isEmpty()) { etTenPhong.setError("Vui lòng nhập tên phòng"); return; }
        if (giaStr.isEmpty()) { etGiaThue.setError("Vui lòng nhập giá thuê"); return; }

        double gia;
        try {
            gia = Double.parseDouble(giaStr);
            if (gia <= 0) { etGiaThue.setError("Giá phải > 0"); return; }
        } catch (NumberFormatException e) {
            etGiaThue.setError("Giá không hợp lệ"); return;
        }

        // Kiểm tra mã trùng
        if (!MainActivity.controller.kiemTraMaPhong(ma, viTri)) {
            etMaPhong.setError("Mã phòng đã tồn tại"); return;
        }

        boolean daThue = rbDaThue.isChecked();
        String tenNguoiThue = "", sdt = "";
        if (daThue) {
            tenNguoiThue = etTenNguoiThue.getText().toString().trim();
            sdt = etSoDienThoai.getText().toString().trim();
            if (tenNguoiThue.isEmpty()) { etTenNguoiThue.setError("Nhập tên người thuê"); return; }
            if (sdt.isEmpty()) { etSoDienThoai.setError("Nhập số điện thoại"); return; }
        }

        Phong phong = new Phong(ma, ten, gia, daThue, tenNguoiThue, sdt);

        if (viTri >= 0) {
            MainActivity.controller.suaPhong(viTri, phong);
            Toast.makeText(this, "Đã cập nhật phòng!", Toast.LENGTH_SHORT).show();
        } else {
            MainActivity.controller.themPhong(phong);
            Toast.makeText(this, "Đã thêm phòng mới!", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }
}