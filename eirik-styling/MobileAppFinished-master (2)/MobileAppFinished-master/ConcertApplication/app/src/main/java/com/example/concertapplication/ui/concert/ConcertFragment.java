package com.example.concertapplication.ui.concert;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.concertapplication.R;
import com.example.concertapplication.singletons.MySingleton;
import com.example.concertapplication.ui.currentUser.CurrentUserFragment;
import com.example.concertapplication.ui.dashboard.DashboardFragment;
import com.example.concertapplication.ui.myUser.MyUserFragment;
import com.example.concertapplication.ui.recycleItem.RecycleItem;
import com.example.concertapplication.ui.recycleItem.RecyclerAdapter;
import com.example.concertapplication.ui.usersConcert.UsersConcertFragment;
import com.example.concertapplication.ui.usersConcert.UsersConcertViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ConcertFragment extends Fragment implements OnMapReadyCallback {

    private ConcertViewModel mViewModel;

    private static final String MAPVIEW_BUNDLE_KEY = "google_maps_key";

    private MapView mMapView;
    private GoogleMap googleMap;


    // concert info
    private int id;
    private String title;
    private String media;
    private String description;
    private int price;

    // concert elements
    private TextView titleView;
    private TextView descriptionView;
    private ImageView imageView;
    private Button buyButton;

    // access to jwt
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private double lat;
    private double lng;
    private String datetime;

    private RequestQueue myQueue;



    public static ConcertFragment newInstance() {
        return new ConcertFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.concert_fragment, container, false);

        sharedPreferences = getContext().getSharedPreferences("com.example.concertapplication", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // get information passed from previous fragment
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        title = bundle.getString("title");
        media = bundle.getString("media");
        description = bundle.getString("description");
        price = bundle.getInt("price");
        lat = bundle.getDouble("lat");
        lng = bundle.getDouble("lng");
        datetime = bundle.getString("datetime");

        titleView = root.findViewById(R.id.concertTitle);
        descriptionView = root.findViewById(R.id.concertDescription);
        imageView = root.findViewById(R.id.concertImage);
        mMapView = root.findViewById(R.id.mapsview);

        titleView.setText(title);
        Log.d("TitleView: ", titleView.getText().toString());
        descriptionView.setText(description);
        Picasso.get().load(Uri.parse(media)).into(imageView);

        buyButton = root.findViewById(R.id.buyButton);

        myQueue = MySingleton.getInstance(root.getContext()).getRequestQueue();

        buyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (sharedPreferences.getString(getString(R.string.token), "") != "") {
                    onHandleBuy();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    CurrentUserFragment currentUserFragment  = new CurrentUserFragment();
                    fragmentTransaction.replace(R.id.nav_host_fragment, currentUserFragment);
                    fragmentTransaction.commit();

                } else {
                    // TODO send to login
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    DashboardFragment dashboardFragment  = new DashboardFragment();
                    fragmentTransaction.replace(R.id.nav_host_fragment, dashboardFragment);
                    fragmentTransaction.commit();
                }
            }
        });

        initGoogleMap(savedInstanceState);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ConcertViewModel.class);


    }

    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        Log.d("lat, lng", lat + ", " + lng);
        LatLng latLng = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(latLng).title("marker"));
        moveToCurrentLocation(latLng);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }

    private void onHandleBuy(){
        String url = "https://concert-backend-heroku.herokuapp.com" + "/api/order/buy";

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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



}
