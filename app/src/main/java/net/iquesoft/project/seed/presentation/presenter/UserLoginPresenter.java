package net.iquesoft.project.seed.presentation.presenter;

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
import net.iquesoft.project.seed.presentation.view.activity.MainActivity;
import net.iquesoft.project.seed.presentation.view.fragment.LoginFragment;
import net.iquesoft.project.seed.utils.Constants;

public class UserLoginPresenter implements Presenter, OnCompleteListener {

    private final MyFirebaseAuth myFirebaseAuth;
    private final MainActivity mainActivity;
    private RegularExpressionValidation validation;
    private UserModel userModel;
    private LoginFragment fragmentView;
    //    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public UserLoginPresenter(MainActivity mainActivity,
                              MyFirebaseAuth myFirebaseAuth,
                              RegularExpressionValidation regularExpressionValidation,
                              UserModel userModel) {
        this.mainActivity = mainActivity;
        this.userModel = userModel;
        this.myFirebaseAuth = myFirebaseAuth;
        this.validation = regularExpressionValidation;
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
        this.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                userModel.setFirebaseUser(user);
                fragmentView.updateUI(user);
            }
        };
    }

    public FirebaseAuth.AuthStateListener getAuthListener() {
        return this.mAuthListener;

    }

    public void signInWithCredentials(AuthCredential credential) {
        this.myFirebaseAuth.signInWithCredentials(credential).addOnCompleteListener(this);
    }

    public CallbackManager getFacebookCallbackManager() {
        if (this.userModel.getFacebookCallback() == null)
            this.userModel.setFacebookCallback(this.myFirebaseAuth.getFacebookCallbackManager());
        return this.userModel.getFacebookCallback();
    }


    public void logInWithEmailAndPassword(String email, String password) {
        this.fragmentView.showLoading();
        this.fragmentView.clearTextField();
        try {
            this.validation.isEmailValid(email);
            this.userModel.setUserEmail(email);
        } catch (InvalidEmailException | EmptyFieldException e) {
            this.fragmentView.hideLoading();
            showEmailError(new DefaultErrorBundle(e));
            return;
        }
        try {
            this.validation.isPasswordValid(password);
            this.userModel.setUserPassword(password);
        } catch (EmptyFieldException | InvalidPasswordException e) {
            this.fragmentView.hideLoading();
            showPasswordError(new DefaultErrorBundle(e));
            return;
        }

        this.myFirebaseAuth.getEmailAndPasswordAuthentication(email, password).addOnCompleteListener(mainActivity, this);
    }

    public void logInWithGoogle(FragmentActivity activity) {
        this.fragmentView.showLoading();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(this.myFirebaseAuth.getGoogleApiClient());
        activity.startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    private void showPasswordError(DefaultErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(mainActivity,
                errorBundle.getException());
        this.fragmentView.showPasswordError(errorMessage);
    }

    private void showEmailError(DefaultErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(mainActivity,
                errorBundle.getException());
        this.fragmentView.showEmailError(errorMessage);
    }


    @Override
    public void onComplete(@NonNull Task task) {
        this.fragmentView.hideLoading();
        if (!task.isSuccessful()) {
            LoginManager.getInstance().logOut();
            this.fragmentView.updateUI(null);
            try {
                throw task.getException();
            } catch (Exception e) {
                this.fragmentView.showError(e.getMessage());
            }
        }
    }


    public FirebaseAuth getMyFirebaseAuth() {
        return myFirebaseAuth.getAuth();
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
            this.fragmentView.hideLoading();
            this.fragmentView.updateUI(null);
            this.fragmentView.showError(mainActivity.getString(R.string.error_message_authentication_error));
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

    public FirebaseUser getFirebaseUser() {
        return userModel.getFirebaseUser();
    }

    public void connectGoogleApiClient() {
        myFirebaseAuth.getGoogleApiClient().connect();
    }

    public void disconnectGoogleApiClient() {
        myFirebaseAuth.getGoogleApiClient().disconnect();
    }

    public GoogleApiClient getGoogleApiClient() {
        return myFirebaseAuth.getGoogleApiClient();
    }
}
