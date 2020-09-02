package de.uni_stuttgart.informatik.sopra.sopraapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitMainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.CockpitDbHelper;

/**
 * splash screen activity of this app. this is the de-facto launcher activity
 */
public class SplashScreen extends Activity {

    public static final String TAG = SplashScreen.class.getName();

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    /**
     * Called when the activity is first created.
     */
    Thread splashTread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Log.d(TAG, "db init started for 3 tables");
        SQLiteDatabase readableDatabase = new CockpitDbHelper(SplashScreen.this).getReadableDatabase();
        readableDatabase.close();
        Log.d(TAG, "db init finished for 3 tables");

        startAnimations();
    }

    /**
     * Starting the animation
     */
    private void startAnimations() {
        //Setting the background of the splash screen
        Animation splashAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        splashAnimation.reset();
        LinearLayout splashLayout = (LinearLayout) findViewById(R.id.splash_layout);
        splashLayout.clearAnimation();
        splashLayout.startAnimation(splashAnimation);

        //Setting the splash picture and animation
        splashAnimation = AnimationUtils.loadAnimation(this, R.anim.translate);
        splashAnimation.reset();
        ImageView splashImage = (ImageView) findViewById(R.id.splash_image);
        splashImage.clearAnimation();
        splashImage.startAnimation(splashAnimation);

        //splashTread which sets the timer of the splash screen.
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashScreen.this,
                            CockpitMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashScreen.this.finish();
                } catch (InterruptedException e) {
                    // interrupt
                    interrupt();
                } finally {
                    SplashScreen.this.finish();
                }
            }
        };
        splashTread.start();

    }

}