package tv.teads.teadssdkdemo.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private static String[] sTitle;

    private List<Fragment> mFragments;
    private SparseArray<Fragment> mRegisteredFragments = new SparseArray<Fragment>();

    public TabFragmentPagerAdapter(String[] pageTitles, FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        sTitle = pageTitles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        try {
            return sTitle[position];
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            return "null";
        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return mRegisteredFragments.get(position);
    }

}
