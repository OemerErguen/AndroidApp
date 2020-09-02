package de.uni_stuttgart.informatik.sopra.sopraapp.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import de.uni_stuttgart.informatik.sopra.sopraapp.BuildConfig;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * about helper fragment which wraps functionality in "about" section
 *
 */
public class AboutHelperFragment extends AppCompatActivity {

    public static final String TAG = AboutHelperFragment.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about_helper);

        WebView webView = this.findViewById(R.id.wv_aboutScreen);
        webView.getSettings().getJavaScriptEnabled();

        String myURL = getIntent().getStringExtra("File");
        String selectedItem = getIntent().getStringExtra("Message");

        if (selectedItem.equals("version")) {
            String html = htmlFileToString();
            if (html != null) {
                html = html.replace("&lt;VERSION&gt;", getVersionNumber());
                html = html.replace("&lt;BUILD&gt;", getBuildTimestamp());
                webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");

            }
            Log.d(TAG, html);
        } else {
            webView.loadUrl(myURL);
        }
    }

    private String htmlFileToString() {
        String result = null;
        try (Scanner s = new Scanner(new BufferedInputStream(getResources().openRawResource(R.raw.version))).useDelimiter("\\A")) {
            result = s.hasNext() ? s.next() : "";
        } catch (Exception e) {
            Log.w(TAG, "error fetching basic html: " + e.getMessage());
        }
        return result;
    }

    private String getVersionNumber() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "name not found exception: " + e.getMessage());
        }
        if (info == null) {
            // fallback
            return "0";
        }
        return info.versionName;
    }

    private String getBuildTimestamp() {
        return new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss").format(new Date(BuildConfig.BUILD_TIMESTAMP));
    }
}
