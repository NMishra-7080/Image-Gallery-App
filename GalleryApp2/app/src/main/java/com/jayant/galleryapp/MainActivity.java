package com.jayant.galleryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<String> list;
    RecyclerView recyclerView;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recycler_view);
//        List<Photo> l;


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        adapter = new ImageAdapter(l);
//        recyclerView.setAdapter(adapter);


        apiInterface = RetrofitInstance.getRetrofit().create(ApiInterface.class);
        list = new ArrayList<>();


        apiInterface.getPost().enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {

                PostData responseBody = response.body();

                if (responseBody != null) {
                    Toast.makeText(MainActivity.this, "not null", Toast.LENGTH_SHORT).show();
                    List<Photo> l = responseBody.getPhotos().getPhoto();
                    Log.d("jayant", l.get(1).getUrl_s());

                    adapter = new ImageAdapter(l, getApplicationContext());
                    recyclerView.setAdapter(adapter);

                }
                else {
                    Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {

                Log.d("res", t.toString());
            }
        });


        Toast.makeText(this, "Welcome to this app", Toast.LENGTH_SHORT).show();


    }
}