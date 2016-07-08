package net.iquesoft.project.seed.presentation.presenter;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

import net.iquesoft.project.seed.presentation.model.UserModel;

/**
 * Created by andre on 09-Jul-16.
 */
public class PrimaryActivityPresenter implements Presenter {

    private final Context context;
    private final UserModel userModel;

    public PrimaryActivityPresenter(Context context, UserModel userModel) {
        this.context = context;
        this.userModel = userModel;
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

    public FirebaseUser getFirebaseUser() {
        return userModel.getFirebaseUser();
    }
}
