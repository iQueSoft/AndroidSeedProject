package net.iquesoft.project.seed.presentation.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.model.UserModel;
import net.iquesoft.project.seed.presentation.navigation.Navigator;
import net.iquesoft.project.seed.presentation.presenter.UserLoginPresenter;
import net.iquesoft.project.seed.presentation.view.fragment.LoginFragment;
import net.iquesoft.project.seed.utils.Constants;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 9001;
    private Navigator navigator;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private UserLoginPresenter presenter;
    private UserModel userModel;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        navigator = Navigator.getInstance();
        presenter = UserLoginPresenter.getInstance(this);
        userModel = UserModel.getInstance();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
        ButterKnife.bind(this);

        initFirebaseListener();
        configureGoogleSignIn();
        subscribeOnMessaging();

        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, new LoginFragment());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void subscribeOnMessaging() {
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_MESSAGE);
    }

    private void configureGoogleSignIn() {
        mGoogleApiClient = presenter.getGoogleApiClient(this);
    }

    private void initFirebaseListener() {
        presenter.initFirebaseListener();
    }




    @Override
    public void onStart() {
        super.onStart();
        presenter.getFirebaseAuth().addAuthStateListener(presenter.getAuthListener());
    }

    private void signOut() {
        updateUI(null);
        presenter.getFirebaseAuth().signOut();
        LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(null);
                    }
                });
        navigator.navigateToLogInFragment(getSupportFragmentManager());
    }

    public void updateUI(FirebaseUser firebaseUser) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = null;
        Menu menu = null;
        if (navigationView != null) {
            view = navigationView.getHeaderView(0);
            menu = navigationView.getMenu();
        }
        TextView tvNavHeaderName = null;
        TextView tvNavHeaderEmail = null;
        ImageView ivUserPhoto = null;

        if (view != null) {
            tvNavHeaderName = (TextView) view.findViewById(R.id.tvNavHeaderName);
            tvNavHeaderEmail = (TextView) view.findViewById(R.id.tvNavHeaderEmail);
            ivUserPhoto = (CircleImageView) view.findViewById(R.id.ivNavHeaderPhoto);
        }
        MenuItem tvNavLogOut = null;
        if (menu != null) {
            tvNavLogOut = menu.findItem(R.id.nav_logOut);
        }

        if (firebaseUser != null) {
            if (tvNavHeaderName != null) {
                tvNavHeaderName.setText(firebaseUser.getDisplayName());
            }
            if (tvNavHeaderEmail != null) {
                tvNavHeaderEmail.setText(firebaseUser.getEmail());
            }

            if (firebaseUser.getPhotoUrl() != null) {
                if (ivUserPhoto != null) {
                    imageLoader.displayImage(firebaseUser.getPhotoUrl().toString(), ivUserPhoto);
                }
            }
            if (tvNavLogOut != null) {
                tvNavLogOut.setVisible(true);
            }
            onLoggedIn();
        } else {
            if (tvNavHeaderName != null) {
                tvNavHeaderName.setText(R.string.user_name);
            }
            if (tvNavHeaderEmail != null) {
                tvNavHeaderEmail.setText(R.string.user_email);
            }
            if (ivUserPhoto != null) {
                ivUserPhoto.setImageResource(R.mipmap.ic_launcher);
            }
            if (tvNavLogOut != null) {
                tvNavLogOut.setVisible(false);
            }
        }
    }

    private void onLoggedIn() {
        navigator.navigateToMainFragment(getSupportFragmentManager());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
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
            navigator.navigateToGalleryFragment(getSupportFragmentManager());

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logOut) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter.getAuthListener() != null) {
            presenter.getFirebaseAuth().removeAuthStateListener(presenter.getAuthListener());
        }
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
}
