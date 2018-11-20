package com.example.thanh.appselling.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.thanh.appselling.DAO.LoaiSanPhamDAO;
import com.example.thanh.appselling.PopUp.PopUp_MuaHang;
import com.example.thanh.appselling.R;
import com.example.thanh.appselling.SQLiteHelper.SQLiteHelper;
import com.example.thanh.appselling.adapter.AdapterLoaiSP;
import com.example.thanh.appselling.adapter.NewSanPhamAdapter;
import com.example.thanh.appselling.adapter.PizzaAdapter;
import com.example.thanh.appselling.model.SanPham;
import com.example.thanh.appselling.model.loaiSP;
import com.example.thanh.appselling.ultil.CheckConnection;
import com.example.thanh.appselling.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.toolbarManHinhChinh) Toolbar toolbarManHinhchinh;
    @BindView(R.id.viewflipperManHinhChinh) ViewFlipper viewflipperManHinhChinh;
    @BindView(R.id.navigationManHinhChinh) NavigationView navigationManHinhChinh;
    @BindView(R.id.listviewNavigationManHinhChinh) ListView listviewNavigationManHinhChinh;
    @BindView(R.id.drawerlayoutManHinhChinh) DrawerLayout drawerlayoutManHinhChinh;
    @BindView(R.id.recyclerviewManHinhChinh) RecyclerView recyclerView;
    @BindView(R.id.ButtonPizza) LinearLayout ButtonPizza;
    @BindView(R.id.ButtonThucAn) LinearLayout ButtonThucAn;
    @BindView(R.id.ButtonThucUong) LinearLayout ButtonThucUong;
    @BindView(R.id.ButtonInfo) LinearLayout ButtonInfo;
    List<loaiSP> loaiSPS;
    AdapterLoaiSP adapter;
    loaiSP loaiSP;
    int id=0;
    public static int phanloai=0;
    String tenloaisanpham="";
    String hinhanhloaisanpham="";

    List<SanPham> listSPMoi;
    NewSanPhamAdapter adapterNewSanPham;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listSPMoi = new ArrayList<SanPham>();
        ButtonPizza.setOnClickListener(this);
        ButtonThucAn.setOnClickListener(this);
        ButtonThucUong.setOnClickListener(this);
       // ButtonInfo.setOnClickListener(this);

        getSPMoi();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapterNewSanPham = new NewSanPhamAdapter(this,listSPMoi);
        recyclerView.setAdapter(adapterNewSanPham);
        adapterNewSanPham.notifyDataSetChanged();


        ActionBar();
        ActionViewFlipper();
    }
    //GET bằng SQLite
   /* private void getDuLieuSanPham() {
        LoaiSanPhamDAO loaiSanPhamDAO=new LoaiSanPhamDAO(getApplicationContext());
        loaiSanPhamDAO.open();
       // loaiSanPhamDAO.getDsLoaiSanPham();
        loaiSPS = loaiSanPhamDAO.getDsLoaiSanPham();
        adapter.notifyDataSetChanged();
    }*/

    //Get bằng LOcal JSON
    /*private void getDuLieuSanPham() {
        InputStream inputStream=         getResources().openRawResource(R.raw.loaisanpham); //Lấy file bằng inputstream

        InputStreamReader reader =new InputStreamReader(inputStream); //đọc inputstreeam
        BufferedReader bufferedReader=new BufferedReader(reader); //đọc file từ reader
        String dong="";
        StringBuilder dulieu=new StringBuilder();
        try {
            while ((dong = bufferedReader.readLine()) != null) {  //Phải bắt try catch IOException
                dulieu.append(dong);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        String json=dulieu.toString();
        //-------------Đọc Json---------------------
        try {
            JSONArray jsonArray= new JSONArray(json);
            if(jsonArray!=null) {
                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    id = jsonObject.getInt("id");
                    tenloaisanpham = jsonObject.getString("tenloaisanpham");
                    hinhanhloaisanpham = jsonObject.getString("hinhanhloaisanpham");
                    loaiSPS.add(new loaiSP(id, tenloaisanpham, hinhanhloaisanpham));
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            Log.e("Lỗi ở đây",e.toString());
            e.printStackTrace();
        }
    }*/
    //Get bằng JSON from MySQL
    /*private void getDuLieuSanPham() {
        final RequestQueue requestQueue =Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Server.linkLoaiSP, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(requestQueue!=null){
                    for(int i=0;i<response.length();++i){
                        try {
                            JSONObject jsonObject=response.getJSONObject(i);
                            id=jsonObject.getInt("id");
                            tenloaisanpham=jsonObject.getString("tenloaisanpham");
                            hinhanhloaisanpham=jsonObject.getString("tenloaisanpham");
                            loaiSPS.add(new loaiSP(id,tenloaisanpham,hinhanhloaisanpham));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToast_Connect(getApplicationContext(),"Errors");
            }
        });
        requestQueue.add(jsonArrayRequest);
    }*/

    private void ActionViewFlipper() {
        ArrayList<String> imageQuangCao=new ArrayList<>();
        imageQuangCao.add("https://www.google.com.vn/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwjewO-7487eAhXEc94KHRr7Av0QjRx6BAgBEAU&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FPizza&psig=AOvVaw32H1JLX9T110BbnLhRiqWs&ust=1542109435830340");
        imageQuangCao.add("https://www.google.com.vn/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwjco9LR487eAhWXF4gKHV9SDZIQjRx6BAgBEAU&url=https%3A%2F%2Fwww.messforless.net%2F2-ingredient-pizza-dough%2F&psig=AOvVaw32H1JLX9T110BbnLhRiqWs&ust=1542109435830340");
        imageQuangCao.add("https://www.google.com.vn/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwilkY3b487eAhVVBIgKHT6dBCMQjRx6BAgBEAU&url=http%3A%2F%2Fekonomi.haber7.com%2Fekonomi%2Fhaber%2F2633469-pizza-pizza-satildi-iste-yeni-sahibi&psig=AOvVaw32H1JLX9T110BbnLhRiqWs&ust=1542109435830340");
        /*for(int i=0;i<imageQuangCao.size();++i){
            Log.e("OK","1");
            ImageView imageView=new ImageView(getApplicationContext());
            Picasso.get().load(imageQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewflipperManHinhChinh.addView(imageView);
        }*/
        ImageView imageView=new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.pizza26);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        viewflipperManHinhChinh.addView(imageView);
        ImageView imageView2=new ImageView(getApplicationContext());
        imageView2.setImageResource(R.drawable.pizza27);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        viewflipperManHinhChinh.addView(imageView2);
        ImageView imageView3=new ImageView(getApplicationContext());
        imageView3.setImageResource(R.drawable.pizza28);
        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
        viewflipperManHinhChinh.addView(imageView3);
        viewflipperManHinhChinh.setFlipInterval(4000);
        viewflipperManHinhChinh.setAutoStart(true);
        Animation animation_slide_right=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rightflipper);
        Animation animation_slide_left=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.leftflipper);
        viewflipperManHinhChinh.setInAnimation(animation_slide_left);
        viewflipperManHinhChinh.setOutAnimation(animation_slide_right);
    }

    @SuppressLint("ResourceType")
    private void ActionBar() {
        setSupportActionBar(toolbarManHinhchinh);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.homebutton);
        toolbarManHinhchinh.setNavigationContentDescription(android.R.drawable.ic_menu_sort_by_size);
        toolbarManHinhchinh.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlayoutManHinhChinh.openDrawer(GravityCompat.START);
            }
        });
    }
    public void getSPMoi(){
        listSPMoi.add(new SanPham("pizza1","Mushroom","200000"));
        listSPMoi.add(new SanPham("pizza2","Cucumber","150000"));
        listSPMoi.add(new SanPham("pizza3","Radish","120000"));
        listSPMoi.add(new SanPham("pizza4","Sausage","100000"));
        listSPMoi.add(new SanPham("pizza5","Cheesepork","220000"));
        listSPMoi.add(new SanPham("pizza6","Mixed","350000"));
        listSPMoi.add(new SanPham("pizza7","Pepperoni","240000"));
        listSPMoi.add(new SanPham("pizza8","Porksmall","190000"));
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this,PizzaActivity.class);
        switch (v.getId()){
            case R.id.ButtonPizza:
                intent.putExtra("phanloai",1);
                intent.putExtra("title","Pizza");
                startActivity(intent);
                break;
            case R.id.ButtonThucAn:
                intent.putExtra("phanloai",2);
                intent.putExtra("title","Thức Ăn Nhanh");
                startActivity(intent);
                break;
            case R.id.ButtonThucUong:
                intent.putExtra("phanloai",3);
                intent.putExtra("title","Thức Uống");
                startActivity(intent);
                break;
            case R.id.ButtonInfo:
                startActivity(new Intent(MainActivity.this,PopUp_MuaHang.class));
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Xác nhận dừng mua hàng")
                .setMessage("Tiếp tục mua hàng?")
                .setPositiveButton("Thoát", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Tiếp tục", null)
                .show();
    }
}
