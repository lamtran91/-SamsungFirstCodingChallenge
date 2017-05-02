package lamtran.net.samsungandroidprototype;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRV;
    private AssetManager mAssetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAssetManager = getAssets();

        mRV = (RecyclerView) findViewById(R.id.recycler_view);
        mRV.setHasFixedSize(true);
        mRV.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRV.setAdapter(new SummaryViewAdapter(Utils.buildCategories(mAssetManager), mAssetManager,

                new RecyclerViewImageAdapter.OnCategoryClickListener() {
                    @Override
                    public void onLongClick(int catId) {
                        Intent detailActivity = new Intent(MainActivity.this, DetailActivity.class);
                        detailActivity.putExtra(DetailActivity.BUNDLE_EXTRA, catId);
                        startActivity(detailActivity);
                    }

                    @Override
                    public void onClick(String cat) {

                    }
                }));
    }
}
