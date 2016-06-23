package net.iquesoft.project.seed.domain.firebase;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.model.UserModel;

public class MyFirebaseAuth {

    private static MyFirebaseAuth ourInstance;
    private FirebaseAuth mAuth;
    private UserModel userModel;

    private MyFirebaseAuth() {
        userModel = UserModel.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
    }

    public static MyFirebaseAuth getInstance() {
        if (ourInstance == null) {
            ourInstance = new MyFirebaseAuth();
        }
        return ourInstance;
    }

    public Task<AuthResult> getEmailAndPasswordAuthentication(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build())
                .build();
    }

    public Task<AuthResult> signInWithCredentials(AuthCredential credential) {
        return mAuth.signInWithCredential(credential);

    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);

    }

    public CallbackManager getFacebookCallbackManager() {
        return CallbackManager.Factory.create();
    }

}
