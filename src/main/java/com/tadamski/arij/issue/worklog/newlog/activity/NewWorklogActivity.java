package com.tadamski.arij.issue.worklog.newlog.activity;

import android.support.v7.app.ActionBarActivity;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.util.analytics.Tracker;

import java.util.Date;

/**
 * @author tmszdmsk
 */
@EActivity(R.layout.worklog_new_activity)
public class NewWorklogActivity extends ActionBarActivity implements NewWorklogFragment.WorkLoggedListener {

    public static final int REQUEST_CODE_LOG = 125;
    private static final String TAG = NewWorklogActivity.class.getName();
    @FragmentById(R.id.worklog_fragment)
    NewWorklogFragment fragment;
    @Extra
    String issueKey;
    @Extra
    Date startDate;
    @Extra
    LoginInfo loginInfo;
    @NonConfigurationInstance
    boolean loaded = false;

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tracker.activityStop(this);
    }

    @AfterViews
    protected void prepareFragment() {
        if (startDate == null) {
            startDate = new Date(System.currentTimeMillis() - 60 * 60 * 1000);
        }
        Long seconds = Math.max((new Date().getTime() - startDate.getTime()) / 1000, 60);
        if (!loaded) {
            setTitle(issueKey);
            fragment.prepare(issueKey, startDate, seconds, loginInfo);
            loaded = true;
        }
    }

    @Override
    public void onWorkLogged() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onWorkDiscarded() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
