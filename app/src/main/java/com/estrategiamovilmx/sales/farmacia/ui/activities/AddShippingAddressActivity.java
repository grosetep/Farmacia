package com.estrategiamovilmx.sales.farmacia.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.estrategiamovilmx.sales.farmacia.R;
import com.estrategiamovilmx.sales.farmacia.model.ShippingAddress;
import com.estrategiamovilmx.sales.farmacia.ui.fragments.NumIntFragment;
import com.estrategiamovilmx.sales.farmacia.ui.fragments.ReferenceFragment;
import com.estrategiamovilmx.sales.farmacia.ui.fragments.ScreenSlidePageFragment;
import com.estrategiamovilmx.sales.farmacia.ui.fragments.SelectShippingPointFragment;

import java.util.ArrayList;
import java.util.List;

public class AddShippingAddressActivity extends AppCompatActivity {
    private static final String TAG = AddShippingAddressActivity.class.getSimpleName();
    public static final String NEW_SHIPPING_ADDRESS = "new_address";
    public static final String ARG_PAGE = "page";
    public static final String TYPE_REQUEST_CP = "cp";
    public static final String TYPE_REQUEST_OTHER = "other";
    public static final String TYPE_REQUEST = "type_request";
    private static final int PAGE_CP = 0;


    private static final int PAGE_NUM_INT = 1;
    private static final int PAGE_REFERENCE = 2;


    private ViewPager mPager;
    private float MIN_SCALE = 1f - 1f / 7f;
    private float MAX_SCALE = 1f;
    private AppCompatButton button_next;
    private AppCompatButton button_previous;
    private RelativeLayout layout_loading;
    public static ShippingAddress shipping_point = new ShippingAddress();
    private ScreenSlidePagerAdapter mPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipping_address);

        final Toolbar toolbar_shipping = (Toolbar) findViewById(R.id.toolbar_add_shipping);
        setSupportActionBar(toolbar_shipping);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate a ViewPager and a PagerAdapter.
        shipping_point.setNew(true);
        shipping_point.setSelected(true);
        shipping_point.setId_location("0");
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(SelectShippingPointFragment.create(PAGE_CP));
        mPagerAdapter.addFragment(NumIntFragment.create(PAGE_NUM_INT));
        mPagerAdapter.addFragment(ReferenceFragment.create(PAGE_REFERENCE));
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                button_previous.setEnabled(mPager.getCurrentItem() > 0?true:false);
                button_next.setText((mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)?R.string.text_button_done:R.string.text_button_next);
            }
        });
        button_previous = (AppCompatButton) findViewById(R.id.button_previous);
        button_next = (AppCompatButton) findViewById(R.id.button_next);
        layout_loading = (RelativeLayout) findViewById(R.id.layout_loading);
        assignActions();
    }
    private void nextPage(){
        if (mPager.getCurrentItem()<mPagerAdapter.getCount()-1) {
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }
    private boolean validate(){
        boolean valid = true;
        /*******************************************************************Ultimo paso***************************************************************/
        if (mPager.getCurrentItem()==mPagerAdapter.getCount()-1) {
            //set value referece
            ScreenSlidePagerAdapter dapter_temp= (ScreenSlidePagerAdapter)mPager.getAdapter();
            ReferenceFragment fragment_reference = (ReferenceFragment)dapter_temp.getFragment(mPager.getCurrentItem());
            if (fragment_reference.getData().isEmpty()){
                fragment_reference.setError(getString(R.string.error_field_required));
                valid = false;
            }else{
                shipping_point.setReference(fragment_reference.getData());
            }


            //validations


            if (shipping_point.getGooglePlace()!=null && !shipping_point.getGooglePlace().isEmpty()){
                if(shipping_point.getReference()!=null && !shipping_point.getReference().isEmpty()){
                    Log.d(TAG,"Exito en validaciones, regresar a principal y crear nueva direcion en lista....");
                    Intent intent = new Intent();
                    Bundle args = new Bundle();
                    ShippingAddress new_address = getShipping_point();
                    args.putSerializable(NEW_SHIPPING_ADDRESS, new_address);
                    intent.putExtras(args);
                    setResult(Activity.RESULT_OK,intent);
                    shipping_point = new ShippingAddress();
                    finish();
                }else{
                    ScreenSlidePagerAdapter adapter= (ScreenSlidePagerAdapter)mPager.getAdapter();
                    Fragment fragment = (Fragment)adapter.getFragment(2);
                    ((ReferenceFragment) fragment).setError(getString(R.string.error_field_required));
                    mPager.setCurrentItem(2);
                }
            }else{
                 ScreenSlidePagerAdapter adapter= (ScreenSlidePagerAdapter)mPager.getAdapter();
                Fragment fragment = (Fragment)adapter.getFragment(PAGE_CP);
                ((SelectShippingPointFragment) fragment).setError(getString(R.string.error_field_required));
            }
        }else{/************************************************Primer paso***************************************************************/
            if (mPager.getCurrentItem()==0){//esta en el primer paso, postal code, calcula estado y pais...

                ScreenSlidePagerAdapter adapter= (ScreenSlidePagerAdapter)mPager.getAdapter();
                SelectShippingPointFragment fragment = (SelectShippingPointFragment)adapter.getFragment(mPager.getCurrentItem());
                if (fragment.getTextLocation().isEmpty()){
                    fragment.setError(getString(R.string.error_field_required));
                    valid = false;
                }else{
                    shipping_point.setLatitude(fragment.getLatitude());
                    shipping_point.setLongitude(fragment.getLongitude());
                    shipping_point.setGooglePlace(fragment.getTextLocation());
                    nextPage();
                }

            }else{
                /************************************************Pasos intermedios***************************************************************/

                ScreenSlidePagerAdapter adapter= (ScreenSlidePagerAdapter)mPager.getAdapter();
                Fragment fragment = (Fragment)adapter.getFragment(mPager.getCurrentItem());
                    /**************Numero interior ***********/
                 if(fragment instanceof NumIntFragment){
                    if (((NumIntFragment) fragment).getData().isEmpty()){
                        shipping_point.setNum_int("");
                        nextPage();
                    }else {
                        shipping_point.setNum_int(((NumIntFragment) fragment).getData());
                        nextPage();
                    }
                     /**************Referencia ***********/
                }else if(fragment instanceof ReferenceFragment){
                    if (((ReferenceFragment) fragment).getData().isEmpty()){
                        ((ReferenceFragment) fragment).setError(getString(R.string.error_field_required));
                        valid = false;
                    }else {
                        shipping_point.setReference(((ReferenceFragment) fragment).getData());
                        nextPage();
                    }
                }

            }

        }
        return valid;
    }
    private void assignActions(){

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        button_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mPager.getCurrentItem()>0)
                    mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });
    }
    public static ShippingAddress getShipping_point(){return shipping_point;}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, ShippingActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        public Fragment getFragment(int key){
            return mFragments.get(key);
        }
        @Override
        public int getCount() {
            return mFragments.size();
        }

    }


}
