package com.myweb.dogcat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ProfileFragment extends Fragment {
    TextView textTitle, textPetName, textPetBreed, textPetAge ;
    ImageView ImgPet;
    String getUser_id;
    Button btnEdit;
    ProgressBar progressBar;
    // Adding HTTP Server URL to string variable.
    String pet_id, pet_name,pet_gender,pet_breed,pet_age,pet_birthday,pet_picture,user_id,output;
    Bitmap bitmap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getUser_id = getArguments().getString("user_id");

        textTitle = getView().findViewById(R.id.pet_title_name);
        textPetName = getView().findViewById(R.id.pet_name);
        textPetBreed = getView().findViewById(R.id.pet_breed);
        textPetAge =  getView().findViewById(R.id.pet_age);
        ImgPet = getView().findViewById(R.id.pet_profile);
        btnEdit = getView().findViewById(R.id.btnEdit);

        //Toast.makeText(getActivity().getApplicationContext(), getUser_id, Toast.LENGTH_SHORT).show();
        loadPetData();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat after = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat before = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;

                try {
                    date = before.parse(pet_birthday);
                    output = after.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                EditPetFragment ssf = new EditPetFragment();
                Bundle data = new Bundle();//create bundle instance
                data.putString("user_id", getUser_id);
                data.putString("pet_id", pet_id);
                data.putString("pet_name", pet_name);
                data.putString("pet_gender", pet_gender);
                data.putString("pet_breed", pet_breed);
                data.putString("pet_birthday", output);
                ssf.setArguments(data);
                ft.replace(R.id.content_fragment, ssf);
                ft.commit();
            }

    });
    }


    private void loadPetData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get.php?user_id=" + getUser_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showJSON(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                pet_id = collectData.getString("pet_id");
                pet_name = collectData.getString("pet_name");
                pet_gender = collectData.getString("pet_gender");
                pet_breed = collectData.getString("pet_breed");
                pet_age = collectData.getString("pet_age");
                pet_birthday = collectData.getString("pet_birthday");
                pet_picture = collectData.getString("pet_picture");

//                String ImagePath = "http://"+ConfigIP.IP+"/dogcat/upload/ei.jpg";
                //File sd = Environment.getExternalStorageDirectory();
                //File imgFile = new  File("http://"+ConfigIP.IP+"/upload/ei.jpg");
//                bitmap = BitmapFactory.decodeFile(ImagePath);



                textTitle.setText(pet_name);
                textPetName.setText(pet_name+"(เพศ: "+pet_gender+")");
                textPetBreed.setText("สายพันธ์: " + pet_breed);
                textPetAge.setText("อายุ: " + pet_age);
                Picasso.get().load("http://"+ConfigIP.IP+"/dogcat/"+pet_picture).into(ImgPet);
//                ImgPet.setImageBitmap(bitmap);


            }

        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

}
