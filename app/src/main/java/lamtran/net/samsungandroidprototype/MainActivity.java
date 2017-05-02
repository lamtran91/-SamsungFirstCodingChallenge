package lamtran.net.samsungandroidprototype;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final Rect mStartBounds = new Rect();
    private RecyclerView mRV;
    private AssetManager mAssetManager;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private ImageView mExpandedImageView;
    private float mStartScaleFinal;
    private GestureDetector mDetector;
    private int mId;
    private List<Category> mCategoryList;
    private int mCurrentCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAssetManager = getAssets();
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

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
                    }

                    @Override
                    public void onClick(int id) {
                        try {
                            mId = 0;
                            if (mCategoryList == null || mCategoryList.get(id) == null || mId >= mCategoryList.get(id).getFileNames().length) {
                                return;
                            }

                            mCurrentCat = id;
                            zoomImageFromThumb(Utils.buildFilePath(mCategoryList.get(id).getCategory(), mCategoryList.get(id).getFileNames()[mId]));
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

    private void zoomImageFromThumb(String filePath) throws IOException {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        mExpandedImageView.setImageBitmap(BitmapFactory.decodeStream(mAssetManager.open(filePath)));


        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        mStartBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) mStartBounds.width() / mStartBounds.height()) {
            startScale = (float) mStartBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - mStartBounds.width()) / 2;
            mStartBounds.left -= deltaWidth;
            mStartBounds.right += deltaWidth;
        } else {
            startScale = (float) mStartBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - mStartBounds.height()) / 2;
            mStartBounds.top -= deltaHeight;
            mStartBounds.bottom += deltaHeight;
        }

        mExpandedImageView.setVisibility(View.VISIBLE);

        mExpandedImageView.setPivotX(0f);
        mExpandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(mExpandedImageView, View.X,
                        mStartBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(mExpandedImageView, View.Y,
                        mStartBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(mExpandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(mExpandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
        mStartScaleFinal = startScale;
    }

    private void closeZoomedImage() {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator
                .ofFloat(mExpandedImageView, View.X, mStartBounds.left))
                .with(ObjectAnimator
                        .ofFloat(mExpandedImageView,
                                View.Y, mStartBounds.top))
                .with(ObjectAnimator
                        .ofFloat(mExpandedImageView,
                                View.SCALE_X, mStartScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(mExpandedImageView,
                                View.SCALE_Y, mStartScaleFinal));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExpandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mExpandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
    }

    @Override
    public void onBackPressed() {
        if (mExpandedImageView.getVisibility() == View.VISIBLE) {
            closeZoomedImage();
        } else

            super.onBackPressed();
    }

    class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() < e1.getX()) {
                // swipe right

                if (mCategoryList == null || mCategoryList.get(mCurrentCat) == null || mId >= mCategoryList.get(mCurrentCat).getFileNames().length - 1) {
                    return false;
                }

                mId++;
            } else if (e2.getX() > e1.getX()) {
                // swipe left
                if (mCategoryList == null || mCategoryList.get(mCurrentCat) == null || mId <= 0) {
                    return false;
                }

                mId--;
            }

            try {
                zoomImageFromThumb(Utils.buildFilePath(mCategoryList.get(mCurrentCat).getCategory(), mCategoryList.get(mCurrentCat).getFileNames()[mId]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
