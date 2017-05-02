package lamtran.net.samsungandroidprototype;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRV;
    private AssetManager mAssetManager;
    private ImageView mExpandedImageView;
    private GestureDetector mDetector;
    private int mId;
    private List<Category> mCategoryList;
    private int mCurrentCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAssetManager = getAssets();
        mExpandedImageView = (ImageView) findViewById(R.id.zoomed_image);
        mRV = (RecyclerView) findViewById(R.id.recycler_view);
        mRV.setHasFixedSize(true);
        mRV.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mCategoryList = Utils.buildCategories(mAssetManager);

        mRV.setAdapter(new SummaryViewAdapter(mCategoryList, mAssetManager,
                new RecyclerViewImageAdapter.OnCategoryClickListener() {
                    @Override
                    public void onLongClick(int catId) {
                        Intent detailActivity = new Intent(MainActivity.this, DetailActivity.class);
                        detailActivity.putExtra(DetailActivity.BUNDLE_EXTRA, catId);
                        startActivity(detailActivity);
                        overridePendingTransition(R.anim.slide_up, R.anim.hold);
                    }

                    @Override
                    public void onClick(int id) {
                        try {
                            mId = 0;
                            if (mCategoryList == null || mCategoryList.get(id) == null || mId >= mCategoryList.get(id).getFileNames().length) {
                                return;
                            }

                            mCurrentCat = id;
                            populateImage(Utils.buildFilePath(mCategoryList.get(id).getCategory(), mCategoryList.get(id).getFileNames()[mId]));
                            Animation ani = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in);
                            Animation ani2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
                            mExpandedImageView.startAnimation(ani2);
                            mExpandedImageView.startAnimation(ani);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));

        mDetector = new GestureDetector(this, new SwipeDetector());
        mExpandedImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void populateImage(String filePath) throws IOException {
        mExpandedImageView.setVisibility(View.VISIBLE);
        mExpandedImageView.setImageBitmap(BitmapFactory.decodeStream(mAssetManager.open(filePath)));
        mExpandedImageView.setBackgroundResource(R.drawable.image_view_border);
    }

    @Override
    public void onBackPressed() {
        if (mExpandedImageView.getDrawable() != null) {
            mExpandedImageView.setImageDrawable(null);
            mExpandedImageView.setBackground(null);
            mExpandedImageView.setVisibility(View.INVISIBLE);

        } else
            super.onBackPressed();
    }

    private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() < e1.getX()) {
                // swipe right
                if (mCategoryList == null || mCategoryList.get(mCurrentCat) == null || mId >= mCategoryList.get(mCurrentCat).getFileNames().length - 1) {
                    return false;
                }

                mId++;
                Animation ani = AnimationUtils.loadAnimation(getBaseContext(), R.anim.right_to_left);
                mExpandedImageView.startAnimation(ani);
            } else if (e2.getX() > e1.getX()) {
                // swipe left
                if (mCategoryList == null || mCategoryList.get(mCurrentCat) == null || mId <= 0) {
                    return false;
                }

                mId--;
                Animation ani = AnimationUtils.loadAnimation(getBaseContext(), R.anim.left_to_right);
                mExpandedImageView.startAnimation(ani);

            }

            try {
                populateImage(Utils.buildFilePath(mCategoryList.get(mCurrentCat).getCategory(), mCategoryList.get(mCurrentCat).getFileNames()[mId]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
