package com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.ecopet.will.ecopet.BoundaryClasses.AdaptersClasses.PageAdapterHome;
import com.ecopet.will.ecopet.ControlClasses.FeedControl;
import com.ecopet.will.ecopet.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // seta os tabs do layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.picture));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.medal));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //exibe a pagina conforme tab selecionado
        final ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        PageAdapterHome adapter = new PageAdapterHome(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
