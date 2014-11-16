package com.namso.pimark;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class PiMark extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment[] fragments;
    private boolean updatePi = false;
    private boolean updateCompare = false;
    int position;
    FragmentManager fragmentManager = getFragmentManager();

    //drawer title
    private  CharSequence mDrawerTitle;

    //app title
    private CharSequence mTitle;

    //slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private  NavDrawerListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pi_mark);

        fragments = new Fragment[3];
        fragments[0] = new BenchmarkFragment();
        fragments[1] = new MyPiFragment();
        fragments[2] = new CompareFragment();

        mTitle = mDrawerTitle = getTitle();

        // load menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        //add items to the drawer array
        //benchmark
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0,-1)));
        //my pi
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1,-1)));
        //compare
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2,-1)));
        navMenuIcons.recycle();

        //sets the list view adapter to the drawer adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // enables the action bar home button as a toggle
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();

            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            displayView(0);
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pi_mark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }
    }

    private void displayView(int position) {
        Fragment fragment = null;
        this.position = position;

        if (fragments[0] != null && fragments[1] != null && fragments[2] != null) {
            if(fragmentManager.findFragmentByTag("A") == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.frame_container, fragments[0], "A")
                        .add(R.id.frame_container, fragments[1], "B")
                        .add(R.id.frame_container, fragments[2], "C")
                        .hide(fragments[1])
                        .hide(fragments[2])
                        .commit();
            }

            switch (position) {
                case 0:
                    fragmentManager.beginTransaction()
                            .hide(fragments[2])
                            .hide(fragments[1])
                            .show(fragments[0])
                            .commit();
                    break;
                case 1:
                        if (updatePi) {
                            fragmentManager.beginTransaction()
                                    .remove(fragmentManager.findFragmentByTag("B"))
                                    .add(R.id.frame_container, fragments[1] = new MyPiFragment(), "B")
                                    .commit();
                            updatePi = false;
                        }
                            fragmentManager.beginTransaction()
                                    .hide(fragments[2])
                                    .hide(fragments[0])
                                    .show(fragments[1])
                                    .commit();
                            break;
                case 2:
                    if (updateCompare) {
                        fragmentManager.beginTransaction()
                                .remove(fragmentManager.findFragmentByTag("C"))
                                .add(R.id.frame_container, fragments[2] = new CompareFragment(), "C")
                                .commit();
                        updateCompare = false;
                    }
                    fragmentManager.beginTransaction()
                            .hide(fragments[1])
                            .hide(fragments[0])
                            .show(fragments[2])
                            .commit();
                    break;
                default:
                    break;
            }
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Log.e("MainActivity", "Error in selecting fragment");
        }
    }

    public void toggleUpdate(){
        updatePi = !updatePi;
        updateCompare = !updateCompare;
    }
}
