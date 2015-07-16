package app.morningsignout.com.morningsignoff;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

// Activity class created in FetchListArticleTask when user clicks on an article from the ListView
public class ArticleActivity extends ActionBarActivity {
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        super.getSupportActionBar().setDisplayHomeAsUpEnabled(true); //made back arrow in top left corner

        // Set the title for this activity to the article title
        Intent intent = getIntent();
        if (intent != null) {
            // Setting variable category and title of activity
            category = getIntent().getStringExtra(Intent.EXTRA_TITLE);
            setTitle(getIntent().getStringExtra(Intent.EXTRA_SHORTCUT_NAME));

            // Disabling title text of actionbar
            this.getSupportActionBar().setDisplayShowCustomEnabled(true);
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);

            // Setting title of actionbar to article's name FIXME: No title vs ellipsized title vs something else?
            View v = getLayoutInflater().inflate(R.layout.title, null);

            ((TextView) v.findViewById(R.id.title_actionBar)).setText(getTitle());
            v.setSelected(true);

            this.getSupportActionBar().setCustomView(v);

            // Getting article from URL and stripping away extra parts of website for better reading
            WebView article = (WebView) findViewById(R.id.webView_article);
            article.setWebViewClient(new ArticleWebViewClient());
            // article.loadUrl(getIntent().getStringExtra(Intent.EXTRA_HTML_TEXT));
            new URLToMobileArticle(article).execute(getIntent().getStringExtra(Intent.EXTRA_HTML_TEXT));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Log.e(category, "yyyyyyyyyyyyy");
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(Intent.EXTRA_TITLE, category);
                NavUtils.navigateUpTo(this, intent);
        }
        return super.onOptionsItemSelected(item);
    }
}


// Create a customized webview client to disable website navigation bar
class ArticleWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(Uri.parse(url).getHost().endsWith("morningsignout.com")) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }
}
