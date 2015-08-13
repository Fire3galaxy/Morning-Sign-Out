package app.morningsignout.com.morningsignoff;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLToMobileArticle extends AsyncTask<String, Void, String> {
    WebView wb;
	String link;

    public URLToMobileArticle(WebView webview) {
        this.wb = webview;
    }

    @Override
    protected String doInBackground(String... params) {
        link = params[0];
		return getArticle(params[0]);
    }

    @Override
    protected void onPostExecute(final String html) {
//        wb.loadData(html, "text/html; charset=UTF-8", null);
        wb.loadDataWithBaseURL(link, html, "text/html; charset=UTF-8", null, link);
		Log.d("URLToMobileArticle", "Loaded webpage " + link);
    }

    // Sections of html that the content post will specifically be in
    // o = open tag, c = close tag
    static final String body_o = "<body>", body_c = "</body>";
    static final String divContainer_o = "<div class=\"container\">", divContainer_c = "</div>";
    static final String divContent_o = "<div class=\"content content--single\">", divContent_c = "</div>";
    static final String divPost_o = "<div class=\"content__post\">", divPost_c = "</div>";

    // Trimming these elements
    static final String header_o = "<header>", header_c = "</header>";
    static final String footer_o = "<footer>", footer_c = "</footer>";
    static final String divSSBA_o = "<div class=\"ssba ssba-wrap\">", divSSBA_c = "</div>";
    static final String divRelated_o = "<div class=\"content__related\">", divRelated_c = "</div>";
    static final String divDisqusThread_o = "<div id=\"disqus_thread\">", divDisqusThread_c = "</div>";
    static final String noComment_o = "<p class=\"nocomments\">", noComment_c = "</p>";
    static final String divPostNav_o = "<div class=\"post-nav\">", divPostNav_c = "</div>";

	public static String getArticle(String link) {
		URL url;
		String html_new;
		try {
			url = new URL(link);
			URLConnection c = url.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(c.getInputStream(), "utf-8"));
	        String input;
	        String html = "";
	        
	        while ((input = in.readLine()) != null) html += (input + "\n");

	        // Find body substring
	        int b_o = html.indexOf(body_o); 						// start of body
	        int b_c = html.lastIndexOf(body_c) + body_c.length(); 	// end of body
	        String sub_b = html.substring(b_o, b_c); 				// body of html
	        
	        // Find divContainer substring
	        int a_divC_o = sub_b.indexOf(divContainer_o); 			// start of container
	        int a_divC_c = sub_b.lastIndexOf(divContainer_c); 		// "Back to top" <div> </div>
	        int b_divC_c = sub_b.lastIndexOf(divContainer_c, a_divC_c - 1) + divContainer_c.length(); 	// end of container
	        String sub_divC = sub_b.substring(a_divC_o, b_divC_c);	// container html (in body)
	        
	        // Trim header
	        int h_o = sub_divC.indexOf(header_o);						// header open tag
	        int h_c = sub_divC.indexOf(header_c) + header_c.length(); 	// header close tag
	        String sub_divC_1 = deleteSub(sub_divC, h_o, h_c);
	        
	        // Trim footer
	        int f_o = sub_divC_1.indexOf(footer_o);
	        int f_c = sub_divC_1.indexOf(footer_c) + header_c.length();
	        String sub_divC_2 = deleteSub(sub_divC_1, f_o, f_c);
	        
	        // Find divContent substring
	        int a_divCt_o = sub_divC_2.indexOf(divContent_o);					// open div of divContent
	        int a_divCt_c = sub_divC_2.lastIndexOf(divContent_c);		 		// close div of divContent 
	        String sub_divCt = sub_divC_2.substring(a_divCt_o, a_divCt_c);
	        
	        // Trim divRelated
//	        int divR_o = sub_divCt.indexOf(divRelated_o);												// open div of content__related
//	        int a_divR_c = sub_divCt.lastIndexOf(divRelated_c);											// close div of divContent
//	        int b_divR_c = sub_divCt.lastIndexOf(divRelated_c, a_divR_c - 1) + divRelated_c.length();	// close div of divRelated
//	        String sub_divCt_1 = deleteSub(sub_divCt, divR_o, b_divR_c);

            // Trim divRelated temporarily
            int divR_o = sub_divCt.indexOf(divRelated_o);												// open div of content__related
            int a_divR_c = sub_divCt.lastIndexOf(divRelated_c);											// close div of divContent
            int b_divR_c = sub_divCt.lastIndexOf(divRelated_c, a_divR_c - 1) + divRelated_c.length();	// close div of divRelated
            String sub_divR = sub_divCt.substring(divR_o, b_divR_c);
            String sub_divCt_1 = deleteSub(sub_divCt, divR_o, b_divR_c);

            // Find divPost substring
            int a_divP_o = sub_divCt_1.indexOf(divPost_o);											// open div of content__post
            int a_divP_c = sub_divCt_1.lastIndexOf(divPost_c);										// close div of divContent
            int b_divP_c = sub_divCt_1.lastIndexOf(divPost_c, a_divP_c - 1) + divPost_c.length();		// close div of divPost
            String sub_divP = sub_divCt_1.substring(a_divP_o, b_divP_c);

            // Trim divDisqusThread
            String sub_divP_1;
            if (sub_divP.contains(divDisqusThread_o)) {
                int divDT_o = sub_divP.indexOf(divDisqusThread_o);														// open div of disqus_thread
                int a_divDT_c = sub_divP.lastIndexOf(divDisqusThread_c);												// close div of divPost
                int b_divDT_c = sub_divP.lastIndexOf(divDisqusThread_c, a_divDT_c - 1) + divDisqusThread_c.length();	// close div of divDT

//		        System.out.println(sub_divP.substring(divDT_o));

                sub_divP_1 = deleteSub(sub_divP, divDT_o, b_divDT_c);
            }
            // Trim no comment section
            else {
                int divNC_o = sub_divP.indexOf(noComment_o);
                int divNC_c = sub_divP.indexOf(noComment_c, divNC_o) + noComment_c.length();
                sub_divP_1 = deleteSub(sub_divP, divNC_o, divNC_c);
            }

	        // Trim divPostNav
	        int divPN_o = sub_divP_1.indexOf(divPostNav_o);													// open div of post-nav
//	        int a_divPN_c = sub_divP_1.lastIndexOf(divPostNav_c);											// close div of divPost
//	        int b_divPN_c = sub_divP_1.lastIndexOf(divPostNav_c, a_divPN_c - 1) + divPostNav_c.length();	// close div of divPN
//	        String sub_divP_2 = deleteSub(sub_divP_1, divPN_o, b_divPN_c);

            // Trim divSSBA2
            int divSSBA2_o = sub_divP_1.lastIndexOf(divSSBA_o);												// open div of ssba-wrap (2)
            int a_divSSBA2_c = sub_divP_1.lastIndexOf(divSSBA_c, divPN_o) + divSSBA_c.length();				// close div of ssba-wrap (2)
//	        int b_divSSBA2_c = sub_divP_2.lastIndexOf(divSSBA_c, a_divSSBA2_c) + divSSBA_c.length();
            String sub_divP_2 = deleteSub(sub_divP_1, divSSBA2_o, a_divSSBA2_c);

            // Trim divSSBA1
            int divSSBA1_o = sub_divP_2.indexOf(divSSBA_o);													// open div of ssba-wrap (1)
            int a_divSSBA1_c = sub_divP_2.lastIndexOf(divSSBA_c);											// close div of divPost
            int b_divSSBA1_c = sub_divP_2.lastIndexOf(divSSBA_c, a_divSSBA1_c - 1);							// close div of divPN
            int c_divSSBA1_c = sub_divP_2.lastIndexOf(divSSBA_c, b_divSSBA1_c - 1) + divSSBA_c.length();	// close div of ssba-wrap (1)
            String sub_divP_3 = deleteSub(sub_divP_2, divSSBA1_o, c_divSSBA1_c);

            // -------------------------Done Trimming and Subdividing-------------------------------

	        // Put divPost substring back into divContent substring
            String sub_divCt_new = replaceSub(sub_divCt, sub_divP_3, a_divP_o, b_divP_c);

//            // Put divRelated substring back into divContent (appending)
//            String sub_divCt_new_2 = sub_divCt_new + sub_divR;

            // Put divContent substring back into container substring
            String sub_divC_new = replaceSub(sub_divC_2, sub_divCt_new, a_divCt_o, a_divCt_c);
	        
	        // Put divContainer substring back into body substring
	        String sub_b_new = replaceSub(sub_b, sub_divC_new, a_divC_o, b_divC_c);
	        
	        // Put body substring into html
	        html_new = replaceSub(html, sub_b_new, b_o, b_c);
		} catch (MalformedURLException e) {
			Log.e(e.getLocalizedMessage(), e.getMessage());
			
			return null;
		} catch (IOException e) {
            Log.e(e.getLocalizedMessage(), e.getMessage());
			
			return null;
		}
		
		return html_new;
	}
	
	static String replaceSub(String s, String substring, int start, int end) {
		return s.substring(0, start) + substring + s.substring(end);
	}
	static String deleteSub(String s, int start, int end) {
		return s.substring(0, start) + s.substring(end);
	}
}
 