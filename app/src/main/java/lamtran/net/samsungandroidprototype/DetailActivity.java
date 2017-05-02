package lamtran.net.samsungandroidprototype;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class DetailActivity extends AppCompatActivity {
    public static final String BUNDLE_EXTRA = "bundle_category_extra";
    private static final String TAG = DetailActivity.class.getSimpleName();
    private RecyclerView mRV;
    private AssetManager mAssetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int cat = getIntent().getIntExtra(BUNDLE_EXTRA, -1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout layout = (AppBarLayout) findViewById(R.id.appbar);
        layout.setVisibility(View.VISIBLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mAssetManager = getAssets();
        mRV = (RecyclerView) findViewById(R.id.recycler_view);
        mRV.setHasFixedSize(true);
        mRV.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRV.setAdapter(new DetailViewAdapter(Utils.buildCategories(mAssetManager), mAssetManager, cat));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
