package com.techscl.btsearch;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Request;

/**
 * 创建者 songchunlin.
 * 创建时间 2017/9/20 12:56.
 * 邮箱 songchunlin1314@gmail.com
 * 备注:
 * 修改者 songchunlin
 * 修改时间 2017/9/20 12:56.
 * 邮箱
 * 备注:
 */

public class BTDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView img;
    private TextView id;
    private TextView name;
    private TextView content;
    private TextView time;
    private TextView resId;
    private TextView resContent;
    private TextView count;
    private TextView from;
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        initView();
        setSupportActionBar(toolbar);
        if (toolbar != null && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(getString(R.string.details));
        BT.DataBean dataBean = getIntent().getParcelableExtra("data");
        if (dataBean != null) {
            OkHttpClientManager.getAsyn(AppConfig.SERVER + "api/bt/click/" + dataBean.getId(), new OkHttpClientManager.ResultCallback<ResponseInfo>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(ResponseInfo response) {
                    setResult(1002);
                }
            });
            Glide.with(BTDetailsActivity.this)
                    .load(dataBean.getImgUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(img);
            id.setText("数据ID：" + String.valueOf(dataBean.getId()));
            name.setText("名称：" + dataBean.getName());
            content.setText("内容：" + dataBean.getContent());
            time.setText("发布时间：" + simpleDateFormat.format(new Date(dataBean.getUpdateTime())));
            count.setText("热度：" + dataBean.getDownload());
            resId.setText(dataBean.getUriId());
            resContent.setText(dataBean.getUriContent());
            from.setText("发布者：" + (dataBean.getPhoneNumber() != null ? dataBean.getPhoneNumber() : "无USIM卡") +
                    "@" + (dataBean.getImei() != null ? dataBean.getImei() : "模拟器"));
            resId.setOnClickListener(this);
            resContent.setOnClickListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        img = (ImageView) findViewById(R.id.img);
        id = (TextView) findViewById(R.id.id);
        name = (TextView) findViewById(R.id.name);
        content = (TextView) findViewById(R.id.content);
        time = (TextView) findViewById(R.id.time);
        resId = (TextView) findViewById(R.id.resId);
        resContent = (TextView) findViewById(R.id.resContent);
        count = (TextView) findViewById(R.id.count);
        from = (TextView) findViewById(R.id.from);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resId:
                clipboardManager.setPrimaryClip(ClipData.newPlainText("resId", resId.getText().toString()));
                Toast.makeText(this, "资源ID复制到剪贴板", Toast.LENGTH_SHORT).show();
                break;
            case R.id.resContent:
                clipboardManager.setPrimaryClip(ClipData.newPlainText("resContent", resContent.getText().toString()));
                Toast.makeText(this, "资源内容复制到剪贴板", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
