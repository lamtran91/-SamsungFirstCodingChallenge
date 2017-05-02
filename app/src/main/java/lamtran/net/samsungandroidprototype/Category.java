package lamtran.net.samsungandroidprototype;

/**
 * Created by lam on 5/1/17.
 */

public class Category {
    private String mCategory;
    private String[] mFileNames;

    public Category(String category, String[] fileNames) {
        mCategory = category;
        mFileNames = fileNames;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String[] getFileNames() {
        return mFileNames;
    }

    public void setFileNames(String[] fileNames) {
        mFileNames = fileNames;
    }
}
