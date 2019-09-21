package com.myweb.dogcat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;


import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AddPetActivity extends AppCompatActivity {
    String pet_kind,pet_name,pet_gender,pet_birthday,pet_breed,pet_image,email,password,url,g_email,g_password,g_user_id;
    Button btnRegister, btnDatePicker, btnImage;
    int mYear, mMonth, mDay, mHour, mMinute;
    RadioGroup kindGroup;
    RadioButton kindButton;
    TextView txtName, txtGender , txtBirthday, txtBreed;
    Spinner spnGender;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    Uri filePath;

    ImageView ShowSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pet);
        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            email = _bundle.getString("email");
            password = _bundle.getString("password");
        }

        txtName = findViewById(R.id.txtName);
        spnGender = (Spinner) findViewById(R.id.gender);
        txtBirthday = findViewById(R.id.txtBirthday);
        txtBreed = findViewById(R.id.txtBreed);
        btnRegister = findViewById(R.id.btnRegister);
        btnDatePicker = findViewById(R.id.btn_date);
        btnImage = findViewById(R.id.btnImage);
        ShowSelectedImage = (ImageView)findViewById(R.id.imageView);

        final String[] genderStr = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapterDept = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, genderStr);

        spnGender.setAdapter(adapterDept);
        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position,long id){

                pet_gender = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPetActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kindGroup = findViewById(R.id.radio);
                int selectedId = kindGroup.getCheckedRadioButtonId();
                kindButton = findViewById(selectedId);


                //pet_image = getStringImage(bitmap);
                pet_kind = kindButton.getText().toString();
                pet_name = txtName.getText().toString();
                pet_birthday = mYear+"-"+mMonth+"-"+mDay;
                pet_breed = txtBreed.getText().toString();

                if (pet_kind.isEmpty() || pet_name.isEmpty() || pet_gender.isEmpty()|| pet_birthday.isEmpty() ||pet_breed.isEmpty() ) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddPetActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    builder.setCancelable(true);
                    builder.setMessage("กรุณากรอกข้อมูลให้ครบ");
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    //GetImageNameFromEditText = imageName.getText().toString();

                    //UploadImageToServer();
                    //--------------------
                    login(email, password);
                }

            }
        });
    }

    public void login(final String email, final String password) {

        url = "http://"+ConfigIP.IP+"/dogcat/login.php?email=" + email + "&password=" + password;
        RequestQueue requestQueue = Volley.newRequestQueue(AddPetActivity.this);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);


                    g_email = jsonObject1.getString("email");
                    g_password = jsonObject1.getString("password");
                    g_user_id = jsonObject1.getString("user_id");

                    new AddPetActivity.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_pet.php?user_id=" + g_user_id);
                    Intent intent = new Intent(AddPetActivity.this, MainActivity.class);
                    startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddPetActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    builder.setCancelable(true);
                    builder.setMessage("อีเมลล์หรือรหัสผิด");
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HiteshURLerror", "" + error);
            }
        });

        requestQueue.add(stringRequest);


    }
    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                pet_image = getStringImage(bitmap);
                RequestBody _requestBody = new FormBody.Builder()
                        .add("pet_name", pet_name)
                        .add("pet_kind", pet_kind)
                        .add("pet_breed", pet_breed)
                        .add("pet_birthday", pet_birthday)
                        .add("pet_gender", pet_gender)
                        .add("pet_picture", pet_image)
                        .build();

                Request _request = new Request.Builder().url(strings[0]).post(_requestBody).build();
                _okHttpClient.newCall(_request).execute();
                return "successfully";

            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null){
                Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ShowSelectedImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    //------------------------------------------------
//    private void showPictureDialog(){
//        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
//        pictureDialog.setTitle("Select Action");
//        String[] pictureDialogItems = {
//                "Photo Gallery",
//                "Camera" };
//        pictureDialog.setItems(pictureDialogItems,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                choosePhotoFromGallary();
//                                break;
//                            case 1:
//                                takePhotoFromCamera();
//                                break;
//                        }
//                    }
//                });
//        pictureDialog.show();
//    }
//    public void choosePhotoFromGallary() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(galleryIntent, GALLERY);
//    }
//
//    private void takePhotoFromCamera() {
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == this.RESULT_CANCELED) {
//            return;
//        }
//        if (requestCode == GALLERY) {
//            if (data != null) {
//                Uri contentURI = data.getData();
//                try {
//                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    // String path = saveImage(bitmap);
//                    //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    ShowSelectedImage.setImageBitmap(FixBitmap);
//                    btnImage.setVisibility(View.VISIBLE);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(  AddPetActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        } else if (requestCode == CAMERA) {
//            FixBitmap = (Bitmap) data.getExtras().get("data");
//            ShowSelectedImage.setImageBitmap(FixBitmap);
//            btnImage.setVisibility(View.VISIBLE);
//            //  saveImage(thumbnail);
//            //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    public void UploadImageToServer(){
//
//        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
//
//        byteArray = byteArrayOutputStream.toByteArray();
//
//        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
//
//        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {
//
//            @Override
//            protected void onPreExecute() {
//
//                super.onPreExecute();
//
//                progressDialog = ProgressDialog.show(  AddPetActivity.this,"Image is Uploading","Please Wait",false,false);
//            }
//
//            @Override
//            protected void onPostExecute(String string1) {
//
//                super.onPostExecute(string1);
//
//                progressDialog.dismiss();
//
//                Toast.makeText(  AddPetActivity.this,string1,Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//
//                AddPetActivity.ImageProcessClass imageProcessClass = new  AddPetActivity.ImageProcessClass();
//
//                HashMap<String,String> HashMapParams = new HashMap<String,String>();
//
//                HashMapParams.put( "image_tag", "genae");
//
//                HashMapParams.put("image_data", ConvertImage);
//
//                String FinalData = imageProcessClass.ImageHttpRequest("http://"+ConfigIP.IP+"/upload.php", HashMapParams);
//
//                return FinalData;
//            }
//        }
//        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
//        AsyncTaskUploadClassOBJ.execute();
//    }
//
//    public class ImageProcessClass{
//
//        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {
//
//            StringBuilder stringBuilder = new StringBuilder();
//
//            try {
//                urls = new URL(requestURL);
//
//                httpURLConnection = (HttpURLConnection) urls.openConnection();
//
//                httpURLConnection.setReadTimeout(20000);
//
//                httpURLConnection.setConnectTimeout(20000);
//
//                httpURLConnection.setRequestMethod("POST");
//
//                httpURLConnection.setDoInput(true);
//
//                httpURLConnection.setDoOutput(true);
//
//                outputStream = httpURLConnection.getOutputStream();
//
//                bufferedWriter = new BufferedWriter(
//
//                        new OutputStreamWriter(outputStream, "UTF-8"));
//
//                bufferedWriter.write(bufferedWriterDataFN(PData));
//
//                bufferedWriter.flush();
//
//                bufferedWriter.close();
//
//                outputStream.close();
//
//                RC = httpURLConnection.getResponseCode();
//
//                if (RC == HttpsURLConnection.HTTP_OK) {
//
//                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//
//                    stringBuilder = new StringBuilder();
//
//                    String RC2;
//
//                    while ((RC2 = bufferedReader.readLine()) != null){
//
//                        stringBuilder.append(RC2);
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return stringBuilder.toString();
//        }
//
//        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
//
//            stringBuilder = new StringBuilder();
//
//            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
//                if (check)
//                    check = false;
//                else
//                    stringBuilder.append("&");
//
//                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
//
//                stringBuilder.append("=");
//
//                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
//            }
//
//            return stringBuilder.toString();
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 5) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Now user should be able to use camera
//
//            }
//            else {
//
//                Toast.makeText( AddPetActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();
//
//            }
//        }
//    }
}
