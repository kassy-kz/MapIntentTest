
package orz.kassy.mapintenttest;

import java.lang.reflect.Method;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

  private static final String TAG = null;
  private int mKeyCode;
  private ImainService mBinder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
  
    Button btn1 = (Button) findViewById(R.id.test1);
    btn1.setOnClickListener(this);
    Button btn2 = (Button) findViewById(R.id.test2);
    btn2.setOnClickListener(this);
    Button btn3 = (Button) findViewById(R.id.test3);
    btn3.setOnClickListener(this);
    Button btn4 = (Button) findViewById(R.id.test4);
    btn4.setOnClickListener(this);
    
    Button btn5 = (Button) findViewById(R.id.test5);
    btn5.setOnClickListener(this);
    Button btn6 = (Button) findViewById(R.id.test6);
    btn6.setOnClickListener(this);
    Button btn7 = (Button) findViewById(R.id.test7);
    btn7.setOnClickListener(this);
    Button btn8 = (Button) findViewById(R.id.test8);
    btn8.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch(v.getId()) {
      case R.id.test1:
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +35.41+","+139.42));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        break;
      case R.id.test2:
        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q='東京駅'"));
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent2);
        break;
      case R.id.test3:
        Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q='東京駅'&mode=w"));
        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent3);
        break;
      case R.id.test4:
        mKeyCode = KeyEvent.KEYCODE_0;
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        try {
          Class serviceManagerClazz = loader.loadClass("android.os.ServiceManager");
          Method getServiceMethod = serviceManagerClazz.getMethod("getService", String.class);
          Object windowObj = getServiceMethod.invoke(null, "window");
          Class windowManagerStubClazz = loader.loadClass("android.view.IWindowManager$Stub");
          Method asInterfaceMethod = windowManagerStubClazz.getMethod("asInterface", IBinder.class);
          Object iwindowObj = asInterfaceMethod.invoke(null, windowObj);
          Class windowManagerClazz = loader.loadClass("android.view.IWindowManager");

          Method method = windowManagerClazz.getMethod("injectKeyEvent", KeyEvent.class, boolean.class);
          method.invoke(iwindowObj, new KeyEvent( KeyEvent.ACTION_DOWN, mKeyCode), true);
          method.invoke(iwindowObj, new KeyEvent( KeyEvent.ACTION_UP, mKeyCode), true);
        } catch (Exception e) {
          e.printStackTrace();
          Log.e(TAG,"error");
        }     
        break;
        
      case R.id.test5:
        KeyEventSender sender = new KeyEventSender();
        sender.execute("");
        break;
      case R.id.test6:
        Log.i(TAG,"btnStart pressed");
        Intent intents = new Intent(this, MainService.class);
        startService(intents);

        // Attach Service
        bindService(intents, connection, BIND_AUTO_CREATE);

        break;
      case R.id.test7:
        Log.i(TAG,"btnCallFunc pressed");
        try{
            if(mBinder != null){
                mBinder.callFunction();
            }
        }catch(RemoteException e){
            e.printStackTrace();
        }

        break;
      case R.id.test8:
        break;

    }
  }
  
  
  private ServiceConnection connection = new ServiceConnection() {
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG,"onServiceConnected");
        mBinder = ImainService.Stub.asInterface(service);
    }
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG,"onServiceDisConnected");
        mBinder = null;
    }
};

  
  public class KeyEventSender extends AsyncTask<String, Integer, Integer> {
    @Override
    protected Integer doInBackground(String... params) {
      Log.i(TAG,"keysync");
      Instrumentation ist = new Instrumentation();
      ist.sendKeyDownUpSync(KeyEvent.KEYCODE_APP_SWITCH);
      return null;
    }
  }
  
}
