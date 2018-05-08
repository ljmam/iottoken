package co.yzq.zxing;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.yzq.zxing.R;

/**
 * Created by jimmylee on 2018/2/21.
 */

    public class ReData extends AppCompatActivity {
        private TextView showdata;
        protected static String str;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        //    showdata.setText(str);
            setContentView(R.layout.redata);
            /*扫描结果*/
            showdata = (TextView) findViewById(R.id.showdata);
            Message msg = hand2.obtainMessage();
            msg.obj = str;
            hand2.sendMessage(msg);
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    Handler hand2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null) {
                showdata.setText((String)msg.obj);
            }
        };
    };
}
