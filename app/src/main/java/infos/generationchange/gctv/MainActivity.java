package infos.generationchange.gctv;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ViewPager viewPager ;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private ImageView search;

    //720 / 576

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.tv);
        tabLayout.getTabAt(1).setText(R.string.alaune);
        tabLayout.getTabAt(2).setText(R.string.emissions);

        search = findViewById(R.id.search);


        search.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this , SearchActivity.class));
        });

        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_black, getTheme());
                toggle.setHomeAsUpIndicator(drawable);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.back1, getTheme());
                toggle.setHomeAsUpIndicator(drawable);
            }
        };
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_black, getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_black, getTheme());
                    toggle.setHomeAsUpIndicator(drawable);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.back1, getTheme());
                    toggle.setHomeAsUpIndicator(drawable);
                }
            }
        });
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_camera,0);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        FragmentManager fm  = getSupportFragmentManager();
        Drawable drawable;
        switch(item.getItemId()) {
            case R.id.nav_camera:
                viewPager.setCurrentItem(0);
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_black, getTheme());
                toggle.setHomeAsUpIndicator(drawable);
                break;
            case R.id.nav_gallery:
                viewPager.setCurrentItem(1);
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_black, getTheme());
                toggle.setHomeAsUpIndicator(drawable);
                break;
            case R.id.nav_slideshow:
                viewPager.setCurrentItem(2);
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_black, getTheme());
                toggle.setHomeAsUpIndicator(drawable);
                break;
            case R.id.nav_send:
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_black, getTheme());
                toggle.setHomeAsUpIndicator(drawable);
                System.exit(0);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        item.setChecked(true);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}
