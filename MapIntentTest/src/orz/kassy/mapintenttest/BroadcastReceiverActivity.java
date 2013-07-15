package orz.kassy.mapintenttest;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.app.KeyguardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class BroadcastReceiverActivity extends Activity implements OnClickListener{

    private static final String TEST_RECEIVER_ACTION = "TestReceiverAction";
    private static final String TAG = "broadcastreceiver";
    TestReceiver mReceiver = new TestReceiver();
    
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast);
        
        Button btnStart = (Button) findViewById(R.id.startReceiver);
        btnStart.setOnClickListener(this);
        Button btnStop = (Button) findViewById(R.id.stopReceiver);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startReceiver:
                IntentFilter filter = new IntentFilter();
                filter.addAction(TEST_RECEIVER_ACTION);
                Log.i(TAG,"register receiver");
                registerReceiver(mReceiver, filter);
                break;
            case R.id.stopReceiver:
                Log.i(TAG,"unregister receiver");
                unregisterReceiver(mReceiver);
                break;
            default:
                break;
        }
        
    }

    /**
     * ブロードキャストレシーバーのクラス
     * @author kashimoto
     * 動作確認は adb shellのあと、
     * am broadcast -a (TEST_RECEIVER_ACTION ※囲みなしで) 
     */
    public class TestReceiver extends BroadcastReceiver{
        
        @Override
        public void onReceive(Context context, Intent intent){
            // インテントを受け取ったときの処理をここに記述
            Toast.makeText(context, "receive broadcast", Toast.LENGTH_LONG).show();
            Log.w(TAG,"Receive Broadcast and action = "+ intent.getAction());
        }
    }

    /**
     * ブロードキャストレシーバーのクラス
     * これはAndroidManifest.xmlで登録する
     * そのため、必ずstaticにする必要がある
     * 動作確認は adb shellのあと、
     * am broadcast -a orz.kassy.receiver2 
     */
    static public class TestReceiver2 extends BroadcastReceiver{
        
        /**
         * ここでは、スリープ復帰をしてみる
         */
        @Override
        public void onReceive(Context context, Intent intent){
            // インテントを受け取ったときの処理をここに記述
            Toast.makeText(context, "receive2 broadcast", Toast.LENGTH_LONG).show();
            Log.w(TAG,"Receive2 Broadcast and action = "+ intent.getAction());
            Instrumentation ist = new Instrumentation();
            ist.sendKeyDownUpSync(KeyEvent.KEYCODE_K);

        }
    }
}
