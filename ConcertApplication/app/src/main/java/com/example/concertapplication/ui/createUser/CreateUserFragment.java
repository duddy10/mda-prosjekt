package com.example.concertapplication.ui.createUser;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.concertapplication.ui.dashboard.DashboardFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class CreateUserFragment extends Fragment {

    private CreateUserViewModel mViewModel;

    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView phoneNumber;
    private TextView username;
    private TextView password;

    private Button createUserButton;

    private RequestQueue myQueue;

    public static CreateUserFragment newInstance() {
        return new CreateUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_user_fragment, container, false);

        firstName = root.findViewById(R.id.createFirstName);
        lastName = root.findViewById(R.id.createLastName);
        email = root.findViewById(R.id.createEmail);
        phoneNumber = root.findViewById(R.id.createPhoneNumber);
        username = root.findViewById(R.id.createUsername);
        password = root.findViewById(R.id.createPassword);

        createUserButton = root.findViewById(R.id.createSubmit);

        myQueue = MySingleton.getInstance(root.getContext()).getRequestQueue();


        createUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                handleCreateUser();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreateUserViewModel.class);
        // TODO: Use the ViewModel
    }

    private void handleCreateUser(){
        String url = "https://concert-backend-heroku.herokuapp.com" + "/api/user/create";

        JSONObject jsonObject = new JSONObject();


        // get user information to update
        try {
            jsonObject.put("firstName", firstName.getText());
            jsonObject.put("lastName", lastName.getText());
            jsonObject.put("phoneNumber", phoneNumber.getText());
            jsonObject.put("email", email.getText());
            jsonObject.put("username", username.getText());
            jsonObject.put("password", password.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("fragmentId", "MyUserFragment");
                        DashboardFragment dashboardFragment  = new DashboardFragment();
                        dashboardFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.nav_host_fragment, dashboardFragment);
                        fragmentTransaction.commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        myQueue.add(request);
    }
}
