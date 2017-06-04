package com.jp.app.bitcoin.orderbook;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by jp on 2017. 6. 3..
 */
public class DonationActivity extends BaseActivity{
    private Button mBtnBtc, mBtcEth, mBtcEtc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_act);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.donation));
        mBtnBtc = (Button) findViewById(R.id.dontion_btn_btc);
        mBtcEth = (Button) findViewById(R.id.dontion_btn_eth);
        mBtcEtc = (Button) findViewById(R.id.dontion_btn_etc);

        mBtnBtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(getString(R.string.donation_btc)+""+getString(R.string.donation_text_copy));
                copyClipboard(getString(R.string.donation_address_btc));
            }
        });
        mBtcEth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(getString(R.string.donation_eth)+""+getString(R.string.donation_text_copy));
                copyClipboard(getString(R.string.donation_address_eth));
            }
        });
        mBtcEtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(getString(R.string.donation_etc)+""+getString(R.string.donation_text_copy));
                copyClipboard(getString(R.string.donation_address_etc));

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void copyClipboard(String text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", text);
        clipboard.setPrimaryClip(clip);
    }
    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
