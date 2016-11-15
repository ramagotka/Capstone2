package example.com.fivetribesscoringsheet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private final String name = "class_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ((MyApplication) getApplication()).startTracking();

    }

    public void onClickBasic (View view){
        Intent intent = new Intent(this, AddPlayerActivity.class);
        intent.putExtra(name, getString(R.string.bas));
        startActivity(intent);
    }

    public void onClickAdvanced(View view){
        Intent intent = new Intent(this, AddPlayerActivity.class);
        intent.putExtra(name, getString(R.string.adv));
        startActivity(intent);
    }

    public void onClickStatistic(View view){
        startActivity(new Intent(this, StatisticActivity.class));
    }
}
