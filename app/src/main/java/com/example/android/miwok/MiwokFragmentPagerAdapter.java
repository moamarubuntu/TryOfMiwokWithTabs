package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MiwokFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MiwokFragmentPagerAdapter(FragmentManager fragmentManager, Context context)
    {
        super(fragmentManager);
        this.mContext = context;
    }
    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return new NumbersFragment();
            case 1:
                return new FamilyFragment();
            case 2:
                return new ColoursFragment();
            case 3:
                return new PhrasesFragment();
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 4;
    }

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //
        switch (position){
            case 0:
                return this.mContext.getString(R.string.category_numbers);
            case 1:
                return this.mContext.getString(R.string.category_family);
            case 2:
                return this.mContext.getString(R.string.category_colours);
            case 3:
                return this.mContext.getString(R.string.category_phrases);
            default:
                return "";
        }
    }
}
