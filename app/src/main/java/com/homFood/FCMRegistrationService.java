package com.homFood;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.homFood.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ma7MouD on 4/30/2018.
 */

public class FCMRegistrationService extends IntentService {

    SharedPreferences preferences;
    String ADD_TOKEN_URL = "https://cookehome.com/CookApp/User/sendToken.php";

    public FCMRegistrationService() {
        super("DisplayNotification");
    }

    public FCMRegistrationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

//        Toast.makeText(this, "yessss", Toast.LENGTH_SHORT).show();
        // get Default Shard Preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // get token from Firebase
        String token = FirebaseInstanceId.getInstance().getToken();
        sendTokenToServer(token);

        // check if intent is null or not if it isn't null we will ger refreshed value and
        // if its true we will override token_sent value to false and apply
//        if (intent.getExtras() != null) {
//            boolean refreshed = intent.getExtras().getBoolean("refreshed");
//            if (refreshed) preferences.edit().putBoolean("token_sent", false).apply();
//        }
//
////        // if token_sent value is false then use method sendTokenToServer to send token to server
//        if (!preferences.getBoolean("token_sent", false))
//            sendTokenToServer(token);

    }

    // method use volley to send token to server and stop the service when done or error happened
    private void sendTokenToServer(final String token) {

        StringRequest request = new StringRequest(Request.Method.POST, ADD_TOKEN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        preferences.edit().putBoolean("token_sent", true).apply();
                        Log.e("Registration Service", "Response : Send Token Success");
                        stopSelf();

                    } else {
                        preferences.edit().putBoolean("token_sent", false).apply();
                        Log.e("Registration Service", "Response : Send Token Failed");
                        stopSelf();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                preferences.edit().putBoolean("token_sent", false).apply();
                Log.e("Registration Service", "Error :Send Token Failed");
                stopSelf();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                Log.d("token", token);
                params.put("user_id", MainActivity.customer_id);
                Log.d("user_id", MainActivity.customer_id);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }
}