package net.iquesoft.project.seed.domain.interactor;

import com.google.firebase.auth.AuthResult;

import net.iquesoft.project.seed.domain.firebase.MyFirebaseAuth;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FirebaseAuthUseCase extends UseCase {


    private final MyFirebaseAuth firebaseAuth;
    private String password;
    private String email;

    public FirebaseAuthUseCase() {
        super(Schedulers.newThread(), AndroidSchedulers.mainThread());
        firebaseAuth = MyFirebaseAuth.getInstance();

    }

    @Override
    protected Observable<com.google.android.gms.tasks.Task<AuthResult>> buildUseCaseObservable() {
        return Observable.just(firebaseAuth.getEmailAndPasswordAuthentication(email, password));
    }

    public void setEmailAndPassword(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

//throw new InterruptedException();