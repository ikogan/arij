package com.tadamski.arij.issue.resource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.issue.IssueResource;
import com.tadamski.arij.issue.resource.issue.IssueUpdate;
import com.tadamski.arij.issue.resource.issue.IssuesResultList;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.resource.model.updates.IssueUpdateParams;
import com.tadamski.arij.issue.resource.search.SearchParams;
import com.tadamski.arij.issue.resource.search.SearchResource;
import com.tadamski.arij.util.database.DatabaseOpenHelper;
import com.tadamski.arij.util.database.DatabaseOpeningException;
import com.tadamski.arij.util.retrofit.RestAdapterProvider;

import java.sql.SQLException;

/**
 * Created by tmszdmsk on 08.07.13.
 */
@EBean
public class IssueService {
    @Bean
    DatabaseOpenHelper databaseHelper;

    private SQLiteDatabase db = null;

    @AfterInject
    @Background
    public void init() {
        db = databaseHelper.getWritableDatabase();
    }

    public boolean favoriteIssue(String issueKey) throws SQLException {
        if(db != null) {
            Cursor result = null;

            try {
                db.beginTransaction();
                result = db.query(DatabaseOpenHelper.FAVORITES_TABLE_NAME, new String[] {"issue_key"}, "issue_key = ?", new String[] {issueKey}, null, null, null);

                if(result.getCount() > 0) {
                    return false;
                } else {
                    ContentValues values = new ContentValues();
                    values.put("issue_key", issueKey);

                    if(db.insert(DatabaseOpenHelper.FAVORITES_TABLE_NAME, null, values) > 0) {
                        return true;
                    } else {
                        throw new SQLException("Error adding " + issueKey + " to favorites");
                    }
                }
            } finally {
                if(result != null) {
                    result.close();
                }

                db.endTransaction();
            }
        } else {
            throw new DatabaseOpeningException();
        }
    }

    public Issue getIssue(LoginInfo loginInfo, String issueKey) {
        IssueResource issueResource = RestAdapterProvider.get(IssueResource.class, loginInfo);
        return issueResource.getIssue(issueKey);
    }

    public Issue updateIssue(LoginInfo loginInfo, String issueKey, IssueUpdateParams updateParams) {
        IssueResource issueResource = RestAdapterProvider.get(IssueResource.class, loginInfo);
        return issueResource.updateIssue(issueKey, new IssueUpdate(updateParams));
    }

    public IssuesResultList search(LoginInfo loginInfo, SearchParams searchParams) {
        SearchResource searchResource = RestAdapterProvider.get(SearchResource.class, loginInfo);
        return searchResource.searchForIssues(searchParams);
    }
}
