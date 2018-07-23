package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import org.moe.natj.general.Pointer;

import apple.uikit.c.UIKit;

public class IOSMoeLauncher extends IOSApplication.Delegate implements ActionResolver {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        return new IOSApplication(new FissureGame(this), config);
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
    }

    @Override
    public void showInterstitial() {

    }

    @Override
    public void showBanner(boolean show) {

    }
}
