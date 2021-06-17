package com.hk.meditechuser;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hk.meditechuser.Model.MyPlace;
import com.hk.meditechuser.Model.Patient;
import com.hk.meditechuser.Model.Results;
import com.hk.meditechuser.Remote.GoogleApiService;
import com.hk.meditechuser.Remote.RetrofitClient;
import com.hk.meditechuser.Remote.RetrofitScalarClient;

import retrofit2.Retrofit;

public class Common {
    public static Patient currentPatient;  //track current patient data
    public static Results currentResults;
    public static final String GOOGLE_API_URL = "https://maps.googleapis.com/";

    public static GoogleApiService getGoogleApiService() {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(GoogleApiService.class);
    }
    public static GoogleApiService getGoogleApiServiceScalars() {
        return RetrofitScalarClient.getScalarClient(GOOGLE_API_URL).create(GoogleApiService.class);
    }
    //internet connection investigate
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
            if (networkInfo != null) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
