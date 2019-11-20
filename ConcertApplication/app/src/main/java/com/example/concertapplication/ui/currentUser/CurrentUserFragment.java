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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CurrentUserFragment extends Fragment implements RecyclerAdapter.OnConcertListener{

    private CurrentUserViewModel mViewModel;

    private String username;
    private TextView usernameView;
    private Button logoutButton;

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

        usernameView = root.findViewById(R.id.userUsername);
        username = sharedPreferences.getString(getString(R.string.currentUserUsername), "");
        usernameView.setText(username);
        logoutButton = root.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO logout request to server
                editor.putString(getString(R.string.token), "");
                editor.putString(getString(R.string.currentUserUsername), "");
                editor.commit();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                DashboardFragment dashboardFragment  = new DashboardFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, dashboardFragment);
                fragmentTransaction.commit();
            }
        });

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
        // TODO: Use the ViewModel
    }

    // Function to get concerts and append them to myView
    // TODO implement this with recycleview
    private void jsonParse(final RecyclerAdapter.OnConcertListener onConcertListener){
        String url = "http://10.0.2.2:8080" + "/api/concerts";

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

                                RecycleItem recycleItem = new RecycleItem(id, media, title, description, price);
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
