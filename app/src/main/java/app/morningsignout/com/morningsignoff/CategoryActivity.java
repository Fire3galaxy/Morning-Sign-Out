package app.morningsignout.com.morningsignoff;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// Category page activity
public class CategoryActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Title of activity when drawer swipe menu is visible
    private String mDrawerTitle;

    // Current Title
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the ListView layout from fragmment_category_main.xml,
        setContentView(R.layout.activity_category);

        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra(Intent.EXTRA_TITLE);
            mDrawerTitle = "Categories Menu";
            setTitle(mTitle.substring(0, 1).toUpperCase() + mTitle.substring(1));
        } else {
            mTitle = "";
            mDrawerTitle = mTitle;
            setTitle("");
        }

        // For DrawerLayout (no fragment)
        mDrawerList = (ListView) findViewById(R.id.listView_slide);
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                R.layout.list_items_slide,
                categories);
        mDrawerList.setAdapter(mAdapter);

        // Change up button of actionbar to 3 horizontal bars for slide
        setUpButtonToTripleBar();
        // Set up button to open/close drawer and change title of current activity
        setDrawerListenerToActionBarToggle();

        // For CategoryFragment
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(CategoryFragment.EXTRA_TITLE, mTitle);
        fragment.setArguments(args);

        // Set fragment's listview
        if (getIntent() != null && savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_category, fragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_category, new ErrorFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if drawer is open, hide action items (if using a menu xml)
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle action bar item clicks here. The action bar will
           automatically handle clicks on the Home/Up button, so long
           as you specify a parent activity in AndroidManifest.xml. */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title.toString();
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState(); // sync toggle state after onRestoreInstanceState
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig); // pass it on to toggle
    }

    void initializeTitles() {

    }

    void setUpButtonToTripleBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setLogo(R.drawable.slide_bars);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    void setDrawerListenerToActionBarToggle() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.title_slide_menu,
                R.string.title_activity_category) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // Show actionbar icons
            }

            public void onDrawerOpened(View view) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // Hide actionbar icons
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
}