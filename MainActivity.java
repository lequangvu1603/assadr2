package fpt.vulq.ass2adr2;

import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        userId = getIntent().getIntExtra("UserID", -1);

        navigationView.setNavigationItemSelectedListener(item -> {
            displaySelectedScreen(item.getItemId());
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        if (savedInstanceState == null) {
            displaySelectedScreen(R.id.nav_gratitude);
        }
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;

        if (itemId == R.id.nav_gratitude) {
            fragment = new GratitudeFragment();
        } else if (itemId == R.id.nav_personal_info) {
            fragment = new fragments.PersonalInfoFragment();
        } else if (itemId == R.id.nav_step_count) {
            fragment = new StepCountFragment();
        } else {
            throw new IllegalArgumentException("Unknown item ID: " + itemId);
        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("UserID", userId);
            fragment.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}