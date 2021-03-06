package com.estrategiamovilmx.sales.farmacia.ui.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.estrategiamovilmx.sales.farmacia.R;
import com.estrategiamovilmx.sales.farmacia.items.UserItem;
import com.estrategiamovilmx.sales.farmacia.model.ApiException;
import com.estrategiamovilmx.sales.farmacia.model.ProductModel;
import com.estrategiamovilmx.sales.farmacia.model.PublicationCardViewModel;
import com.estrategiamovilmx.sales.farmacia.requests.CartRequest;
import com.estrategiamovilmx.sales.farmacia.responses.GenericResponse;
import com.estrategiamovilmx.sales.farmacia.retrofit.RestServiceWrapper;
import com.estrategiamovilmx.sales.farmacia.tools.Constants;
import com.estrategiamovilmx.sales.farmacia.tools.GeneralFunctions;
import com.estrategiamovilmx.sales.farmacia.tools.ShowConfirmations;
import com.estrategiamovilmx.sales.farmacia.ui.fragments.DetailProductsFragment;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailPublicationActivity extends AppCompatActivity {
    public static String EXTRA_PRODUCT = "id_product";
    public static String EXTRA_IMAGEPATH="imagePath";
    public static String EXTRA_IMAGENAME="imageName";
    private FloatingActionButton button_add_to_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_publication);

        Intent intent = getIntent();
        final String idProduct = intent.getStringExtra(EXTRA_PRODUCT);
        final String imagePath = intent.getStringExtra(EXTRA_IMAGEPATH);
        final String imageName = intent.getStringExtra(EXTRA_IMAGENAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_add_to_cart = (FloatingActionButton) findViewById(R.id.button_add_to_cart);
        button_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserItem user = GeneralFunctions.getCurrentUser(getApplicationContext());
                if (user!=null) {
                    addToCart();
                }else{
                    finish();
                    Intent i = new Intent(getBaseContext(),LoginActivity.class);
                    i.putExtra(Constants.flow,MainActivity.flow_no_registered);
                    startActivity(i);
                }
            }
        });

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");

        ProductModel product = new ProductModel();
        product.setIdProduct(idProduct);
        product.setPath(imagePath);
        product.setImage(imageName);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerDetailPublication, DetailProductsFragment.createInstance(product), "DetailProductsFragment")
                    .commit();

        }

    }
    private void addToCart(){
        DetailProductsFragment fragment = (DetailProductsFragment)getSupportFragmentManager().findFragmentByTag("DetailProductsFragment");
        if (fragment!=null) fragment.addToCart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DetailProductsFragment fragment = (DetailProductsFragment) getSupportFragmentManager().
                findFragmentByTag("DetailProductsFragment");
        fragment.loadInformation();

    }
}
