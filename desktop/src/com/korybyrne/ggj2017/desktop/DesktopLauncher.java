package com.korybyrne.ggj2017.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.korybyrne.ggj2017.Drop;
import com.korybyrne.ggj2017.GGJ2017_Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drop";
        config.width = Drop.SCREEN_WIDTH;
        config.height = Drop.SCREEN_HEIGHT;
        config.foregroundFPS = 30;
		config.samples = 4;
		new LwjglApplication(new Drop(), config);
	}
}
