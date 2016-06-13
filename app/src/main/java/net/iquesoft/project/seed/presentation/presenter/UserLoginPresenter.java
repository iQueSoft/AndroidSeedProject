package net.iquesoft.project.seed.presentation.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import net.iquesoft.project.seed.domain.RegularExpressionValidation;
import net.iquesoft.project.seed.utils.Constants;
import net.iquesoft.project.seed.utils.LogUtil;

import rx.Observable;

public class UserLoginPresenter implements Presenter {

    private static UserLoginPresenter ourInstance;
    private RegularExpressionValidation validation;
    private Context context;
    private String password;
    private String email;
    private int errorCode;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    private UserLoginPresenter(Context context) {
        this.context = context;
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

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
        // [START sign_in_with_email]
        return mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        LogUtil.makeLog("signInWithEmailAndPassword " + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("LOG", "signInWithEmailAndPassword", task.getException());
                            errorCode = Constants.ERROR_AUTH_FAILED;
                        }
                    }
                }).isComplete();
        // [END sign_in_with_email]
    }

    public boolean isCredentialsValid() {
        validation = new RegularExpressionValidation();
        int code = validation.isEmailValid(email);
        if (code != Constants.CODE_OK) {
            errorCode = code;
            return false;
        }
        code = validation.isPasswordValid(password);
        if (code != Constants.CODE_OK) {
            errorCode = code;
            return false;
        }
        return true;
    }

    public ProgressDialog getProgressDialog() {
        return ProgressDialog.show(context, "Logging in", "", true, true);
    }

    public Observable getLogInObservable() {
        return Observable.just(signIn(email, password));
    }

    public String getPassword() {
        return password;
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
        return errorCode;
    }


}
