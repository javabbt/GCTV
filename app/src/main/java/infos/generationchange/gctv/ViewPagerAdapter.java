package infos.generationchange.gctv;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import infos.generationchange.gctv.fragments.ALaUne;
import infos.generationchange.gctv.fragments.DirectAndTv;
import infos.generationchange.gctv.fragments.Emissions;



public class ViewPagerAdapter extends FragmentPagerAdapter
{
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new DirectAndTv();
        else if (position == 1)
            fragment = new ALaUne();
        else if (position == 2)
            fragment = new Emissions();
        return fragment;
    }
    @Override
    public int getCount() {
        return 3;
    }
}
