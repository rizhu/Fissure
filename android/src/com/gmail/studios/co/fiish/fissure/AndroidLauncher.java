package com.gmail.studios.co.fiish.fissure;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


public class AndroidLauncher extends AndroidApplication implements ActionResolver {
	private static final String AD_APP_ID = "AD_APP_ID";
	private static final String AD_UNIT_ID_BANNER_TOP = "AD_UNIT_ID_BANNER_TOP";   //Admob ID:
																									//Test ID: ca-app-pub-3940256099942544/6300978111
	private static final String AD_UNIT_ID_INTERSTITIAL = "AD_UNIT_ID_INTERSTITIAL"; // Admob ID:
																									// Test ID: ca-app-pub-3940256099942544/1033173712
	private InterstitialAd mInterstitialAd;

	private AdView mAdViewTopBanner;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;

		//replaces initialize()
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		mAdViewTopBanner = createAdView(AD_UNIT_ID_BANNER_TOP, 56789, RelativeLayout.ALIGN_PARENT_TOP);
		View gameView = createGameView(config);
		layout.addView(gameView);
		layout.addView(mAdViewTopBanner);

		setContentView(layout);

		MobileAds.initialize(this, AD_APP_ID);
		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
		mInterstitialAd.loadAd(new AdRequest.Builder().build());
		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				mInterstitialAd.loadAd(new AdRequest.Builder().build());
			}
		});

		mAdViewTopBanner.setVisibility(View.GONE);
		mAdViewTopBanner.loadAd(new AdRequest.Builder().build());
	}

	@Override
	public void onResume() {
		mAdViewTopBanner.resume();
		super.onResume();
	}

	@Override
	public void onPause() {
		mAdViewTopBanner.pause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		mAdViewTopBanner.destroy();
		super.onDestroy();
	}

	@Override
	public void showInterstitial() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					mInterstitialAd.show();
				}
			});
		} catch (Exception e) {
		}
	}

	@Override
	public void showBanner(boolean show) {
	    if (show) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdViewTopBanner.setVisibility(View.VISIBLE);
                }
            });
        } else {
	        runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdViewTopBanner.setVisibility(View.GONE);
                    mAdViewTopBanner.loadAd(new AdRequest.Builder().build());
                }
            });
        }
	}

	private AdView createAdView(String adUnitID, int viewID, int alignment) {
		AdView adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(adUnitID);
		adView.setId(viewID); // this is an arbitrary id, allows for relative positioning in createGameView()
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(alignment, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		adView.setLayoutParams(params);
		adView.setBackgroundColor(Color.BLACK);
		return adView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg) {
		View gameView = initializeForView(new FissureGame(this), cfg);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		gameView.setLayoutParams(params);
		return gameView;
	}

}


