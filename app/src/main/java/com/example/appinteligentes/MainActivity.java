package com.example.appinteligentes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    Button btnEncode,btnDecode;
    TextView textView;
    TextView imagePrediction;
    ImageView imageView;
    String sImage;
    ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEncode=findViewById(R.id.btn_encode);
        btnDecode=findViewById(R.id.btn_decode);
        textView=findViewById(R.id.textView);
        imageView= findViewById(R.id.imageView);
        imagePrediction =  findViewById(R.id.imagePrediction);
        loadingPB = findViewById(R.id.LoadingPB);

        // Code for Encode button
        btnEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check condition
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    // when permission is nor granted
                    // request permission
                    ActivityCompat.requestPermissions(MainActivity.this
                            , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

                }
                else
                {
                    // when permission
                    // is granted
                    // create method
                    selectImage();
                }
            }

        });

        // Code for Decode button
        btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // decode base64 string
                byte[] bytes=Base64.decode(sImage,Base64.DEFAULT);
                // Initialize bitmap
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                // set bitmap on imageView

                postData(sImage);


            }


        });
    }
    private void selectImage() {
        // clear previous data
        textView.setText("");
        imageView.setImageBitmap(null);
        // Initialize intent
        Intent intent=new Intent(Intent.ACTION_PICK);
        // set type
        intent.setType("image/*");
        // start activity result
        startActivityForResult(Intent.createChooser(intent,"Select Image"),100);
    }

    private void postData(String image) {

        // below line is for displaying our progress bar.
        loadingPB.setVisibility(View.VISIBLE);

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.77:5000/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        // passing data from our text fields to our modal class.
        ImageModel imageModel = new ImageModel(image);

        // calling a method to create a post and passing our modal class.
        Call<ResponseModel> call = retrofitAPI.createPost(imageModel);

        // on below line we are executing our method.
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                // this method is called when we get response from our api.
                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                // on below line we are setting empty text
                // to our both edit text.

                // we are getting response from our body
                // and passing it to our modal class.
                ResponseModel responseFromAPI = response.body();

                // on below line we are getting our data from modal class and adding it to our string.
                String responseString =  "La pelota es : " + responseFromAPI.getClase()+ "\n" + responseFromAPI.getProbabilidades() ;

                // below line we are setting our
                // string to our text view.
                imagePrediction.setText(responseString);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                imagePrediction.setText("Error found is : " + t.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // check condition
        if (requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            // when permission
            // is granted
            // call method
            selectImage();
        }
        else
        {
            // when permission is denied
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check condition
        if (requestCode==100 && resultCode==RESULT_OK && data!=null)
        {
            // when result is ok
            // initialize uri
            Uri uri=data.getData();
            // Initialize bitmap
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                // initialize byte stream
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                // Initialize byte array
                byte[] bytes=stream.toByteArray();
                // get base64 encoded string
                sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
                // set encoded text on textview
                textView.setText(sImage);


                byte[] bytes2=Base64.decode(sImage,Base64.DEFAULT);
                // Initialize bitmap
                Bitmap bitmap2= BitmapFactory.decodeByteArray(bytes,0,bytes2.length);
                // set bitmap on imageView
                imageView.setImageBitmap(bitmap2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}