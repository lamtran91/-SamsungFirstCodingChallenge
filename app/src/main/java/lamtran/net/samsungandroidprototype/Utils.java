package lamtran.net.samsungandroidprototype;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lam on 5/1/17.
 */

public class Utils {
    public static final String ROOT_ASSET = "app_images";
    private static final String TAG = Utils.class.getSimpleName();
    private static List<Category> CATEGORIES;

    public static List<Category> buildCategories(AssetManager assetManager) {
        List<Category> categories;
        if (CATEGORIES == null) {

            if (assetManager == null) return null;
            categories = new ArrayList<>();

            // get all the categories folder at the root asset layer
            try {
                String[] cats = assetManager.list(ROOT_ASSET);
                for (String cat : cats) {
                    String[] files = assetManager.list(ROOT_ASSET + "/" + cat);
                    Category category = new Category(cat, files);
                    categories.add(category);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Fail to retrieve files from assets", e);
            }

            CATEGORIES = categories;
        }

        return CATEGORIES;
    }


    public static String buildFilePath(String cat, String filename) {
        return Utils.ROOT_ASSET + "/" + cat + "/" + filename;
    }
}
