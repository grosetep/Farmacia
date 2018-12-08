package com.estrategiamovilmx.sales.farmacia.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estrategiamovilmx.sales.farmacia.R;
import com.estrategiamovilmx.sales.farmacia.items.ConfigItem;
import com.estrategiamovilmx.sales.farmacia.items.UserItem;
import com.estrategiamovilmx.sales.farmacia.tools.ApplicationPreferences;
import com.estrategiamovilmx.sales.farmacia.tools.Constants;
import com.estrategiamovilmx.sales.farmacia.tools.FireBaseOperations;
import com.estrategiamovilmx.sales.farmacia.tools.GeneralFunctions;
import com.estrategiamovilmx.sales.farmacia.tools.UtilPermissions;
import com.estrategiamovilmx.sales.farmacia.ui.fragments.OfferFragment;
import com.estrategiamovilmx.sales.farmacia.ui.fragments.PhotoListFragment;
import com.estrategiamovilmx.sales.farmacia.ui.fragments.ProductsFragment;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View mCustomView;
    public Toolbar toolbar;
    public static final String flow_main = "flow_main";
    public static final String flow_no_registered = "flow_no_registered";
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        //custom toolbar
        toolbar.setContentInsetsAbsolute(0,8);


        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(
                getSupportActionBar().DISPLAY_SHOW_CUSTOM,
                getSupportActionBar().DISPLAY_SHOW_CUSTOM |  getSupportActionBar().DISPLAY_SHOW_HOME
                        |  getSupportActionBar().DISPLAY_SHOW_TITLE);

        LayoutInflater mInflater = LayoutInflater.from(this);
        mCustomView = mInflater.inflate(R.layout.actionbar_custom_view_search, null);
        mCustomView.findViewById(R.id.layout_search).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                        Toast.makeText(v.getContext(),"Buscar...",Toast.LENGTH_LONG);
                    }
                });

        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigItem config = GeneralFunctions.getConfiguration(getApplicationContext());
                if (config!=null)
                    makeCall(config.getPhone());
                else
                    makeCall(getString(R.string.shipping_phone));
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initGUI();
        loadProfile();
        subscribeUser();
    }
    private void subscribeUser(){
        UserItem user = GeneralFunctions.getCurrentUser(getApplicationContext());
        if (user!=null){//usuario logueado, suscribir al topic de su perfil
            FireBaseOperations.subscribe(getApplicationContext(),user.getProfile());
        }else{//no logueado, suscribir a client y quitar de los otros
            FireBaseOperations.subscribe(getApplicationContext(),Constants.profile_client);
        }
    }
    private void makeCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS = {Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE};

            if (!UtilPermissions.hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, UtilPermissions.PERMISSION_ALL);
            }
        }
        startActivity(intent);
    }
    private void loadProfile(){
        Gson gson = new Gson();
        String json_user = ApplicationPreferences.getLocalStringPreference(MainActivity.this, Constants.user_object);
        Log.d(TAG,"loadProfile--->");
        if (json_user!=null){
            //get user saved
            UserItem user = gson.fromJson(json_user,UserItem.class);


            if (user!=null && user.getIdUser()!=null) {
                //usuario ya logueado, recuperar info
                    Log.d(TAG, "Carga datos login:::::::::::::::::::::::::::::" + user.toString());
                    updateNavigationView(user);
            }else{//cierre de session, no existe idUser
                Log.d(TAG,"Usuario no logueado valor ID local: ");
                updateNavigationView(null);
            }
        }
    }
    private void updateNavigationView(UserItem user) {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        if (hView != null) {
            final TextView text_name_user = (TextView) hView.findViewById(R.id.text_name_user);
            TextView text_email_user = (TextView) hView.findViewById(R.id.text_email_user);
            ImageView image_profile = (ImageView) hView.findViewById(R.id.image_profile);
            Log.d(TAG,"DAtos del usuario a mostrar-------------------------------------------------------------------------------------------------------");

            final String user_name = ((user!=null && !user.getName().isEmpty())?user.getName():getResources().getString(R.string.text_user_guess));
            text_email_user.setText((user!=null)?user.getEmail():"");
            if (user!=null) {
                Log.d(TAG,"name:"+user.getName() + " user_name:"+user_name);
                final boolean show_tip = (user!=null && (user.getName()==null || user.getName().isEmpty()));
                final String profile = user.getProfile().equals(Constants.profile_client)?"":" - ".concat(user.getProfile());
                        runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_name_user.setText((show_tip)?getResources().getString(R.string.text_user_guess_tip):user_name.concat(profile));
                        }
                });
                loadProfileImage(user, image_profile);
            }else{

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_name_user.setText(getResources().getString(R.string.text_user_guess));
                    }
                });
            }

        }
    }
    private void loadProfileImage(UserItem user,ImageView image_profile){
        Log.d(TAG,"loadProfileImage....................."+user.getAvatarPath()+user.getAvatarImage());
        if (user!=null && user.getAvatarPath()!=null && user.getAvatarImage()!=null){//existe ya un usuario logueado
                Glide.with(image_profile.getContext())
                        .load(user.getAvatarPath()+user.getAvatarImage())
                        .into(image_profile);

        }else{//usuario invitado
            Glide.with(image_profile.getContext())
                    .load(R.drawable.ic_account_circle)
                    .into(image_profile);
        }
    }
    private void initGUI(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
    }
    private  HashMap<String, String>  getParametersProductsSection(){
        HashMap<String, String> params = new HashMap<>();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        params.put("idSubCategory", "");
        return params;
    }
    private void setupViewPager(ViewPager viewPager) {
        Adapter  adapter = new Adapter (getSupportFragmentManager());

       // adapter.addFragment(MenuFragment.createInstance("init"),"MENU");
        adapter.addFragment(OfferFragment.createInstance(getParametersProductsSection()),"OFERTAS DEL DÍA");
        adapter.addFragment(ProductsFragment.createInstance(getParametersProductsSection()),"PRODUCTOS");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);//fragments in memory
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            UserItem user = GeneralFunctions.getCurrentUser(getApplicationContext());
            if (user!=null) {
                Intent i = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                startActivity(i);
            }else{
                Intent i = new Intent(getBaseContext(),LoginActivity.class);
                i.putExtra(Constants.flow,flow_no_registered);
                startActivity(i);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            redirectToLogin();
        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra(Constants.flow, flow_main);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.nav_orders) {


            UserItem user = GeneralFunctions.getCurrentUser(getApplicationContext());

            if (user != null && user.getIdUser() != null) {
                Intent i;
                if (user.getProfile().equals(Constants.profile_deliver_man)) {
                    i = new Intent(this, OrdersDeliverActivity.class);
                } else {
                    i = new Intent(this, OrdersActivity.class);
                }
                i.putExtra(Constants.flow, flow_main);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
         else {

            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(Constants.flow, MainActivity.flow_no_registered);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            }
        }
        else if (id == R.id.nav_location) {
            Intent i = new Intent(this,LocationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }else if(id == R.id.nav_exit){
            Intent i = new Intent(this,SignOutActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void redirectToLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra(Constants.flow,flow_main);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    static class Adapter extends FragmentStatePagerAdapter {
        //FragmentPagerAdapter : Mantiene datos en memoria, destruye fragment cuando no son visibles.
        //FragmentStatePagerAdapter: El fragment se destruye uy solo se guarda su estado, es como listview pero con fragments
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position); //Solo texto en tabs
        }
    }
}
