package com.gmail.studios.co.fiish.fissure.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gmail.studios.co.fiish.fissure.FissureGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Fissure";
		config.useGL30 = false;
		new LwjglApplication(new FissureGame(new ActionResolverDesktop()), config);
	}
}
