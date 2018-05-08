package co.yzq.zxing;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yzq.zxing.R;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author: yzq
 * @date: 2017/10/26 15:17
 * @declare :
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView scanBtn;
    private ImageView dd1;
    private ImageView dd2;
    private ImageView dd3;
    private ImageView dd4;
    private WebView webView;
    private LinearLayout LinearLayout1;
    private String url="https://www.iottoken.co/androidhome.php";//地址
    private String url1="https://www.iottoken.co/androidhome1.php";//地址
    private String url2="https://www.iottoken.co/androidhome2.php";//地址
    private String url3="https://www.iottoken.co/androidhome3.php";//地址
    private int REQUEST_CODE_SCAN = 111;
    private Switch Txhash;
    private Switch Blockno;
    private Switch UnixTimestamp;
    private Switch DateTime;
    private Switch From;
    private Switch To;
    private Switch Quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Txhash = (Switch) findViewById(R.id.Txhash);
        Txhash.setOnClickListener(this);
        Blockno = (Switch) findViewById(R.id.Blockno);
        Blockno.setOnClickListener(this);
        UnixTimestamp = (Switch) findViewById(R.id.UnixTimestamp);
        UnixTimestamp.setOnClickListener(this);
        DateTime = (Switch) findViewById(R.id.DateTime);
        DateTime.setOnClickListener(this);
        From = (Switch) findViewById(R.id.From);
        From.setOnClickListener(this);
        To = (Switch) findViewById(R.id.To);
        To.setOnClickListener(this);
        Quantity = (Switch) findViewById(R.id.Quantity);
        Quantity.setOnClickListener(this);
        SharedPreferences sp1 = getSharedPreferences("iotshezhi", 0);
        Txhash.setChecked(sp1.getBoolean("Txhash", true));
        Blockno.setChecked(sp1.getBoolean("Blockno", true));
        UnixTimestamp.setChecked(sp1.getBoolean("UnixTimestamp", true));
        DateTime.setChecked(sp1.getBoolean("DateTime", true));
        From.setChecked(sp1.getBoolean("From", true));
        To.setChecked(sp1.getBoolean("To", true));
        Quantity.setChecked(sp1.getBoolean("Quantity", true));

        initView();
    }


    private void initView() {
        /*扫描按钮*/
        scanBtn = (ImageView) findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);
        dd1 = (ImageView) findViewById(R.id.dd);
        dd1.setOnClickListener(this);
        dd2 = (ImageView) findViewById(R.id.dd2);
        dd2.setOnClickListener(this);
        dd3 = (ImageView) findViewById(R.id.dd3);
        dd3.setOnClickListener(this);
        dd4 = (ImageView) findViewById(R.id.dd4);
        dd4.setOnClickListener(this);
        /*扫描结果*/
        webView = (WebView) findViewById(R.id.webview);
        LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout);
        webView.getSettings().setJavaScriptEnabled(true);// 支持运行javascript
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;//ture为在webview中打开
            }
        });
    }

    @Override
    public void onClick(View v) {

        SharedPreferences sp = getSharedPreferences("iotshezhi",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        switch (v.getId()) {
            case R.id.scanBtn:
                dd1.setImageResource(R.drawable.a1_2);
                dd2.setImageResource(R.drawable.a3_2);
                dd3.setImageResource(R.drawable.a4_2);
                dd4.setImageResource(R.drawable.a2_2);
               AndPermission.with(this)
                        .permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .callback(new PermissionListener() {
                            @Override
                            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);

                                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                                * 也可以不传这个参数
                                * 不传的话  默认都为默认不震动  其他都为true
                                * */

                                ZxingConfig config = new ZxingConfig();
                                config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
                                config.setPlayBeep(true);//是否播放提示音
                                config.setShake(true);//是否震动
                                //config.setShowAlbum(true);//是否显示相册
                                config.setShowFlashLight(true);//是否显示闪光灯
                                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);

                                startActivityForResult(intent, REQUEST_CODE_SCAN);
                            }

                            @Override
                            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

                                Uri packageURI = Uri.parse("package:" + getPackageName());
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);

                                Toast.makeText(MainActivity.this, "Cannot scan without permission", Toast.LENGTH_LONG).show();
                            }
                        }).start();
                break;
            case R.id.dd:
                webView.setVisibility(View.VISIBLE);
                LinearLayout1.setVisibility(View.GONE);
                dd1.setImageResource(R.drawable.a1_1);
                dd2.setImageResource(R.drawable.a3_2);
                dd3.setImageResource(R.drawable.a4_2);
                dd4.setImageResource(R.drawable.a2_2);
                webView.loadUrl(url1);
                webView.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;//ture为在webview中打开
                    }
                });
                break;
            case R.id.dd2:
                dd1.setImageResource(R.drawable.a1_2);
                dd2.setImageResource(R.drawable.a3_1);
                dd3.setImageResource(R.drawable.a4_2);
                dd4.setImageResource(R.drawable.a2_2);
                webView.setVisibility(View.VISIBLE);
                LinearLayout1.setVisibility(View.GONE);
                webView.loadUrl(url2);
                webView.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;//ture为在webview中打开
                    }
                });
                break;
            case R.id.dd3:
                dd1.setImageResource(R.drawable.a1_2);
                dd2.setImageResource(R.drawable.a3_2);
                dd3.setImageResource(R.drawable.a4_1);
                dd4.setImageResource(R.drawable.a2_2);
                webView.setVisibility(View.VISIBLE);
                LinearLayout1.setVisibility(View.GONE);
                webView.loadUrl(url3);
                webView.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;//ture为在webview中打开
                    }
                });
                break;
            case R.id.dd4:
                dd1.setImageResource(R.drawable.a1_2);
                dd2.setImageResource(R.drawable.a3_2);
                dd3.setImageResource(R.drawable.a4_2);
                dd4.setImageResource(R.drawable.a2_1);
                webView.setVisibility(View.GONE);
                LinearLayout1.setVisibility(View.VISIBLE);
                break;
            case R.id.Txhash:
                editor.putBoolean("Txhash", Txhash.isChecked());
                editor.commit();
                break;
            case R.id.Blockno:
                editor.putBoolean("Blockno", Blockno.isChecked());
                editor.commit();
                break;
            case R.id.UnixTimestamp:
                editor.putBoolean("UnixTimestamp", UnixTimestamp.isChecked());
                editor.commit();
                break;
            case R.id.DateTime:
                editor.putBoolean("DateTime", DateTime.isChecked());
                editor.commit();
                break;
            case R.id.From:
                editor.putBoolean("From", From.isChecked());
                editor.commit();
                break;
            case R.id.To:
                editor.putBoolean("To", To.isChecked());
                editor.commit();
                break;
            case R.id.Quantity:
                editor.putBoolean("Quantity", Txhash.isChecked());
                editor.commit();
                break;

            default:
        }
    }

    Handler hand = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
                    if (msg.obj != null) {

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, ReData.class);
                        ReData.str=(String)msg.obj;
                        startActivity(intent);
                    }
        };
    };

    private void postRequest(final String qcode){
        OkHttpClient client =new OkHttpClient();
        FormBody.Builder formBodyBuild = new FormBody.Builder();
        formBodyBuild.add("qcode",qcode);
        Request requst = new Request.Builder().url("https://www.iottoken.co/request").post(formBodyBuild.build()).build();
        Call call = client.newCall(requst);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            //也是非UI线程
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SharedPreferences sp1 = getSharedPreferences("iotshezhi", 0);
                            String ssv="";
                           // String ss = JSON.parseObject(res).getString("version");
                                Message msg = hand.obtainMessage();
                            if(sp1.getBoolean("Txhash", true)){
                                ssv = ssv+"\n\nTxhash:\n"+ JSON.parseObject(res).getString("Txhash");
                            }
                            if(sp1.getBoolean("Blockno", true)){
                                ssv = ssv+"\n\nBlock Height:\n"+ JSON.parseObject(res).getString("Blockno");
                            }
                            if(sp1.getBoolean("UnixTimestamp", true)){
                                ssv = ssv+"\n\nUnixTimestamp:\n"+ JSON.parseObject(res).getString("UnixTimestamp");
                            }
                            if(sp1.getBoolean("DateTime", true)){
                                ssv = ssv+"\n\nDateTime:\n"+ JSON.parseObject(res).getString("DateTime");
                            }
                            if(sp1.getBoolean("From", true)){
                                ssv = ssv+"\n\nFrom:\n"+ JSON.parseObject(res).getString("From");
                            }
                            if(sp1.getBoolean("To", true)){
                                ssv = ssv+"\n\nTo:\n"+ JSON.parseObject(res).getString("To");
                            }
                            if(sp1.getBoolean("Quantity", true)){
                                ssv = ssv+"\n\nQuantity:\n"+ JSON.parseObject(res).getString("Quantity");
                            }
                                msg.obj = ssv;
                                // 给主ui发送消息传递数据
                                hand.sendMessage(msg);
                        }catch (Exception e){
                            Message msg = hand.obtainMessage();
                            msg.obj = "web error";
                            // 给主ui发送消息传递数据
                            hand.sendMessage(msg);
                        }

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.info, menu);

        //menu.add(1, Menu.FIRST, 1, "Change Site ID");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.quit1:
                super.finish();
                System.exit(0);
                return true;
            default:
                return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                content=content.substring(36);
               postRequest(content);

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            Intent i= new Intent(Intent.ACTION_MAIN);  //主启动，不期望接收数据

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);       //新的activity栈中开启，或者已经存在就调到栈前

            i.addCategory(Intent.CATEGORY_HOME);            //添加种类，为设备首次启动显示的页面

            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }
}
