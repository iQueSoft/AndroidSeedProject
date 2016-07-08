package net.iquesoft.project.seed.presentation.view.activity;

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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.presenter.UserLoginPresenter;
import net.iquesoft.project.seed.presentation.view.fragment.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PrimaryActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar_primary)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout_primary)
    DrawerLayout drawer;
    @BindView(R.id.nav_view_primary)
    NavigationView navigationView;

    private UserLoginPresenter loginPresenter;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer_primary, new MainFragment());
        }
        ButterKnife.bind(this);

        loginPresenter = UserLoginPresenter.getInstance(this);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));

        showUserInfoInUI();


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
    }

    private void showUserInfoInUI() {

        FirebaseUser firebaseUser = loginPresenter.getFirebaseUser();
        View view = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();

        TextView tvNavHeaderName = (TextView) view.findViewById(R.id.tvNavHeaderName_primary);
        TextView tvNavHeaderEmail = (TextView) view.findViewById(R.id.tvNavHeaderEmail_primary);
        CircleImageView ivUserPhoto = (CircleImageView) view.findViewById(R.id.ivNavHeaderPhoto_primary);
        MenuItem tvNavLogOut = menu.findItem(R.id.nav_logOut);

        tvNavHeaderName.setText(firebaseUser.getDisplayName());
        tvNavHeaderEmail.setText(firebaseUser.getEmail());

        if (firebaseUser.getPhotoUrl() != null) {
            imageLoader.displayImage(firebaseUser.getPhotoUrl().toString(), ivUserPhoto);
        }
        tvNavLogOut.setVisible(true);
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
        getMenuInflater().inflate(R.menu.primary, menu);
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
            navigator.navigateToGalleryFragment(getSupportFragmentManager(), R.id.fragmentContainer_primary);

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

    private void signOut() {
        navigator.navigateToMainActivity(this, "SignOut");
    }
}
