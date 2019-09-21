package com.myweb.dogcat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class SearchFragment extends Fragment {
    String pet_kind,pet_gender,pet_breed,province,url;
    Button btnSearch;
    RadioGroup kindGroup;
    RadioButton kindButton;
    TextView txtBreed;
    Spinner spnGender,spnProvince;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        spnGender = (Spinner) getView().findViewById(R.id.gender);
        spnProvince = (Spinner) getView().findViewById(R.id.province);
        txtBreed = getView().findViewById(R.id.txtBreed);
        btnSearch = getView().findViewById(R.id.btnSearch);

        final String[] genderStr = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapterDeptGender = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, genderStr);

        spnGender.setAdapter(adapterDeptGender);
        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pet_gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final String[] pvStr = getResources().getStringArray(R.array.province);
        ArrayAdapter<String> adapterDeptProvince = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, pvStr);

        spnProvince.setAdapter(adapterDeptProvince);
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                province = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kindGroup = (RadioGroup) getView().findViewById(R.id.radio);
                int selectedId = kindGroup.getCheckedRadioButtonId();
                kindButton = (RadioButton) getView().findViewById(selectedId);

                pet_kind = kindButton.getText().toString();
                pet_breed = txtBreed.getText().toString();

                if (pet_kind.isEmpty() || pet_gender.isEmpty() || pet_gender.isEmpty() || pet_breed.isEmpty() || province.isEmpty()) {
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
                    Toast.makeText(getContext(), pet_kind + pet_kind + pet_gender + pet_breed + province,Toast.LENGTH_SHORT).show();
                    //search(pet_kind,pet_gender,pet_breed,province);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ShowSearchFragment ssf = new ShowSearchFragment();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("pet_kind", pet_kind);
                    data.putString("pet_gender", pet_gender);
                    data.putString("pet_breed", pet_breed);
                    data.putString("province", province);
                    ssf.setArguments(data);
                    ft.replace(R.id.content_fragment, ssf);
                    ft.commit();
                }

            }
        });
    }
}
