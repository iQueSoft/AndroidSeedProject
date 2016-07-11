package net.iquesoft.project.seed.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.trncic.library.DottedProgressBar;

import net.iquesoft.project.seed.R;

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initiateAppLaunch();
    }

    @Override
    protected void setupActivityComponent() {

    }

    private void initiateAppLaunch() {
        DottedProgressBar progressBar = (DottedProgressBar) findViewById(R.id.progress_bar_splash);
        progressBar.startProgress();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Imitate some data loading or whatever you want else during 3000ms
                startActivity(new Intent(getBaseContext(), MainActivity.class));
//                progressBar.stopProgress();
            }
        }, 3000);
    }
}
