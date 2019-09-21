package com.myweb.dogcat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class ShowSearchFragment extends Fragment {
    String get_pet_kind,get_pet_gender,get_pet_breed,get_province,pet_kind,pet_gender,pet_breed,pet_picture,pet_rp,province,pet_name,pet_ng,pet_b,pet_age,pet_a;
    List<RowItem_show> rowItems;
    RecyclerView showsearch_rv;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_search,null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        get_pet_kind = getArguments().getString("pet_kind");
        get_pet_gender = getArguments().getString("pet_gender");
        get_pet_breed = getArguments().getString("pet_breed");
        get_province = getArguments().getString("province");

        Log.d("get_province",get_province);

        showsearch_rv = getView().findViewById(R.id.list2);
        showsearch_rv.setHasFixedSize(true);
        showsearch_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        showsearch_rv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));


        Toast.makeText(getContext(), get_pet_kind + get_pet_gender + get_pet_breed + get_province,Toast.LENGTH_SHORT).show();
        rowItems = new ArrayList<>();
        loadShowPet();

    }

    private void loadShowPet(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_showpet.php?pet_kind=" + get_pet_kind + "&pet_gender=" + get_pet_gender + "&pet_breed=" + get_pet_breed + "&province=" + get_province;
        Log.d("get_url",url);
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

                pet_name = collectData.getString("pet_name");
                pet_kind = collectData.getString("pet_kind");
                pet_gender = collectData.getString("pet_gender");
                pet_age = collectData.getString("pet_age");
                pet_breed = collectData.getString("pet_breed");
                pet_picture = collectData.getString("pet_picture");
                pet_rp = "http://"+ConfigIP.IP+"/dogcat/"+pet_picture;
                pet_ng = "ชื่อ: " + pet_name + " " + "(" + pet_gender + ")";
                pet_b = "สายพันธ์: " + pet_breed;
                pet_a = "อายุ: " +  pet_age ;

                Log.d("petname",pet_name+pet_breed);
                Log.d("pet_picture",pet_rp);
                Log.d("pet_age",pet_a);
//                province = collectData.getString("province");


                    RowItem_show item = new RowItem_show(pet_ng, pet_b,pet_a, pet_rp);
                    rowItems.add(item);

                //Log.d("item",item);

               // showsearch_listview.setOnItemClickListener(this);

            }


            ShowSearchAdapter adapter = new ShowSearchAdapter(getActivity(), rowItems);
            showsearch_rv.setAdapter(adapter);

        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /*public void onItemClick(@NonNull AdapterView<?> parent, @NonNull View view, @NonNull int position,
                            long id) {

        String member_name = rowItems.get(position).getPet_kind();
    }*/


}
