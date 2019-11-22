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
import com.squareup.picasso.Picasso;

public class UsersConcertFragment extends Fragment {

    private UsersConcertViewModel mViewModel;

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

        titleView = root.findViewById(R.id.usersConcertTitle);
        descriptionView = root.findViewById(R.id.usersConcertDescription);
        imageView = root.findViewById(R.id.usersConcertImage);


        titleView.setText(title);
        Log.d("TitleView: ", titleView.getText().toString());
        descriptionView.setText(description);
        Picasso.get().load(Uri.parse(media)).into(imageView);


        // return inflater.inflate(R.layout.concert_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UsersConcertViewModel.class);
        // TODO: Use the ViewModel
    }

}
