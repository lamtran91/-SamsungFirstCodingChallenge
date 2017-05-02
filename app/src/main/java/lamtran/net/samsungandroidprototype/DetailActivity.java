package lamtran.net.samsungandroidprototype;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class DetailActivity extends AppCompatActivity {
    public static final String BUNDLE_EXTRA = "bundle_category_extra";
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        boolean hideToolBar = false;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (hideToolBar) {
                getSupportActionBar().hide();
            } else {
                getSupportActionBar().show();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 20) {
                hideToolBar = true;

            } else if (dy < -5) {
                hideToolBar = false;
            }
        }
    };

    private RecyclerView mRV;
    private AssetManager mAssetManager;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.container);
        mCoordinatorLayout.setBackgroundResource(android.R.color.background_light);

        int cat = getIntent().getIntExtra(BUNDLE_EXTRA, -1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final AppBarLayout layout = (AppBarLayout) findViewById(R.id.appbar);
        layout.setVisibility(View.VISIBLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mAssetManager = getAssets();
        mRV = (RecyclerView) findViewById(R.id.recycler_view);
        mRV.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        mRV.setLayoutManager(layoutManager);
        mRV.setAdapter(new DetailViewAdapter(Utils.buildCategories(mAssetManager), mAssetManager, cat));

        mRV.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int deltaY = scrollY - oldScrollY;
                if (deltaY > 10) {
//                    layout.setVisibility(View.VISIBLE);
//                    Animation ani = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_down);
//                    layout.startAnimation(ani);
                } else if (deltaY < 10) {
//                    layout.setVisibility(View.GONE);
//                    Animation ani = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_up);
//                    layout.startAnimation(ani);
                }

//                layout.setExpanded(mRV.canScrollVertically(View.FOCUS_DOWN), true);
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.slide_down);
    }
}
