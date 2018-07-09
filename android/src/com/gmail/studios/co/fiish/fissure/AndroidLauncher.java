package com.gmail.studios.co.fiish.fissure;


import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;


public class AndroidLauncher extends AndroidApplication implements ActionResolver {
	private static final String AD_APP_ID = "APP_ID";
	private static final String AD_UNIT_ID_INTERSTITIAL = "AD_UNIT_ID";
	private InterstitialAd mInterstitialAd;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;

		initialize(new FissureGame(this), config);

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
}


