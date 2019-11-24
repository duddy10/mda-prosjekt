package com.example.concertapplication.ui.myUser;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.Map;

public class MyUserFragment extends Fragment{

    private MyUserViewModel mViewModel;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String username;
    private TextView usernameView;
    private Button logoutButton;

    private TextView firstName;
    private TextView lastName;
    private TextView phoneNumber;
    private TextView email;
    private TextView role;

    private Button userCancle;
    private Button userUpdate;
    private Button validateTicketButton;

    private RequestQueue myQueue;

    private MyUserFragmentListener myUserFragmentListener;

    public interface MyUserFragmentListener{
        void onInputMyUserFragmentSent(String input);
    }

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

        firstName = root.findViewById(R.id.userFirstName);
        lastName = root.findViewById(R.id.userLastName);
        phoneNumber = root.findViewById(R.id.userPhoneNumber);
        email = root.findViewById(R.id.userEmail);
        role = root.findViewById(R.id.userRole);

        userUpdate = root.findViewById(R.id.userUpdate);
        userCancle = root.findViewById(R.id.userCancle);

        validateTicketButton = root.findViewById(R.id.validateTicketButton);

        myQueue = MySingleton.getInstance(root.getContext()).getRequestQueue();

        getUserInformation();


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO logout request to server
                editor.putString(getString(R.string.token), "");
                editor.putString(getString(R.string.currentUserUsername), "");
                editor.commit();

                loadLoginPage();
            }
        });

        userUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateUserInformation();
            }
        });

        userCancle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getUserInformation();
            }
        });

        validateTicketButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String instruction = "ADM";
                myUserFragmentListener.onInputMyUserFragmentSent(instruction);
            }
        });

        return root;
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof MyUserFragmentListener){
            myUserFragmentListener = (MyUserFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MyUserFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myUserFragmentListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyUserViewModel.class);
        // TODO: Use the ViewModel
    }

    private void loadLoginPage(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("fragmentId", "MyUserFragment");
        DashboardFragment dashboardFragment  = new DashboardFragment();
        dashboardFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment, dashboardFragment);
        fragmentTransaction.commit();
    }

    private void getUserInformation(){
        String url = "https://concert-backend-heroku.herokuapp.com" + "/api/user/information";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            firstName.setText(response.getString("firstName"));
                            lastName.setText(response.getString("lastName"));
                            phoneNumber.setText(response.getString("phoneNumber"));
                            email.setText(response.getString("email"));
                            role.setText(response.getString("role"));
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void updateUserInformation(){
        String url = "https://concert-backend-heroku.herokuapp.com" + "/api/user/update";

        JSONObject jsonObject = new JSONObject();


        // get user information to update
        try {
            jsonObject.put("firstName", firstName.getText());
            jsonObject.put("lastName", lastName.getText());
            jsonObject.put("phoneNumber", phoneNumber.getText());
            jsonObject.put("email", email.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            firstName.setText(response.getString("firstName"));
                            lastName.setText(response.getString("lastName"));
                            phoneNumber.setText(response.getString("phoneNumber"));
                            email.setText(response.getString("email"));
                            role.setText(response.getString("role"));
                            if( response.getString("role") =="ADM"){
                                validateTicketButton.setVisibility(View.VISIBLE);

                            } else {
                                validateTicketButton.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                Log.d("MINTAG", headers.toString());
                return headers;
            }
        };
        myQueue.add(request);
    }
}
