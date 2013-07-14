package orz.kassy.mapintenttest;

import orz.kassy.mapintenttest.MainActivity.KeyEventSender;
import android.app.Instrumentation;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;

public class MainService extends Service{
	
	private static final String TAG="MainService";
	private Handler handler;
	private NotificationManager mNM;

	
	@Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");
        handler = new Handler();
    }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.i(TAG,"onStart");
    KeyEventSender sender = new KeyEventSender();
    sender.execute("");
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG,"onBind");
		return ImainServiceBinder;
	}
	
	private final ImainService.Stub ImainServiceBinder = new ImainService.Stub(){
		public void callFunction(){
			Log.i(TAG,"called callFunction");
		}
	};
	
  public class KeyEventSender extends AsyncTask<String, Integer, Integer> {
    @Override
    protected Integer doInBackground(String... params) {
      while(true) {
        try {
          Thread.sleep(4000);
          Log.i(TAG,"keysync");
          Instrumentation ist = new Instrumentation();
          ist.sendKeyDownUpSync(KeyEvent.KEYCODE_K);
          return null;
        } catch (InterruptedException e) {
          Log.i(TAG,"error");

          e.printStackTrace();
        }
      }
    }
  }


}