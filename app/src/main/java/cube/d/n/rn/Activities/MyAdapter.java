package cube.d.n.rn.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class MyAdapter  extends FragmentPagerAdapter {
    public ArrayList<Fragment> frags = new ArrayList<>();


    public MyAdapter(FragmentManager fm) {
        super(fm);
        frags.add(new CubeFrag());
        frags.add(new HelpFrag());
        frags.add(new MenuFrag());
    }

    @Override
    public Fragment getItem(int i) {
//        fragmentReferences.put(i, new WeakReference<Fragment>(frags.get(i)));
        return frags.get(i);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object rez = super.instantiateItem(container, position);
        //if (!object.equals(map.get(map.get(position)))) {
        map.put(position, (Fragment) rez);
        return rez;
    }

    private HashMap<Integer, Fragment> map = new HashMap<>();

    public Fragment getFragment(int fragmentId) {
//        WeakReference<Fragment> ref = fragmentReferences.get(fragmentId);
//        return ref == null ? null : ref.get();
        return map.get(fragmentId);
    }

    @Override
    public int getCount() {
        return frags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "FRAG " + (position + 1);
    }

}
