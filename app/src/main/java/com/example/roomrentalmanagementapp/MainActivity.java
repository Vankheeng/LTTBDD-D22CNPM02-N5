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
import android.os.Bundle;

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
    private void hienDialogTinhTien(int position) {
        Phong p = adapter.getPhongTaiViTri(position);
        NumberFormat fmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // Layout dialog nhập chỉ số điện
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 24);

        TextView tvInfo = new TextView(this);
        tvInfo.setText("Phòng: " + p.getTenPhong() + "\nNgười thuê: " + p.getTenNguoiThue());
        tvInfo.setPadding(0, 0, 0, 16);
        layout.addView(tvInfo);

        EditText etCu = new EditText(this);
        etCu.setHint("Chỉ số điện cũ (kWh)");
        etCu.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        etCu.setText(String.valueOf((int) p.getChiSoDienCu()));
        layout.addView(etCu);

        EditText etMoi = new EditText(this);
        etMoi.setHint("Chỉ số điện mới (kWh)");
        etMoi.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        etMoi.setText(String.valueOf((int) p.getChiSoDienMoi()));
        layout.addView(etMoi);

        new AlertDialog.Builder(this)
                .setTitle("💰 Tính tiền phòng")
                .setView(layout)
                .setPositiveButton("Tính", (dialog, which) -> {
                    try {
                        double cu = Double.parseDouble(etCu.getText().toString());
                        double moi = Double.parseDouble(etMoi.getText().toString());
                        if (moi < cu) {
                            Toast.makeText(this, "Chỉ số mới phải lớn hơn cũ!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        p.setChiSoDienCu(cu);
                        p.setChiSoDienMoi(moi);

                        double tienDien = p.tinhTienDien();
                        double tongTien = p.tinhTongTienThang();

                        // Hiện kết quả + nút gửi SMS
                        String thongBao = "🏠 Tiền phòng: " + fmt.format(p.getGiaThue()) + " đ\n"
                                + "⚡ Tiền điện: " + fmt.format(tienDien) + " đ\n"
                                + "   (" + (int)(moi-cu) + " kWh × " + fmt.format(Phong.getDonGiaDien()) + " đ)\n"
                                + "━━━━━━━━━━━━\n"
                                + "💰 TỔNG: " + fmt.format(tongTien) + " đ";

                        new AlertDialog.Builder(this)
                                .setTitle("Hóa đơn - " + p.getTenPhong())
                                .setMessage(thongBao)
                                .setPositiveButton("📱 Gửi SMS", (d2, w2) -> {
                                    guiSMS(p.getSoDienThoai(), thongBao);
                                })
                                .setNeutralButton("📞 Zalo", (d2, w2) -> {
                                    moZalo(p.getSoDienThoai());
                                })
                                .setNegativeButton("Đóng", null)
                                .show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Vui lòng nhập đúng chỉ số điện!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void guiSMS(String sdt, String noiDung) {
        try {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.setData(Uri.parse("smsto:" + sdt));
            smsIntent.putExtra("sms_body", noiDung);
            startActivity(smsIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở ứng dụng SMS!", Toast.LENGTH_SHORT).show();
        }
    }

    private void moZalo(String sdt) {
        try {
            Intent zaloIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://zalo.me/" + sdt));
            startActivity(zaloIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở Zalo!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            locVaHienThi();
            capNhatDashboard();
        }
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