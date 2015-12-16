package com.example.rank.component;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 06/07/2015
 * Time: 09:54
 */
public class Abas {
    private List<Fragment> fragments;
    private List<String> titulos;
    private ViewPager viewPager;

    public Abas() {
        this.fragments = new LinkedList<>();
        this.titulos = new LinkedList<>();
    }

    public Abas addTitulo(String... titulos) {
        Collections.addAll(this.titulos, titulos);
        return this;
    }

    public Abas addFragmento(Fragment... fragments) {
        Collections.addAll(this.fragments, fragments);
        return this;
    }

    public void construir(FragmentManager fragmentManager, ViewPager viewPager, ActionBar actionBar) {
        this.viewPager = viewPager;
        ViewPagerListener pagerListener = new ViewPagerListener(viewPager, actionBar);
        viewPager.setOnPageChangeListener(pagerListener);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String titulo : titulos) {
            actionBar.addTab(actionBar.newTab().setText(titulo).setTabListener(pagerListener));
        }

        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    public void setCurrentItemViewPager(int position) {
        viewPager.setCurrentItem(position);
    }

    private class ViewPagerListener extends ViewPager.SimpleOnPageChangeListener implements ActionBar.TabListener {
        private ViewPager viewPager;
        private ActionBar actionBar;

        public ViewPagerListener(ViewPager viewPager, ActionBar actionBar) {
            this.viewPager = viewPager;
            this.actionBar = actionBar;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onPageSelected(int position) {
            actionBar.setSelectedNavigationItem(position);
        }
    }
}
