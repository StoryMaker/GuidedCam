package cc.storymaker.guidedcam;

import android.content.Context;
import android.view.View;

public abstract class NotificationPlugin {
	NotificationController mController;
	Context mContext;
	View mView;
	
	protected NotificationPlugin(Context context, NotificationController controller, View view) {
		mController = controller;
		mContext = context;
		mView = view;
	}
	
	public abstract void start(); 
//		 must override in plugin classes to start up their AsyncTask loop that decides their own status
	
	public abstract void stop();
}
