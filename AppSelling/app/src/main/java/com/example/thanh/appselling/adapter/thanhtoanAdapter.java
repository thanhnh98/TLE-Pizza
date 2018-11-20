package com.example.thanh.appselling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thanh.appselling.R;
import com.example.thanh.appselling.model.SanPham;
import com.example.thanh.appselling.model.SanPhamDaMua;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class thanhtoanAdapter extends BaseAdapter {


    List<SanPhamDaMua> list;
    Context context;
    int layout;
    public thanhtoanAdapter( Context context, int layout,List<SanPhamDaMua> list) {
        this.list = list;
        this.context = context;
        this.layout = layout;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    class Holder{
        @BindView(R.id.tenSanPham_thanhtoan)
        TextView tenSP;
        @BindView(R.id.imgSanpham_thanhtoan)
        ImageView imgSP;
        @BindView(R.id.loaiSanPham_thanhtoan) TextView loaiSP;
        @BindView(R.id.giaSanPham_thanhtoan) TextView giaSP;
        @BindView(R.id.SoLuongSanPham_thanhtoan) TextView soluong;
        public Holder(View view){
            ButterKnife.bind(this,view);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, parent, false);
            thanhtoanAdapter.Holder holder = new thanhtoanAdapter.Holder(view);

            view.setTag(holder);
        }
        thanhtoanAdapter.Holder holder= (thanhtoanAdapter.Holder) view.getTag();
        holder.tenSP.setText(list.get(position).getTenSanPham());
        holder.loaiSP.setText("Loại: "+list.get(position).getLoaiSanPham());

        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        final String gia=decimalFormat.format(Integer.parseInt(list.get(position).getGiaSanPham()));
        holder.giaSP.setText("Giá: "+gia+" Đ");

        int id =context.getResources().getIdentifier("com.example.thanh.appselling:drawable/" + list.get(position).getHinhAnhSP(),
                null, null);
        holder.imgSP.setImageResource(id);

        holder.soluong.setText("Số lượng: "+list.get(position).getSoluong());
        return view;
    }
}
