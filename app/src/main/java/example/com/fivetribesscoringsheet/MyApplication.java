package example.com.fivetribesscoringsheet;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by hania on 03.11.16.
 */

public class MyApplication extends Application {

    public Tracker mTracker;

    public void startTracking() {

        if (mTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
            mTracker = ga.newTracker(R.xml.track_info);

            ga.enableAutoActivityReports(this);

            ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
    }

    public Tracker getTracker() {
        startTracking();
        return mTracker;
    }
}
