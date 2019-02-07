package ntt.bps.namberwan.allstatntt;


import android.Manifest;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;

import ntt.bps.namberwan.allstatntt.berita.BeritaFragment;
import ntt.bps.namberwan.allstatntt.brs.BrsFragment;
import ntt.bps.namberwan.allstatntt.chat.ChatActivity;
import ntt.bps.namberwan.allstatntt.chat.ViewChatAdminActivity;
import ntt.bps.namberwan.allstatntt.indikator.IndikatorFragment;
import ntt.bps.namberwan.allstatntt.publikasi.PublikasiFragment;
import ntt.bps.namberwan.allstatntt.tabelstatis.TabelFragment;

public class MainActivity extends AppCompatActivity {

    public static final String SEARCH_KEYWORD = "search keyword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_bps_launcher);
        }

        IndikatorFragment indikatorFragment = new IndikatorFragment();
        BrsFragment brsFragment = new BrsFragment();
        PublikasiFragment publikasiFragment= new PublikasiFragment();
        TabelFragment tabelFragment = new TabelFragment();
        BeritaFragment beritaFragment = new BeritaFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(indikatorFragment, "Indikator");
        viewPagerAdapter.addFragment(brsFragment, "BRS");
        viewPagerAdapter.addFragment(publikasiFragment, "Publikasi");
        viewPagerAdapter.addFragment(tabelFragment, "Tabel Statis");
        viewPagerAdapter.addFragment(beritaFragment, "Berita");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());

        checkPermission();

        AppUtils.createNotificationChannel(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), ViewChatAdminActivity.class);
                startActivity(i);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("sented")!=null){
                String sender = bundle.getString("sented");
                String receiver = bundle.getString("user");
                String username = bundle.getString("username");
                String photo = bundle.getString("photo");

                Intent i = new Intent(this, ChatActivity.class);
                i.putExtra(ChatActivity.ID_ADMIN_RECEIVER, receiver);
                i.putExtra(ChatActivity.ID_USER_SENDER, sender);
                i.putExtra(ChatActivity.USERNAME_RECEIVER, username);
                i.putExtra(ChatActivity.URL_PHOTO_RECEIVER, photo);
                startActivity(i);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(SEARCH_SERVICE);

        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            if (searchManager!=null){
                SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
                searchView.setSearchableInfo(searchableInfo);
                searchView.setOnSearchClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSearchRequested();
                    }
                });
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return true;
                    }
                });
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }else*/ if (id == R.id.action_search){
            onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested(@Nullable SearchEvent searchEvent) {
        return super.onSearchRequested(searchEvent);
    }
}