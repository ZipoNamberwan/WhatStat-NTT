package ntt.bps.namberwan.allstatntt;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ntt.bps.namberwan.allstatntt.berita.BeritaFragment;
import ntt.bps.namberwan.allstatntt.brs.BrsFragment;
import ntt.bps.namberwan.allstatntt.chat.AskFragment;
import ntt.bps.namberwan.allstatntt.indikator.IndikatorFragment;
import ntt.bps.namberwan.allstatntt.publikasi.PublikasiFragment;
import ntt.bps.namberwan.allstatntt.tabelstatis.TabelFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        tabLayout =findViewById(R.id.tabs);
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
        AskFragment askFragment = new AskFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(indikatorFragment, "Indikator");
        viewPagerAdapter.addFragment(brsFragment, "BRS");
        viewPagerAdapter.addFragment(publikasiFragment, "Publikasi");
        viewPagerAdapter.addFragment(tabelFragment, "Tabel Statis");
        viewPagerAdapter.addFragment(beritaFragment, "Berita");
        //viewPagerAdapter.addFragment(askFragment, "Ask Me!");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());

        checkPermission();

        AppUtil.createNotificationChannel(this);

        /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //PublikasiFragment publikasiFragment = new PublikasiFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container, publikasiFragment).commit();

        //BrsFragment brsFragment = new BrsFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container, brsFragment).commit();

        //BeritaFragment beritaFragment = new BeritaFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container, beritaFragment).commit();

        /*Drawer drawer = new DrawerBuilder(this)
                .withToolbar(toolbar)
                .withActivity(this)
                .withActionBarDrawerToggleAnimated(true)
                .withHeader(R.layout.navigation_drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.home).withIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_home).color(ContextCompat.getColor(this, R.color.nav_item_home))),
                        new PrimaryDrawerItem().withName(R.string.brs).withIcon(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_sigma).color(ContextCompat.getColor(this, R.color.nav_item_brs))),
                        new PrimaryDrawerItem().withName(R.string.berita).withIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_newspaper_o).color(ContextCompat.getColor(this, R.color.nav_item_berita))),
                        new PrimaryDrawerItem().withName(R.string.tabel).withIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_table).color(ContextCompat.getColor(this, R.color.nav_item_tabel))),
                        new PrimaryDrawerItem().withName(R.string.publikasi).withIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_book).color(ContextCompat.getColor(this, R.color.nav_item_publikasi))),
                        new PrimaryDrawerItem().withName(R.string.infografis).withIcon(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_chart_pie).color(ContextCompat.getColor(this, R.color.nav_item_infografis))),
                        new SectionDrawerItem().withName(R.string.lainnya),
                        new PrimaryDrawerItem().withName(R.string.bookmark).withIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_collections_bookmark).color(ContextCompat.getColor(this, R.color.nav_item_other))),
                        new PrimaryDrawerItem().withName(R.string.pengaturan).withIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_settings).color(ContextCompat.getColor(this, R.color.nav_item_other))),
                        new PrimaryDrawerItem().withName(R.string.bantuan).withIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_info_outline).color(ContextCompat.getColor(this, R.color.nav_item_other))))
                .withSliderBackgroundColor(ContextCompat.getColor(this, R.color.md_white_1000))
                .build();*/

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
        }*/

        return super.onOptionsItemSelected(item);
    }
}