package com.jp.app.bitcoin.orderbook.help;

import android.support.annotation.NonNull;

/**
 * Created by jp on 16. 10. 21..
 */
public class HelpPresenter implements HelpContract.Presenter {
    private int currenctStep = 0;
    private final HelpContract.View mHelpView;

    @Override
    public void start() {

    }

    public HelpPresenter(@NonNull HelpContract.View helpView) {

        this.mHelpView = helpView;
        mHelpView.setPresenter(this);
    }


    @Override
    public void showNextStep() {
        if (currenctStep == 0) {
            mHelpView.showStep1();
            currenctStep++;

        } else if (currenctStep == 1) {
            mHelpView.showStep2();

            currenctStep++;
        } else if (currenctStep == 2) {

            mHelpView.showStep3();

        }
    }
}
