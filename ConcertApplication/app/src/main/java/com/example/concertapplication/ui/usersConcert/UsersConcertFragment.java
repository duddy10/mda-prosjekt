package com.example.concertapplication.ui.usersConcert;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.concertapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class UsersConcertFragment extends Fragment implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "google_maps_key";

    private UsersConcertViewModel mViewModel;
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

    // access to jwt
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private double lat;
    private double lng;
    private String datetime;

    private int orderId;

    private UsersConcertFragmentListener usersConcertFragmentListener;

    public interface UsersConcertFragmentListener{
        void onInputUsersConcertFragmentSent(Integer input);
    }

    public UsersConcertFragment() {
    }

    public static UsersConcertFragment newInstance() {
        return new UsersConcertFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.users_concert_fragment, container, false);


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
        orderId = bundle.getInt("orderId");



        titleView = root.findViewById(R.id.usersConcertTitle);
        descriptionView = root.findViewById(R.id.usersConcertDescription);
        imageView = root.findViewById(R.id.usersConcertImage);

        mMapView = root.findViewById(R.id.mapsview);


        titleView.setText(title);
        descriptionView.setText(description + "\n DATE: " + datetime);
        Picasso.get().load(Uri.parse(media)).into(imageView);



        initGoogleMap(savedInstanceState);

        usersConcertFragmentListener.onInputUsersConcertFragmentSent(orderId);


        return root;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof UsersConcertFragment.UsersConcertFragmentListener){
            usersConcertFragmentListener = (UsersConcertFragment.UsersConcertFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement UsersConcertFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        usersConcertFragmentListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UsersConcertViewModel.class);
        // TODO: Use the ViewModel
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

}
