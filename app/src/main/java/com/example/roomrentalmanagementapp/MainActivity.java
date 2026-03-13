package com.example.roomrentalmanagementapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomrentalmanagementapp.controller.PhongController;
import com.example.roomrentalmanagementapp.model.Phong;
import com.example.roomrentalmanagementapp.view.adapter.PhongAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static PhongController controller = new PhongController();

    private PhongAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvTongPhong, tvPhongTrong, tvPhongDaThue, tvDoanhThu;
    private SearchView searchView;
    private Button btnLocTatCa, btnLocTrong, btnLocDaThue;

    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    public static final String EXTRA_POSITION = "position";

    private String tuKhoaHienTai = "";
    private int loaiLocHienTai = 0; // 0=tất cả, 1=trống, 2=đã thuê

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerView);
        tvTongPhong = findViewById(R.id.tvTongPhong);
        tvPhongTrong = findViewById(R.id.tvPhongTrong);
        tvPhongDaThue = findViewById(R.id.tvPhongDaThue);
        tvDoanhThu = findViewById(R.id.tvDoanhThu);
        searchView = findViewById(R.id.searchView);
        btnLocTatCa = findViewById(R.id.btnLocTatCa);
        btnLocTrong = findViewById(R.id.btnLocTrong);
        btnLocDaThue = findViewById(R.id.btnLocDaThue);
        FloatingActionButton fabThem = findViewById(R.id.fabThem);

        // Setup adapter
        adapter = new PhongAdapter(controller.getDanhSachPhong(), new PhongAdapter.OnPhongClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, AddEditPhongActivity.class);
                intent.putExtra(EXTRA_POSITION, position);
                startActivityForResult(intent, REQUEST_EDIT);
            }

            @Override
            public void onDeleteClick(int position) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa phòng này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            controller.xoaPhong(position);
                            locVaHienThi();
                            capNhatDashboard();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }

            @Override
            public void onTinhTienClick(int position) {
                hienDialogTinhTien(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                tuKhoaHienTai = newText;
                locVaHienThi();
                return true;
            }
        });

        // Nút lọc
        btnLocTatCa.setOnClickListener(v -> { loaiLocHienTai = 0; locVaHienThi(); });
        btnLocTrong.setOnClickListener(v -> { loaiLocHienTai = 1; locVaHienThi(); });
        btnLocDaThue.setOnClickListener(v -> { loaiLocHienTai = 2; locVaHienThi(); });

        // FAB thêm
        fabThem.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditPhongActivity.class);
            intent.putExtra(EXTRA_POSITION, -1);
            startActivityForResult(intent, REQUEST_ADD);
        });

        capNhatDashboard();
        locVaHienThi();
    }

    private void locVaHienThi() {
        List<Phong> ketQua = controller.timKiem(tuKhoaHienTai, loaiLocHienTai);
        adapter.capNhatDanhSach(ketQua);
    }

    private void capNhatDashboard() {
        NumberFormat fmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        tvTongPhong.setText(String.valueOf(controller.getDanhSachPhong().size()));
        tvPhongTrong.setText(String.valueOf(controller.getSoPhongTrong()));
        tvPhongDaThue.setText(String.valueOf(controller.getSoPhongDaThue()));
        tvDoanhThu.setText(fmt.format(controller.getDoanhThuDuKien()) + " đ");
    }
}