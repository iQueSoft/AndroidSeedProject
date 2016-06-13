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

import rx.Observable;

public class UserSignUpPresenter implements Presenter {

    private static UserSignUpPresenter ourInstance;
    RegularExpressionValidation validation;
    private Context context;
    private String password;
    private String email;
    private int errorCode;
    private FirebaseAuth mAuth;

    private UserSignUpPresenter(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public static UserSignUpPresenter getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UserSignUpPresenter(context);
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


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean signUp(String email, String password) {

        // [START create_user_with_email]
        return mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LOG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("LOG", "signInWithCredential", task.getException());
                            errorCode = Constants.ERROR_AUTH_FAILED;
                        }
                    }
                }).isComplete();
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
        return ProgressDialog.show(context, "Signing up", "", true, true);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Observable getSignUpObservable() {
        return Observable.just(signUp(email, password));
    }
}
