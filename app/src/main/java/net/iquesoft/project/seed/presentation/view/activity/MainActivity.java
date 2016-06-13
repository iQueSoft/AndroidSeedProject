package net.iquesoft.project.seed.presentation.view.activity;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.model.UserModel;
import net.iquesoft.project.seed.presentation.navigation.Navigator;
import net.iquesoft.project.seed.presentation.presenter.UserLoginPresenter;
import net.iquesoft.project.seed.presentation.view.fragment.LoginFragment;
import net.iquesoft.project.seed.utils.LogUtil;

import butterknife.ButterKnife;

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
    private GoogleSignInOptions gso;

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

    private void configureGoogleSignIn() {
        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void initFirebaseListener() {

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    LogUtil.makeLog("1 onAuthStateChanged:signed_in:" + user.getUid());
                    userModel.setUserEmail(user.getEmail());
                    userModel.setUserId(user.getUid());
                    userModel.setUserName(user.getDisplayName());
                    userModel.setUserPhotoUrl(user.getPhotoUrl());
                    checkProfileForUpdates(user);
                    updateUI(true);
                } else {
                    // User is signed out
                    updateUI(false);
                    LogUtil.makeLog("LOG", "2 onAuthStateChanged:signed_out");
                }
            }
        };
        // [END auth_state_listener]

    }

    private void checkProfileForUpdates(FirebaseUser user) {
        GoogleSignInAccount gAccount = userModel.getGoogleAccount();
        if (gAccount != null) {
            if (user.getDisplayName() == null && gAccount.getDisplayName() != null) {
                new UserProfileChangeRequest.Builder().setDisplayName(gAccount.getDisplayName()).build();
            }
            if (user.getPhotoUrl() == null && gAccount.getPhotoUrl() != null) {
                new UserProfileChangeRequest.Builder().setDisplayName(gAccount.getPhotoUrl().toString()).build();
            }
        }
    }


    public void showLoading() {
        progressDialog = presenter.getProgressDialog();
    }

    public void hideLoading() {
        LogUtil.makeLog("Hiding loading UI");
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    public void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                userModel.setGoogleAccount(acct);
                userModel.setUserName(acct.getDisplayName());
                userModel.setUserEmail(acct.getEmail());
                userModel.setUserPhotoUrl(acct.getPhotoUrl());
                userModel.setUserId(acct.getId());
                firebaseAuthWithGoogle(acct);
            }
            updateUI(true);

        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("LOG", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showLoading();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LOG", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("LOG", "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        hideLoading();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    private void signOut() {

        mAuth.signOut();
        updateUI(false);
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
        navigator.navigateToLogInFragment(getSupportFragmentManager());
    }

    public void updateUI(boolean signedIn) {
        hideLoading();
        LogUtil.makeLog("Updating UI");
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
            ivUserPhoto = (ImageView) view.findViewById(R.id.ivNavHeaderPhoto);
        }
        MenuItem tvNavLogOut = null;
        if (menu != null) {
            tvNavLogOut = menu.findItem(R.id.nav_logOut);
        }

        if (signedIn) {
            if (tvNavHeaderName != null) {
                tvNavHeaderName.setText(userModel.getUserName());
            }
            if (tvNavHeaderEmail != null) {
                tvNavHeaderEmail.setText(userModel.getUserEmail());
            }

            if (userModel.getUserPhotoUrl() != null) {
                LogUtil.makeLog("userModel.getUserPhotoUrl() " + userModel.getUserPhotoUrl());
                if (ivUserPhoto != null) {
                    imageLoader.displayImage(userModel.getUserPhotoUrl().toString(), ivUserPhoto);
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
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
