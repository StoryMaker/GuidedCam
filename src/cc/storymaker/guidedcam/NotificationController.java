package cc.storymaker.guidedcam;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.webkit.WebView.FindListener;

import com.kviation.android.sample.orientation.AttitudeIndicator;

public class NotificationController {
	private ArrayList<NotificationPlugin> mPlugins;
	
	public static final int STATE_OK 		= 0;
	public static final int STATE_MINOR 	= 1;
	public static final int STATE_MAJOR 	= 2;
	public static final int STATE_CRITICAL 	= 3;

	private View mTray;
	private View mTopCenter;
	private View mCenter;
	
	public void updateState(NotificationPlugin plugin, int state) {
		
	}
	
	public void addPlugin(NotificationPlugin plugin) {
		mPlugins.add(plugin);
	}
	
	NotificationController(View tray, View topCenter, View center) {
		mTray = tray;
		mTopCenter = topCenter;
		mCenter = center;
		mPlugins = new ArrayList<NotificationPlugin>(); 
	}
	
	public void start() {
		for (NotificationPlugin i: mPlugins) {
			i.start();
		}
	}
	
	public void stop() {
		for (NotificationPlugin i: mPlugins) {
			i.stop();
		}
	}
	
	public ArrayList<NotificationPlugin> getPlugins() {
		return mPlugins;
	}
}
