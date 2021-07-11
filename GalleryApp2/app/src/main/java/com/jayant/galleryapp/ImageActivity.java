package com.jayant.galleryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ImageView fullImageView;

    String src;

    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        fullImageView = findViewById(R.id.full_image_view);

        src = getIntent().getStringExtra("img");
        Glide.with(this).load(src).into(fullImageView);

        PRDownloader.initialize(getApplicationContext());


        fab = findViewById(R.id.fab_icon);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ImageActivity.this, "image downloading", Toast.LENGTH_SHORT).show();

                getPermission();


//                BitmapDrawable drawable = (BitmapDrawable) fullImageView.getDrawable();
//                Bitmap bitmap = drawable.getBitmap();
//
//                File filepath = Environment.getExternalStorageDirectory();
//                File dir = new File(filepath.getAbsolutePath() + "/Gallery Image/");
//                dir.mkdir();
//                File file = new File(dir, System.currentTimeMillis() + ".jpg");
//
//                try {
//                    outputStream = new FileOutputStream(file);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                Toast.makeText(ImageActivity.this, "downloading...", Toast.LENGTH_SHORT).show();
//
//                try {
//                    outputStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }
        });

    }

    private void getPermission() {

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(report.areAllPermissionsGranted()) {
                    downloadImage();
                }
                else {
                    Toast.makeText(ImageActivity.this, "Please allow all Permissions.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    private void downloadImage() {

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Downloading...");
        pd.setCancelable(false);
        pd.show();
        File filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        PRDownloader.download(src, filepath.getPath(), URLUtil.guessFileName(src, null, null))
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                        long per = progress.currentBytes*100/ progress.totalBytes;
                        pd.setMessage("Downloading:" + per + "%");
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        Toast.makeText(ImageActivity.this, "Downloading completed.", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onError(Error error) {

                        Toast.makeText(ImageActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                });
    }
}