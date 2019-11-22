package com.example.concertapplication.ui.myUser;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.concertapplication.R;
import com.example.concertapplication.ui.currentUser.CurrentUserFragment;
import com.example.concertapplication.ui.dashboard.DashboardFragment;

public class MyUserFragment extends Fragment {

    private MyUserViewModel mViewModel;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String username;
    private TextView usernameView;
    private Button logoutButton;


    public static MyUserFragment newInstance() {
        return new MyUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.my_user_fragment, container, false);


        sharedPreferences = getContext().getSharedPreferences("com.example.concertapplication", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // TODO add check if token valid
        if(sharedPreferences.getString(getString(R.string.token), "") == ""){
            loadLoginPage();
        }

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




        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyUserViewModel.class);
        // TODO: Use the ViewModel
    }

    private void loadLoginPage(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DashboardFragment dashboardFragment  = new DashboardFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment, dashboardFragment);
        fragmentTransaction.commit();
    }
}
