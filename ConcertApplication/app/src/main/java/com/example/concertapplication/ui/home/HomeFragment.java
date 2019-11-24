package com.example.concertapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.concertapplication.R;
import com.example.concertapplication.singletons.MySingleton;
import com.example.concertapplication.ui.concert.ConcertFragment;
import com.example.concertapplication.ui.recycleItem.RecycleItem;
import com.example.concertapplication.ui.recycleItem.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements RecyclerAdapter.OnConcertListener {

    private HomeViewModel homeViewModel;

    // initialize myqueue to get instance of singleton
    private RequestQueue myQueue;
    private ArrayList<RecycleItem> concertArray;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        concertArray = new ArrayList<>();

        // Get the singleton and call jsonparse
        myQueue = MySingleton.getInstance(root.getContext()).getRequestQueue();
        jsonParse(this);

        return root;
    }

    // Function to get concerts and append them to myView
    // TODO implement this with recycleview
    private void jsonParse(final RecyclerAdapter.OnConcertListener onConcertListener){
        String url = "https://concert-backend-heroku.herokuapp.com" + "/api/concerts";

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
                                double lat = concert.getDouble("lat");
                                double lng = concert.getDouble("lng");
                                String timestamp = concert.getString("date");

                                RecycleItem recycleItem = new RecycleItem(id, media, title, description, price, lat, lng, timestamp);
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
        });

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
            bundle.putDouble("lat", item.getLat());
            bundle.putDouble("lng", item.getLng());
            bundle.putString("date", item.getTimestamp());

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            ConcertFragment concertFragment  = new ConcertFragment();
            concertFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.nav_host_fragment, concertFragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}