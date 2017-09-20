package com.techscl.btsearch;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * 创建者 songchunlin.
 * 创建时间 2017/9/20 11:12.
 * 邮箱 songchunlin1314@gmail.com
 * 备注:
 * 修改者 songchunlin
 * 修改时间 2017/9/20 11:12.
 * 邮箱
 * 备注:
 */

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText name;
    private EditText content;
    private EditText resId;
    private EditText resContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bt);
        initView();
        setSupportActionBar(toolbar);
        if (toolbar != null && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("添加");
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (EditText) findViewById(R.id.name);
        content = (EditText) findViewById(R.id.content);
        resId = (EditText) findViewById(R.id.resId);
        resContent = (EditText) findViewById(R.id.resContent);
        Button btnAdd = (Button) findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                submit();
                break;
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

    private void submit() {
        // validate
        String nameString = name.getText().toString().trim();
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, "名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String contentString = content.getText().toString().trim();
        if (TextUtils.isEmpty(contentString)) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String resIdString = resId.getText().toString().trim();
        if (TextUtils.isEmpty(resIdString)) {
            Toast.makeText(this, "资源ID不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String resContentString = resContent.getText().toString().trim();
        if (TextUtils.isEmpty(resContentString)) {
            Toast.makeText(this, "资源内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        Map<String, String> params = new HashMap<>();
        params.put("name", name.getText().toString());
        params.put("content", content.getText().toString());
        params.put("uriId", resId.getText().toString());
        params.put("uriContent", resContent.getText().toString());
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher)
                .setMessage("正在发布，请稍后...")
                .setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        OkHttpClientManager.postAsyn(AppConfig.SERVER + "api/bt/add", new OkHttpClientManager.ResultCallback<ResponseInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                alertDialog.dismiss();
            }

            @Override
            public void onResponse(ResponseInfo response) {
                alertDialog.dismiss();
                if (response.getCode() == 0)
                    finish();
            }
        }, params);

    }
}
