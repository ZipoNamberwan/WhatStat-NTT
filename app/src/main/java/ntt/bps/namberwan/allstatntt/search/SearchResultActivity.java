package ntt.bps.namberwan.allstatntt.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ntt.bps.namberwan.allstatntt.MainActivity;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.ViewPagerAdapter;
import ntt.bps.namberwan.allstatntt.berita.BeritaFragment;
import ntt.bps.namberwan.allstatntt.brs.BrsFragment;
import ntt.bps.namberwan.allstatntt.indikator.IndikatorFragment;
import ntt.bps.namberwan.allstatntt.publikasi.PublikasiFragment;
import ntt.bps.namberwan.allstatntt.tabelstatis.TabelFragment;

public class SearchResultActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        setTitle("'" + query + "'");
        Bundle args = new Bundle();
        args.putString(MainActivity.SEARCH_KEYWORD, query);

        BrsFragment brsFragment = new BrsFragment();
        brsFragment.setArguments(args);
        PublikasiFragment publikasiFragment = new PublikasiFragment();
        publikasiFragment.setArguments(args);
        TabelFragment tabelFragment = new TabelFragment();
        tabelFragment.setArguments(args);
        BeritaFragment beritaFragment = new BeritaFragment();
        beritaFragment.setArguments(args);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(brsFragment, "BRS");
        viewPagerAdapter.addFragment(publikasiFragment, "Publikasi");
        viewPagerAdapter.addFragment(tabelFragment, "Tabel Statis");
        viewPagerAdapter.addFragment(beritaFragment, "Berita");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
