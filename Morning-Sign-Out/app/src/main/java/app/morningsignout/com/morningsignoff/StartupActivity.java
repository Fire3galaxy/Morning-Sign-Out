package app.morningsignout.com.morningsignoff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by liukwarm on 8/19/15.
 */
public class StartupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Immediately start new Recent Category Activity
        Intent categoryPageIntent = new Intent(this, CategoryActivity.class);
        categoryPageIntent.putExtra(Intent.EXTRA_TITLE, 0);
        startActivity(categoryPageIntent);
        setContentView(R.layout.activity_main);
    }
}
