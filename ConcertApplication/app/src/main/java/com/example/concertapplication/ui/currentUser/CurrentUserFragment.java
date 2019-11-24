package com.example.concertapplication.ui.currentUser;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.concertapplication.R;
import com.example.concertapplication.singletons.MySingleton;
import com.example.concertapplication.ui.concert.ConcertFragment;
import com.example.concertapplication.ui.dashboard.DashboardFragment;
import com.example.concertapplication.ui.recycleItem.RecycleItem;
import com.example.concertapplication.ui.recycleItem.RecyclerAdapter;
import com.example.concertapplication.ui.usersConcert.UsersConcertFragment;
import com.example.concertapplication.ui.usersConcert.UsersConcertViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CurrentUserFragment extends Fragment implements RecyclerAdapter.OnConcertListener{

    private CurrentUserViewModel mViewModel;

    // access to jwt
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // initialize myqueue to get instance of singleton
    private RequestQueue myQueue;
    private ArrayList<RecycleItem> concertArray;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static CurrentUserFragment newInstance() {
        return new CurrentUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.current_user_fragment, container, false);

        sharedPreferences = getContext().getSharedPreferences("com.example.concertapplication", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        recyclerView = root.findViewById(R.id.userRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        concertArray = new ArrayList<>();

        // Get the singleton and call jsonparse
        myQueue = MySingleton.getInstance(root.getContext()).getRequestQueue();
        jsonParse(this);

        Log.d("concertArray content: ", concertArray.toString());

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CurrentUserViewModel.class);
    }

    // Function to get concerts and append them to myView
    private void jsonParse(final RecyclerAdapter.OnConcertListener onConcertListener){
        String url = "http://10.0.2.2:8080" + "/api/user/orders";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("concerts");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject concert = jsonArray.getJSONObject(i);

                                int id = concert.getInt("id");
                                String title = concert.getString("title");
                                String description = concert.getString("description");
                                String media = concert.getString("media");
                                int price = concert.getInt("price");
                                double lng = concert.getDouble("lng");
                                double lat = concert.getDouble("lat");
                                String datetime = concert.getString("date");

                                RecycleItem recycleItem = new RecycleItem(id, media, title, description, price, lat, lng, datetime);
                                Log.d("item: ", recycleItem.toString());
                                concertArray.add(recycleItem);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            adapter = new RecyclerAdapter(concertArray, onConcertListener);
                            recyclerView.setAdapter(adapter);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + sharedPreferences.getString(getString(R.string.token), "").toString());
                return headers;
            }

        };



        myQueue.add(request);

    }



    @Override
    public void onConcertClick(int position) throws NullPointerException{
        try {
            RecycleItem item = concertArray.get(position);
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());
            bundle.putString("title", item.getText1());
            bundle.putString("media", item.getImageResource());
            bundle.putString("description", item.getText2());
            bundle.putInt("price", item.getPrice());
            bundle.putDouble("lng", item.getLng());
            bundle.putDouble("lat", item.getLat());
            bundle.putString("datetime", item.getTimestamp().toString());

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            UsersConcertFragment usersConcertFragment  = new UsersConcertFragment();
            usersConcertFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.nav_host_fragment, usersConcertFragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
