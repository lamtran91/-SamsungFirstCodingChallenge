package lamtran.net.samsungandroidprototype;

import android.content.res.AssetManager;

import java.util.List;

/**
 * Created by lam on 5/1/17.
 */
public class DetailViewAdapter extends RecyclerViewImageAdapter {
    private int mCatId;
    private Category mCategory;

    public DetailViewAdapter(List<Category> list, AssetManager assetManager, int catId) {
        super(list, assetManager, false, null);
        mCatId = catId;
        mLayout = R.layout.image_detail_item;
        if (catId > -1) mCategory = mCategories.get(catId);
    }

    @Override
    public int getItemCount() {
        return (mCategories != null && mCatId > -1 && mCategories.get(mCatId) != null) ? mCategories.get(mCatId).getFileNames().length : -1;
    }

    @Override
    String getFilePath(int index) {
        if (mCategory == null || mCategory.getFileNames() == null) return null;
        return Utils.buildFilePath(mCategory.getCategory(), mCategory.getFileNames()[index]);
    }

    @Override
    String getTitle(int index) {
        if (mCategory == null || mCategory.getFileNames() == null) return null;
        return mCategory.getFileNames()[index];
    }
}
