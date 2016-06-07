package net.iquesoft.project.seed.presentation.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import net.iquesoft.project.seed.domain.RegularExpressionValidation;
import net.iquesoft.project.seed.utils.Constants;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;

public class UserLoginPresenter implements Presenter {

    private static UserLoginPresenter ourInstance;
    RegularExpressionValidation validation;
    private Context context;
    private String password;
    private String login;
    private int errorCode;
    private GoogleSignInAccount googleSignInAccount;

    private UserLoginPresenter(Context context) {
        this.context = context;
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


    public int logIn(String email, String password) throws InterruptedException {
        validation = new RegularExpressionValidation();
        int code = validation.isEmailValid(email);
        if (code != Constants.CODE_OK) {
            errorCode = code;
            throw new InterruptedException();
        }
        code = validation.isPasswordValid(password);
        if (code != Constants.CODE_OK) {
            errorCode = code;
            throw new InterruptedException();
        }

        /*
         * Here should be used HTTP Request
         */

        return Constants.CODE_OK;
    }

    public ProgressDialog getProgressDialog() {
        return ProgressDialog.show(context, "Logging in", "", true, true);
    }

    public Observable getObservable() {
        return observableLogging();
    }

    private Observable<Integer> observableLogging() {
        return Observable.defer(new Func0<Observable<Integer>>() {
            public int code;

            @Override
            public Observable<Integer> call() {
                try {
                     /*
                      *  HTTP Request imitation
                      */
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                    code = logIn(login, password);
                } catch (InterruptedException e) {
                    throw OnErrorThrowable.from(e);
                }
                return Observable.just(code);
            }
        });
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setGoogleSignInAccount(GoogleSignInAccount googleSignInAccount) {
        this.googleSignInAccount = googleSignInAccount;

    }
}
