package lamtran.net.samsungandroidprototype;

import android.content.res.AssetManager;

import java.util.List;

/**
 * Created by lam on 5/1/17.
 */
public class SummaryViewAdapter extends RecyclerViewImageAdapter {
    private static final String TAG = SummaryViewAdapter.class.getSimpleName();
    private List<Category> mCategories;

    public SummaryViewAdapter(List<Category> list, AssetManager assetManager, OnCategoryClickListener listener) {
        super(list, assetManager, true, listener);
        mAssetManager = assetManager;
        mCategories = list;
        mLayout = R.layout.image_summary_item;
    }

    @Override
    public int getItemCount() {
        return mCategories != null ? mCategories.size() : -1;
    }
}
