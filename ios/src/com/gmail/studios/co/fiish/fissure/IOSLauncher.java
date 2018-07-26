package com.gmail.studios.co.fiish.fissure;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
/*
import org.robovm.apple.uikit.UIViewController;
import org.robovm.pods.google.mobileads.GADBannerView;
import org.robovm.pods.google.mobileads.GADInterstitial;
import org.robovm.pods.google.mobileads.GADInterstitialDelegateAdapter;
import org.robovm.pods.google.mobileads.GADMobileAds;
import org.robovm.pods.google.mobileads.GADRequest;
*/

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate /* implements ActionResolver */ {
    /*
    private static final String AD_UNIT_ID_BANNER_TOP = "ca-app-pub-3940256099942544/6300978111";   //Test ID: ca-app-pub-3940256099942544/6300978111
    private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"; // Test ID: ca-app-pub-3940256099942544/1033173712

    private UIViewController mViewController;
    private GADBannerView mBannerView;

    private GADInterstitial mInterstitial;
    */

    @Override
    protected IOSApplication createApplication() {
        //GADMobileAds.disableSDKCrashReporting();

        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationLandscape = true;

        /*
        mViewController = UIApplication.getSharedApplication().getKeyWindow().getRootViewController();

        mInterstitial = new GADInterstitial(AD_UNIT_ID_INTERSTITIAL);
        mInterstitial.loadRequest(new GADRequest());
        mInterstitial.setDelegate(new GADInterstitialDelegateAdapter() {
            @Override
            public void didDismissScreen(GADInterstitial ad) {
                mInterstitial.loadRequest(new GADRequest());
            }
        });

        mBannerView = new GADBannerView();
        mBannerView.setAdUnitID(AD_UNIT_ID_BANNER_TOP);
        mBannerView.loadRequest(new GADRequest());
        */

        return new IOSApplication(new FissureGame(), config);
    }

    /*
    @Override
    public void showInterstitial() {
        mInterstitial.present(mViewController);
    }

    @Override
    public void showBanner(boolean show) {
        if (show) {
            mViewController.getView().addSubview(mBannerView);
        } else {
            mViewController.getView().sendSubviewToBack(mBannerView);
            mBannerView.loadRequest(new GADRequest());
        }
    }
    */

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}