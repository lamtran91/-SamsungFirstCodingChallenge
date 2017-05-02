package lamtran.net.samsungandroidprototype;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

        mAssetManager = getAssets();
        mRV = (RecyclerView) findViewById(R.id.recycler_view);
        mRV.setHasFixedSize(true);
        mRV.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRV.setAdapter(new DetailViewAdapter(Utils.buildCategories(mAssetManager), mAssetManager, cat));
    }
}
