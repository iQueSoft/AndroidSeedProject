package net.iquesoft.project.seed.domain.firebase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.model.UserModel;
import net.iquesoft.project.seed.utils.Constants;

import rx.Observable;
import rx.Observer;

public class MyFirebaseAuth {
    private static MyFirebaseAuth ourInstance;
    private final FirebaseAuth mAuth;
    Observer<String> myObserver = new Observer<String>() {
        @Override
        public void onCompleted() {
            // Called when the observable has no more data to emit
        }

        @Override
        public void onError(Throwable e) {
            // Called when the observable encounters an error
        }

        @Override
        public void onNext(String s) {
            // Called each time the observable emits data
        }
    };
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

    public FirebaseAuth.AuthStateListener getAuthListener(final Observable observable) {
        return new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userModel.setUserEmail(user.getEmail());
                    userModel.setUserId(user.getUid());
                    userModel.setUserName(user.getDisplayName());
                    userModel.setUserPhotoUrl(user.getPhotoUrl());
                    observable.subscribe(myObserver);
                }
            }
        };
    }

    public boolean getEmailAndPasswordAuthentication(Context context, String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            userModel.setErrorCode(Constants.ERROR_AUTH_FAILED);
                        }
                    }
                }).isComplete();
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

    public void signInWithCredentials(Context context, AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                        }
                    }
                });
    }

    public boolean createUserWithEmailAndPassword(Context context, String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            userModel.setErrorCode(Constants.ERROR_AUTH_FAILED);
                        }
                    }
                }).isComplete();
    }

    public CallbackManager getFacebookCallbackManager() {
        return CallbackManager.Factory.create();
    }

}
