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

import com.example.concertapplication.R;
import com.example.concertapplication.ui.dashboard.DashboardFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ConcertFragment extends Fragment {

    private ConcertViewModel mViewModel;

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

        titleView = root.findViewById(R.id.concertTitle);
        descriptionView = root.findViewById(R.id.concertDescription);
        imageView = root.findViewById(R.id.concertImage);

        titleView.setText(title);
        Log.d("TitleView: ", titleView.getText().toString());
        descriptionView.setText(description);
        Picasso.get().load(Uri.parse(media)).into(imageView);

        buyButton = root.findViewById(R.id.buyButton);

        buyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (sharedPreferences.getString(getString(R.string.token), "") != "") {
                    // TODO add check to server if token timed out

                    // TODO if token timed out - send to login

                    // TODO if token valid - send to my concerts
                    Log.d("Has token", "USER IS LOGGED IN");
                } else {
                    // TODO send to login
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    DashboardFragment dashboardFragment  = new DashboardFragment();
                    fragmentTransaction.replace(R.id.nav_host_fragment, dashboardFragment);
                    fragmentTransaction.commit();
                }
            }
        });

        // return inflater.inflate(R.layout.concert_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ConcertViewModel.class);


    }

}
