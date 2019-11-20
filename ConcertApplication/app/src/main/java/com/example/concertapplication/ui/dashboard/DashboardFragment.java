package com.example.concertapplication.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.concertapplication.R;
import com.example.concertapplication.singletons.MySingleton;
import com.example.concertapplication.ui.createUser.CreateUserFragment;
import com.example.concertapplication.ui.currentUser.CurrentUserFragment;
import com.example.concertapplication.ui.notifications.NotificationsFragment;
import com.example.concertapplication.ui.recycleItem.RecycleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private TextView usernameText;
    private TextView passwordText;
    private Button loginButton;
    private RequestQueue myQueue;
    private Button createUserButton;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sharedPreferences = getContext().getSharedPreferences("com.example.concertapplication", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // TODO add check if token valid
        if(sharedPreferences.getString(getString(R.string.token), "") != ""){
            loadLoggedInView();
        }

        usernameText = root.findViewById(R.id.usernameInput);
        passwordText = root.findViewById(R.id.passwordInput);
        loginButton = root.findViewById(R.id.loginButton);
        createUserButton = root.findViewById(R.id.createUserButton);

        createUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                CreateUserFragment createUserFragment  = new CreateUserFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, createUserFragment);
                fragmentTransaction.commit();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myQueue = MySingleton.getInstance(root.getContext()).getRequestQueue();
                handleLogin(usernameText.getText().toString(), passwordText.getText().toString());
            }
        });

        return root;
    }

    private void handleLogin(final String username, String password){
        String url = "http://10.0.2.2:8080" + "/api/authenticate";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String jwt = response.getString("jwt");
                            Log.d("Response", jwt);
                            editor.putString(getString(R.string.token), jwt);
                            editor.commit();
                            editor.putString(getString(R.string.currentUserUsername), username);
                            editor.commit();
                            logSharedPreferenceToken();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        loadLoggedInView();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO print wrong uname or password
                error.printStackTrace();
            }
        });
        myQueue.add(request);
    }

    // TODO check if bearer is actually stored
    private void logSharedPreferenceToken(){
        Log.d("JWT IN SHARED PREF", sharedPreferences.getString(getString(R.string.token), ""));
    }

    private void loadLoggedInView(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        CurrentUserFragment currentUserFragment  = new CurrentUserFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment, currentUserFragment);
        fragmentTransaction.commit();
    }
}
