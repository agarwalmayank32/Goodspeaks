package com.cocoagarage.application.goodspeaks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cocoagarage.application.goodspeaks.Adapters.CompetentLeadershipListAdapter;
import com.cocoagarage.application.goodspeaks.Data.DataManager;
import com.cocoagarage.application.goodspeaks.Models.LeadershipRole;
import com.cocoagarage.application.goodspeaks.Models.LeadershipProject;
import com.cocoagarage.application.goodspeaks.R;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Competent_Leadership extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    CompetentLeadershipListAdapter listAdapter;

    ExpandableListView listView_cl;
    List<String> listHeader;
    HashMap<String, List<String>> listChild;

    LeadershipProject leadershipProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competent_leadership);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        CompetentLeadershipListAdapter mAdapter;

        listView_cl = (ExpandableListView)findViewById(R.id.listview_cl);
        listView_cl.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //Add Color to Mentor,Goals and Article
        Menu menu = navigationView.getMenu();
        MenuItem mentor= menu.findItem(R.id.mentor);
        MenuItem goal=menu.findItem(R.id.goals);
        MenuItem aticle=menu.findItem(R.id.articles);
        SpannableString m = new SpannableString(mentor.getTitle());
        SpannableString g = new SpannableString(goal.getTitle());
        SpannableString a = new SpannableString(aticle.getTitle());
        m.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance1), 0, m.length(), 0);
        g.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance1), 0, g.length(), 0);
        a.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance1), 0, a.length(), 0);
        mentor.setTitle(m);
        goal.setTitle(g);
        aticle.setTitle(a);

        navigationView.setNavigationItemSelectedListener(this);

        mAdapter = new CompetentLeadershipListAdapter(this, listView_cl);
        final CompetentLeadershipListAdapter finalAdapter = mAdapter;
        listView_cl.setAdapter(mAdapter);
        listView_cl.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                finalAdapter.notifyDataSetChanged();
                return false;
            }
        });
        final Context currentContext = this;
        listView_cl.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                LeadershipProject leadershipProject = DataManager.sharedManager().getLeadershipProjects(currentContext).get(groupPosition);
                LeadershipRole role = DataManager.sharedManager().getLeadershipProject(currentContext,leadershipProject.get_ID()).getRoles().get(childPosition);
                CheckedTextView textView = (CheckedTextView) view.findViewById(R.id.checkedTextView);
                TextView dateTextView = (TextView) view.findViewById(R.id.textView4);
                Date completionDate;
                if (textView.isChecked()) {
                    // Uncheck
                    role.setCompleted(false);
                    completionDate = null;role.setCompletionDate(completionDate);
                    textView.setChecked(false);
                    dateTextView.setText(Utility.convertDateToString(role.getCompletionDate()));
                    DataManager.sharedManager().updateLeadershipRole(currentContext, role);
                } else {
                    // Check.
                    role.setCompleted(true);
                    completionDate = Utility.todaysDate();
                    role.setCompletionDate(completionDate);
                    dateTextView.setText(Utility.convertDateToString(role.getCompletionDate()));
                    DataManager.sharedManager().updateLeadershipRole(currentContext, role);
                    textView.setChecked(true);
                }
                finalAdapter.notifyDataSetChanged();
                return true;
            }
        });
        listView_cl.expandGroup(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.competent_leadership, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        Utility.onNaviationItemSelected(this, item);
        return true;
    }
}
