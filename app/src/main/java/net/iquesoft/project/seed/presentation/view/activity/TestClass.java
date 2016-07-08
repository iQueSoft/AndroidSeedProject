package net.iquesoft.project.seed.presentation.view.activity;

import android.content.Context;

import net.iquesoft.project.seed.utils.LogUtil;

import javax.inject.Inject;


public class TestClass {
    Context context;

    @Inject
    public TestClass(Context context) {
        this.context = context;
    }

    void someFunc() {
        LogUtil.makeLog("TEST CLASS " + context);
    }
}
