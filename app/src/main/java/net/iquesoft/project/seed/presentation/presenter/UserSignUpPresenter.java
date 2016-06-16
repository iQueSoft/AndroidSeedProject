package net.iquesoft.project.seed.presentation.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import net.iquesoft.project.seed.domain.RegularExpressionValidation;
import net.iquesoft.project.seed.domain.firebase.MyFirebaseAuth;
import net.iquesoft.project.seed.utils.Constants;

import rx.Observable;

public class UserSignUpPresenter implements Presenter {

    private static UserSignUpPresenter ourInstance;
    private final MyFirebaseAuth firebaseAuth;
    RegularExpressionValidation validation;
    private Context context;
    private String password;
    private String email;
    private int errorCode;
    private FirebaseAuth mAuth;

    private UserSignUpPresenter(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = MyFirebaseAuth.getInstance();
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
        return firebaseAuth.createUserWithEmailAndPassword(context, email, password);
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
