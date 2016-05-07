package com.example.weirdo.photosexporting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {
    private TextView mTextDetails;

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    public FacebookCallback<LoginResult> getmCallback() {
        return mCallback;
    }

    public FacebookCallback<LoginResult> mCallback=new FacebookCallback<LoginResult>() {
        // successful login print a hello plus username and display the albums names in a grid
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken= loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null){
                mTextDetails.setText("Welcome" + profile.getName());


                /** in order to fetch the albums (albums ids and names and our case)
                 * the result is two arrays one contains the album ids and the other the names

                 */
            GraphRequestAsyncTask graphRequest = new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                String [] albumName = null;
                                String [] albumId = null;
                                try{
                                    JSONObject json = response.getJSONObject();
                                    JSONArray jarray = json.getJSONArray("data");
                                    for(int i = 0; i < jarray.length(); i++) {
                                        JSONObject oneAlbum = jarray.getJSONObject(i);
                                        //get albums names


                                        albumName[i] = oneAlbum.getString("name");
                                        albumId[i] = oneAlbum.getString("id");
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }


                            }
                        }
                ).executeAsync();

            }

        }



        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken) {

            }
        };

         mProfileTracker=new ProfileTracker() {
             //keeping track of the users
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayWelcomeMessage(newProfile);

            }
        };
        mTokenTracker.startTracking();
        mProfileTracker.startTracking();
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void displayWelcomeMessage(Profile profile){
        if (profile != null){
            mTextDetails.setText("Welcome" + profile.getName());
    }

    }
    // using the facebook  LoginButton class, It follows the current access token and can log people in and out.
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_photos");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);
    }
    // in case of screen rotation this method keeps the welcome message on the screen, otherwise it disappears
    public void onResume(){
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
