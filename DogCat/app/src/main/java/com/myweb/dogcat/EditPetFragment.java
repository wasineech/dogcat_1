package com.myweb.dogcat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;


public class EditPetFragment extends Fragment {
    String get_pet_id,get_pet_name,get_pet_gender,get_pet_breed,get_pet_birthday,get_pet_picture,
            pet_kind,pet_gender,pet_breed,pet_birthday,pet_picture,pet_rp,province,pet_name,pet_ng,pet_b,pet_age,pet_a,get_user_id;
    TextView txtName, txtGender , txtBirthday, txtBreed;
    ImageView ShowSelectedImage;
    Button btnRegister, btnDatePicker, btnImage;
    Spinner spnGender;
    String output;
    int mYear, mMonth, mDay, mHour, mMinute , rYear, rMonth, rDay;
//    int PICK_IMAGE_REQUEST = 1;
//    Bitmap bitmap;
//    Uri filePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_pet,null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        get_user_id = getArguments().getString("user_id");
        get_pet_id = getArguments().getString("pet_id");
        get_pet_name = getArguments().getString("pet_name");
        get_pet_gender = getArguments().getString("pet_gender");
        get_pet_breed = getArguments().getString("pet_breed");
        get_pet_birthday = getArguments().getString("pet_birthday");
        get_pet_picture = getArguments().getString("pet_picture");

        txtName = getView().findViewById(R.id.txtName);
        txtBreed = getView().findViewById(R.id.txtBreed);
        spnGender = (Spinner) getView().findViewById(R.id.gender);
        txtBirthday = getView().findViewById(R.id.txtBirthday);
        ShowSelectedImage = getView().findViewById(R.id.imageView);
        btnRegister = getView().findViewById(R.id.btnRegister);
        btnDatePicker = getView().findViewById(R.id.btn_date);
       // btnImage = getView().findViewById(R.id.btnImage);

        txtName.setText(get_pet_name);
        txtBreed.setText(get_pet_breed);
        txtBirthday.setText(get_pet_birthday);

        final String[] genderStr = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapterDeptGender = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, genderStr);


        spnGender.setAdapter(adapterDeptGender);
        if(get_pet_gender.equals("Male")) {
            spnGender.setSelection(0);
        }
        else{
            spnGender.setSelection(1);
        }

        Log.d("pet_gender",get_pet_gender);


        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pet_gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                                rYear = dayOfMonth ;
//                                rMonth = (monthOfYear + 1);
//                                rDay = year ;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pet_image = getStringImage(bitmap);
                pet_name = txtName.getText().toString();
                pet_birthday = txtBirthday.getText().toString();
                pet_breed = txtBreed.getText().toString();
                //pet_gender = txtBreed.getText().toString();

                DateFormat before = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat after = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;

                try {
                    date = before.parse(pet_birthday);
                    output = after.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.d("pet_birthday",pet_birthday);
                Log.d("output_date","op" + output);

                if (pet_name.isEmpty() || pet_gender.isEmpty()|| pet_birthday.isEmpty() ||pet_breed.isEmpty() ) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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
                    //login(email, password);
                    new EditPetFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/edit_pet.php?pet_id=" + get_pet_id);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ProfileFragment ssf = new ProfileFragment();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("user_id", get_user_id);
                    data.putString("pet_id", get_pet_id);
                    data.putString("pet_name", pet_name);
                    data.putString("pet_gender", pet_gender);
                    data.putString("pet_breed", pet_breed);
                    data.putString("pet_birthday", pet_birthday);
                    data.putString("pet_picture", pet_picture);
                    ssf.setArguments(data);
                    ft.replace(R.id.content_fragment, ssf);
                    ft.commit();
                }

            }
        });
    }
    public class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("pet_id", get_pet_id)
                        .add("pet_name", pet_name)
                        .add("pet_breed", pet_breed)
                        .add("pet_birthday", output)
                        .add("pet_gender", pet_gender)
                        .add("pet_name", pet_name)
                        .add("user_id", get_user_id)
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
        public void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null){
               // Toast.makeText(EditPetFragment.this, "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
            }else {
                //Toast.makeText(getActivity(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
