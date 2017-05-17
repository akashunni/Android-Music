package com.quintlr.music;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

/**
 * Created by akash on 19/2/17.
 */

public class GoogleAPI implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient googleApiClient = null;
    private GoogleSignInOptions googleSignInOptions;
    private String TAG = "akash";
    private static GoogleAPI googleAPI = null;

    static GoogleAPI getInstance(){
        if (googleAPI == null){
            googleAPI = new GoogleAPI();
        }
        return googleAPI;
    }

    void createGoogleSignInOptions(){
        googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(Drive.SCOPE_APPFOLDER)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
    }

    void buildGoogleApiClient(Context context){
        googleApiClient = null;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Drive.API)
                .build();
    }

    GoogleApiClient getGoogleApiClient(){
        return googleApiClient;
    }

    void connect(){
        if (!googleApiClient.isConnected()){
            Log.d(TAG, "connecting....");
            googleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
        }
    }

    void disconnect(){
        if (googleApiClient.isConnected()){
            Log.d(TAG, "disconnecting...");
            googleApiClient.disconnect();
        }
    }

    boolean isConnected(){
        return googleApiClient.isConnected();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }
}
