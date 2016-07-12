package net.iquesoft.project.seed.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.di.HasComponent;
import net.iquesoft.project.seed.presentation.di.components.DaggerLoginComponent;
import net.iquesoft.project.seed.presentation.di.components.LoginComponent;
import net.iquesoft.project.seed.presentation.di.modules.LoginModule;
import net.iquesoft.project.seed.presentation.navigation.Navigator;
import net.iquesoft.project.seed.presentation.presenter.UserLoginPresenter;
import net.iquesoft.project.seed.presentation.view.fragment.LoginFragment;
import net.iquesoft.project.seed.utils.Constants;
import net.iquesoft.project.seed.utils.LogUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, HasComponent<LoginComponent> {

    @Inject
    UserLoginPresenter presenter;

    @Inject
    Navigator navigator;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private LoginComponent loginComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));

        initFirebaseListener();
        subscribeOnMessaging();

        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                LogUtil.makeLog("Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]
        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, new LoginFragment());
        }

        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        if (intent.getStringExtra("EXTRA") != null) {
            if (intent.getStringExtra("EXTRA").equals("SignOut")) {
                signOut();
            }
        }
    }

    @Override
    protected void setupActivityComponent() {
        this.loginComponent = DaggerLoginComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .loginModule(new LoginModule(this))
                .build();
        this.getComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getMyFirebaseAuth().addAuthStateListener(presenter.getAuthListener());
        presenter.connectGoogleApiClient();

    }

    public void showUserInfoInUI(@NonNull FirebaseUser firebaseUser) {
        View view = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();

        TextView tvNavHeaderName = (TextView) view.findViewById(R.id.tvNavHeaderName);
        TextView tvNavHeaderEmail = (TextView) view.findViewById(R.id.tvNavHeaderEmail);
        CircleImageView ivUserPhoto = (CircleImageView) view.findViewById(R.id.ivNavHeaderPhoto);
        MenuItem tvNavLogOut = menu.findItem(R.id.nav_logOut);

        tvNavHeaderName.setText(firebaseUser.getDisplayName());
        tvNavHeaderEmail.setText(firebaseUser.getEmail());

        if (firebaseUser.getPhotoUrl() != null) {
            imageLoader.displayImage(firebaseUser.getPhotoUrl().toString(), ivUserPhoto);
        }
        tvNavLogOut.setVisible(true);
        onLoggedIn();
    }

    public void showDefaultUserInUI() {
        View view = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        TextView tvNavHeaderName = (TextView) view.findViewById(R.id.tvNavHeaderName);
        TextView tvNavHeaderEmail = (TextView) view.findViewById(R.id.tvNavHeaderEmail);
        CircleImageView ivUserPhoto = (CircleImageView) view.findViewById(R.id.ivNavHeaderPhoto);
        MenuItem tvNavLogOut = menu.findItem(R.id.nav_logOut);
        tvNavHeaderName.setText(R.string.user_name);
        tvNavHeaderEmail.setText(R.string.user_email);
        ivUserPhoto.setImageResource(R.mipmap.ic_launcher);
        tvNavLogOut.setVisible(false);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            navigator.navigateToGalleryFragment(getSupportFragmentManager(), R.id.fragmentContainer);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logOut) {
            signOut();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter.getAuthListener() != null) {
            presenter.getMyFirebaseAuth().removeAuthStateListener(presenter.getAuthListener());
        }
        presenter.disconnectGoogleApiClient();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.getFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            presenter.handleGoogleSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
        }
    }


    private void initFirebaseListener() {
        presenter.initFirebaseListener();
    }

    private void subscribeOnMessaging() {
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_MESSAGE);
    }


    private void onLoggedIn() {
        navigator.navigateToPrimaryActivity(this);
    }

    private void signOut() {
        showDefaultUserInUI();
        presenter.getMyFirebaseAuth().signOut();
        LoginManager.getInstance().logOut();
        while (presenter.getGoogleApiClient().isConnected()) {
            Auth.GoogleSignInApi.signOut(presenter.getGoogleApiClient()).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            showDefaultUserInUI();
                        }
                    });
        }
        navigator.navigateToLogInFragment(getSupportFragmentManager());
    }

    @Override
    public LoginComponent getComponent() {
        return this.loginComponent;
    }
}
