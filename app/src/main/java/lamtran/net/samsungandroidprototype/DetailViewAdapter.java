package lamtran.net.samsungandroidprototype;

import android.content.res.AssetManager;

import java.util.List;

/**
 * Created by lam on 5/1/17.
 */
public class DetailViewAdapter extends RecyclerViewImageAdapter {
    private static final String TAG = DetailViewAdapter.class.getSimpleName();
    private int mCatId;

    public DetailViewAdapter(List<Category> list, AssetManager assetManager, int catId) {
        super(list, assetManager, false, null);
        mCatId = catId;
        mLayout = R.layout.image_detail_item;
    }

    @Override
    public int getItemCount() {
        return (mCategories != null && mCatId > -1 && mCategories.get(mCatId) != null) ? mCategories.get(mCatId).getFileNames().length : -1;
    }
}
