package app.morningsignout.com.morningsignoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

// Category page activity
public class CategoryActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the ListView layout from fragmment_category_main.xml,
        setContentView(R.layout.activity_category);

        if (getIntent() != null) {
            String title = getIntent().getStringExtra(Intent.EXTRA_TITLE);
            setTitle(title.substring(0, 1).toUpperCase() + title.substring(1));

            CategoryFragment fragment = new CategoryFragment();
            Bundle args = new Bundle();
            args.putString(CategoryFragment.EXTRA_TITLE, title);
            fragment.setArguments(args);

            // Set fragment's listview
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_category, fragment)
                        .commit();
            }
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_category, new ErrorFragment())
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
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
}