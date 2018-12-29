package infos.generationchange.gctv;


import infos.generationchange.gctv.fragments.ALaUne;
import infos.generationchange.gctv.fragments.DirectAndTv;
import infos.generationchange.gctv.fragments.Emissions;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
