package com.cocoagarage.application.goodspeaks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cocoagarage.application.goodspeaks.Adapters.CompetentCommunicationAdapter;
import com.cocoagarage.application.goodspeaks.Data.DataManager;
import com.cocoagarage.application.goodspeaks.R;

public class competentCommunication extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView_cc;

    CompetentCommunicationAdapter adapter = null;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_competent_communication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView_cc = (ListView)findViewById(R.id.listview_cc);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

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


        adapter = new CompetentCommunicationAdapter(this);
        listView_cc.setAdapter(adapter);
        final Context appContext = this;
        listView_cc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(competentCommunication.this,Speech_Detail.class);
                intent.putExtra("SpeechProject", DataManager.sharedManager().getSpeechProjects(appContext).get(i));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
        adapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.competent_communication, menu);
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
        Utility.onNaviationItemSelected(this, item);
        return true;
    }
}
