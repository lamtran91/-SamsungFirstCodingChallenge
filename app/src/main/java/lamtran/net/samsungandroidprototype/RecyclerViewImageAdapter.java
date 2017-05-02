package lamtran.net.samsungandroidprototype;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/**
 * Created by lam on 5/1/17.
 */
public abstract class RecyclerViewImageAdapter extends RecyclerView.Adapter<RecyclerViewImageAdapter.ViewHolder> {
    private static final String TAG = RecyclerViewImageAdapter.class.getSimpleName();
    protected List<Category> mCategories;
    protected AssetManager mAssetManager;
    protected
    @LayoutRes
    int mLayout;
    private OnCategoryClickListener mListener;
    private boolean mIsSummary;

    public RecyclerViewImageAdapter(List<Category> list, AssetManager assetManager, boolean isSummary, OnCategoryClickListener listener) {
        mAssetManager = assetManager;
        mCategories = list;
        mIsSummary = isSummary;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mCategories == null || mAssetManager == null) return;

        //get the position in the adapter instead of layout position
        final int pos = holder.getAdapterPosition();
        holder.mTitleTv.setText(getTitle(pos));
        try {
            final String filePath = getFilePath(pos);
            holder.mImageIv.setImageBitmap(BitmapFactory.decodeStream(mAssetManager.open(filePath)));

            if (mListener != null) {
                holder.mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        mListener.onClick(pos);
                    }
                });
                holder.mRootView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mListener.onLongClick(pos);
                        return true;
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Fail to log asset into image view", e);
        }
    }


    abstract String getFilePath(int index);

    abstract String getTitle(int index);


    interface OnCategoryClickListener {
        void onLongClick(int id);

        void onClick(int id);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageIv;
        public TextView mTitleTv;
        public View mRootView;

        public ViewHolder(View view) {
            super(view);
            mRootView = view;
            mImageIv = (ImageView) view.findViewById(R.id.image_view);
            mTitleTv = (TextView) view.findViewById(R.id.title);
        }
    }
}
