package com.yvrun.officeprocess.core.primary.knowledge;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * FragmentPagerAdapter 适用于子Fragment数量较少的情况，destroyItem会销毁fragment实例
 * FragmentStatePagerAdapter 适用于子Fragment数量较多的情况，destroyItem时只会detach并不会将fragment销毁
 */
public class KnowledgeTopPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] mFragments;
    private String[] mTitles;

    public KnowledgeTopPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public KnowledgeTopPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setFragmentList(Fragment... fragments){

        mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.length;
    }
}
