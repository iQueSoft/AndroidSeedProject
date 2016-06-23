package net.iquesoft.project.seed.presentation.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.domain.RegularExpressionValidation;
import net.iquesoft.project.seed.domain.exception.DefaultErrorBundle;
import net.iquesoft.project.seed.domain.exception.EmptyFieldException;
import net.iquesoft.project.seed.domain.exception.ErrorMessageFactory;
import net.iquesoft.project.seed.domain.exception.InvalidEmailException;
import net.iquesoft.project.seed.domain.exception.InvalidPasswordException;
import net.iquesoft.project.seed.domain.firebase.MyFirebaseAuth;
import net.iquesoft.project.seed.presentation.model.UserModel;
import net.iquesoft.project.seed.presentation.view.fragment.LoginFragment;
import net.iquesoft.project.seed.utils.Constants;

public class UserLoginPresenter implements Presenter, OnCompleteListener {

    private static UserLoginPresenter ourInstance;
    private final MyFirebaseAuth firebaseAuth;
    private RegularExpressionValidation validation;
    private Context context;
    private UserModel userModel;
    private LoginFragment fragmentView;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private UserLoginPresenter(Context context) {
        this.context = context;
        userModel = UserModel.getInstance();
        firebaseAuth = MyFirebaseAuth.getInstance();
        validation = new RegularExpressionValidation();
        mGoogleApiClient = getGoogleApiClient(context);
    }

    public static UserLoginPresenter getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UserLoginPresenter(context);
        }
        return ourInstance;
    }

    public void setView(LoginFragment view) {
        this.fragmentView = view;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
    }

    public void initFirebaseListener() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    fragmentView.updateUI(user);
                } else {
                    fragmentView.updateUI(null);
                }
            }
        };
    }

    public FirebaseAuth.AuthStateListener getAuthListener() {
        return mAuthListener;

    }

    public GoogleApiClient getGoogleApiClient(Context context) {
        if (userModel.getGoogleApiClient() == null) {
            userModel.setGoogleApiClient(firebaseAuth.getGoogleApiClient(context));
        }
        return userModel.getGoogleApiClient();
    }

    public void signInWithCredentials(AuthCredential credential) {
        firebaseAuth.signInWithCredentials(credential).addOnCompleteListener(this);
    }

    public CallbackManager getFacebookCallbackManager() {
        if (userModel.getFacebookCallback() == null)
            userModel.setFacebookCallback(firebaseAuth.getFacebookCallbackManager());
        return userModel.getFacebookCallback();
    }


    public void logInWithEmailAndPassword(String email, String password) {
        this.fragmentView.showLoading();
        this.fragmentView.clearTextField();
        try {
            validation.isEmailValid(email);
            userModel.setUserEmail(email);
        } catch (InvalidEmailException | EmptyFieldException e) {
            this.fragmentView.hideLoading();
            showEmailError(new DefaultErrorBundle(e));
            return;
        }
        try {
            validation.isPasswordValid(password);
            userModel.setUserPassword(password);
        } catch (EmptyFieldException | InvalidPasswordException e) {
            this.fragmentView.hideLoading();
            showPasswordError(new DefaultErrorBundle(e));
            return;
        }

        firebaseAuth.getEmailAndPasswordAuthentication(email, password).addOnCompleteListener((Activity) context, this);
    }

    private void showPasswordError(DefaultErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(context,
                errorBundle.getException());
        this.fragmentView.showPasswordError(errorMessage);
    }

    private void showEmailError(DefaultErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(context,
                errorBundle.getException());
        this.fragmentView.showEmailError(errorMessage);
    }


    @Override
    public void onComplete(@NonNull Task task) {
        fragmentView.hideLoading();
        if (!task.isSuccessful()) {
            LoginManager.getInstance().logOut();
            fragmentView.updateUI(null);
            try {
                throw task.getException();
            } catch (Exception e) {
                fragmentView.showError(e.getMessage());
            }
        }
    }

    public void logInWithGoogle(FragmentActivity activity) {
        this.fragmentView.showLoading();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    public void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                signInWithCredentials(credential);
            }
        } else {
            // Signed out, show unauthenticated UI.
            fragmentView.hideLoading();
            fragmentView.updateUI(null);
            fragmentView.showError(context.getString(R.string.error_message_authentication_error));
        }
    }

    public void registerFacebookCallback(LoginButton loginButton) {
        getFacebookCallbackManager();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(getFacebookCallbackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                signInWithCredentials(credential);
            }

            @Override
            public void onCancel() {
                fragmentView.updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                fragmentView.showError(error.getMessage());
                fragmentView.updateUI(null);
            }
        });
    }
}
