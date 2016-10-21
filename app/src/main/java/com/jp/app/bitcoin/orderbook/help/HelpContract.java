package com.jp.app.bitcoin.orderbook.help;

import com.jp.app.bitcoin.orderbook.BasePresenter;
import com.jp.app.bitcoin.orderbook.BaseView;

/**
 * Created by jp on 16. 10. 21..
 */
public interface HelpContract {
    interface View extends BaseView<Presenter>{

        void showStep1();
        void showStep2();
        void showStep3();
        boolean isActive();

    }
    interface Presenter extends BasePresenter {
        void showNextStep();

    }
}
