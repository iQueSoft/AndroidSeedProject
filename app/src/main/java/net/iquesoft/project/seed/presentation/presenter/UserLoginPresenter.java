package net.iquesoft.project.seed.presentation.presenter;

import android.content.Context;

import com.facebook.CallbackManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;

import net.iquesoft.project.seed.domain.RegularExpressionValidation;
import net.iquesoft.project.seed.domain.firebase.MyFirebaseAuth;
import net.iquesoft.project.seed.presentation.model.UserModel;
import net.iquesoft.project.seed.utils.Constants;

import rx.Observable;

public class UserLoginPresenter implements Presenter {

    private static UserLoginPresenter ourInstance;
    private final MyFirebaseAuth firebaseAuth;
    private RegularExpressionValidation validation;
    private Context context;
    private String password;
    private String email;
    private UserModel userModel;


    private UserLoginPresenter(Context context) {
        this.context = context;
        userModel = UserModel.getInstance();
        firebaseAuth = MyFirebaseAuth.getInstance();
    }

    public static UserLoginPresenter getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UserLoginPresenter(context);
        }
        return ourInstance;
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

    private boolean signIn(String email, String password) {
        return firebaseAuth.getEmailAndPasswordAuthentication(context, email, password);
    }

    public boolean isCredentialsValid() {
        validation = new RegularExpressionValidation();
        int code = validation.isEmailValid(email);
        if (code != Constants.CODE_OK) {
            userModel.setErrorCode(code);
            return false;
        }
        code = validation.isPasswordValid(password);
        if (code != Constants.CODE_OK) {
            userModel.setErrorCode(code);
            return false;
        }
        return true;
    }


    public Observable<Boolean> getLogInObservable() {
        return Observable.just(signIn(email, password));
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getErrorCode() {
        return userModel.getErrorCode();
    }


    public FirebaseAuth.AuthStateListener getAuthListener(final Observable observable) {
        return firebaseAuth.getAuthListener(observable);

    }

    public GoogleApiClient getGoogleApiClient(Context context) {
        if (userModel.getGoogleApiClient() == null) {
            userModel.setGoogleApiClient(firebaseAuth.getGoogleApiClient(context));
        }
        return userModel.getGoogleApiClient();
    }

    public void signInWithCredentials(Context context, AuthCredential credential) {
        firebaseAuth.signInWithCredentials(context, credential);
    }

    public CallbackManager getFacebookCallbackManager() {
        if (userModel.getFacebookCallback() == null)
            userModel.setFacebookCallback(firebaseAuth.getFacebookCallbackManager());
        return userModel.getFacebookCallback();
    }

}
