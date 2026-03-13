package com.example.roomrentalmanagementapp.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomrentalmanagementapp.R;
import com.example.roomrentalmanagementapp.model.Phong;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PhongAdapter extends RecyclerView.Adapter<PhongAdapter.PhongViewHolder> {

    public interface OnPhongClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onTinhTienClick(int position);
    }

    private List<Phong> danhSachGoc;
    private List<Phong> danhSachHienThi; // filtered list
    private OnPhongClickListener listener;

    public PhongAdapter(List<Phong> danhSach, OnPhongClickListener listener) {
        this.danhSachGoc = danhSach;
        this.danhSachHienThi = new ArrayList<>(danhSach);
        this.listener = listener;
    }

    public void capNhatDanhSach(List<Phong> danhSachMoi) {
        this.danhSachHienThi = new ArrayList<>(danhSachMoi);
        notifyDataSetChanged();
    }

    public Phong getPhongTaiViTri(int position) {
        return danhSachHienThi.get(position);
    }

    // Tìm vị trí thật trong danh sách gốc
    public int getViTriGoc(int position) {
        Phong p = danhSachHienThi.get(position);
        return danhSachGoc.indexOf(p);
    }

    @NonNull
    @Override
    public PhongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_phong, parent, false);
        return new PhongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhongViewHolder holder, int position) {
        Phong p = danhSachHienThi.get(position);
        NumberFormat fmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        holder.tvTenPhong.setText(p.getTenPhong() + " (" + p.getMaPhong() + ")");
        holder.tvGiaThue.setText(fmt.format(p.getGiaThue()) + " đ/tháng");

        if (p.isDaThue()) {
            holder.tvTrangThai.setText("Đã thuê - " + p.getTenNguoiThue());
            holder.tvTrangThai.setTextColor(Color.parseColor("#F44336"));
            holder.viewTrangThai.setBackgroundColor(Color.parseColor("#F44336"));
            holder.btnTinhTien.setVisibility(View.VISIBLE);
        } else {
            holder.tvTrangThai.setText("Còn trống");
            holder.tvTrangThai.setTextColor(Color.parseColor("#4CAF50"));
            holder.viewTrangThai.setBackgroundColor(Color.parseColor("#4CAF50"));
            holder.btnTinhTien.setVisibility(View.GONE); // Ẩn nút nếu phòng trống
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(getViTriGoc(holder.getAdapterPosition())));
        holder.btnXoa.setOnClickListener(v -> listener.onDeleteClick(getViTriGoc(holder.getAdapterPosition())));
        holder.btnTinhTien.setOnClickListener(v -> listener.onTinhTienClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() { return danhSachHienThi.size(); }

    static class PhongViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenPhong, tvGiaThue, tvTrangThai;
        View viewTrangThai;
        ImageButton btnXoa;
        Button btnTinhTien;

        PhongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenPhong = itemView.findViewById(R.id.tvTenPhong);
            tvGiaThue = itemView.findViewById(R.id.tvGiaThue);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            viewTrangThai = itemView.findViewById(R.id.viewTrangThai);
            btnXoa = itemView.findViewById(R.id.btnXoa);
            btnTinhTien = itemView.findViewById(R.id.btnTinhTien);
        }
    }
}