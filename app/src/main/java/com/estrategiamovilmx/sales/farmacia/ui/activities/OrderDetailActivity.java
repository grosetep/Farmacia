package com.estrategiamovilmx.sales.farmacia.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estrategiamovilmx.sales.farmacia.R;
import com.estrategiamovilmx.sales.farmacia.items.OrderDetail;
import com.estrategiamovilmx.sales.farmacia.items.OrderItem;
import com.estrategiamovilmx.sales.farmacia.items.UserItem;
import com.estrategiamovilmx.sales.farmacia.responses.GetOrdersResponse;
import com.estrategiamovilmx.sales.farmacia.responses.OrderDetailResponse;
import com.estrategiamovilmx.sales.farmacia.retrofit.RestServiceWrapper;
import com.estrategiamovilmx.sales.farmacia.tools.Connectivity;
import com.estrategiamovilmx.sales.farmacia.tools.Constants;
import com.estrategiamovilmx.sales.farmacia.tools.GeneralFunctions;
import com.estrategiamovilmx.sales.farmacia.tools.ShowConfirmations;
import com.estrategiamovilmx.sales.farmacia.tools.StringOperations;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderDetailActivity extends AppCompatActivity {
    private static final String TAG = OrderDetailActivity.class.getSimpleName();
    public static String ID_ORDER = "order";
    private TextView text_order;
    private TextView text_hour;
    private TextView text_date;
    private ImageView image_status;
    private TextView text_status;
    private TextView text_name_view;
    private TextView text_email_view;
    private TextView text_name_contact;
    private TextView text_phone_contact;
    private TextView text_address;
    private LinearLayout layout_address;
    private TextView text_products;
    private TextView text_total;
    private FrameLayout container_loading;
    private LinearLayout layout_principal;
    private RelativeLayout no_connection_layout;
    private OrderItem order;
    private OrderDetail detail;
    private UserItem user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //show detail and drive for delivers only
        Intent i = getIntent();
        order = (OrderItem) i.getSerializableExtra(OrderDetailActivity.ID_ORDER);
        user = GeneralFunctions.getCurrentUser(getApplicationContext());
        init();
        assignActions();
        if (Connectivity.isNetworkAvailable(getApplicationContext())) {

            if (user!=null) {Log.d(TAG,"Usuario activo:" + user.toString());
                initProcess(true);
                showInitValues();
                getDetailOrder();
            }
        }else{
            showNoConnectionLayout();
        }
    }
    private void showInitValues(){
        //assign values
        text_order.setText(order.getIdOrder());
        switch (order.getStatus()){
            case Constants.status_review:
                image_status.setImageResource(R.drawable.ic_description);
                text_status.setText(Constants.status_review.substring(0,1).toUpperCase() + Constants.status_review.substring(1));
                break;
            case Constants.status_rejected://rechazado, cambia textoy color icono
                image_status.setImageResource(R.drawable.ic_assignment_turned_in);
                image_status.setColorFilter(ContextCompat.getColor(this,android.R.color.holo_red_light));
                text_status.setText(Constants.status_rejected.substring(0,1).toUpperCase() + Constants.status_rejected.substring(1));
                break;
            case Constants.status_accepted:
                image_status.setImageResource(R.drawable.ic_assignment_turned_in);
                text_status.setText(Constants.status_accepted.substring(0,1).toUpperCase() + Constants.status_accepted.substring(1));
                break;
            case Constants.status_cancel:
                image_status.setImageResource(R.drawable.ic_assignment_turned_in);
                image_status.setColorFilter(ContextCompat.getColor(this,android.R.color.holo_red_light));
                text_status.setText(Constants.status_cancel.substring(0,1).toUpperCase() + Constants.status_cancel.substring(1));
                break;
            case Constants.status_on_way:
                image_status.setImageResource(R.drawable.ic_motorcycle);
                text_status.setText(Constants.status_on_way.substring(0,1).toUpperCase() + Constants.status_on_way.substring(1));
                break;

            case Constants.status_deliver:
                image_status.setImageResource(R.drawable.ic_check_circle);
                text_status.setText(Constants.status_deliver.substring(0,1).toUpperCase() + Constants.status_deliver.substring(1));
                break;
            case Constants.status_no_deliver:
                image_status.setImageResource(R.drawable.ic_check_circle);
                image_status.setColorFilter(ContextCompat.getColor(this,android.R.color.holo_red_light));
                text_status.setText(Constants.status_no_deliver.substring(0,1).toUpperCase() + Constants.status_no_deliver.substring(1));
                break;
        }
    }
    private void assignActions(){

        if (user!=null && user.getProfile().equals(Constants.profile_deliver_man) ){
            layout_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getDetail()!=null){
                        String geolocation = "geo:".concat(getDetail().getLatitude()).concat(",").concat(getDetail().getLongitude());
                        Uri gmmIntentUri = Uri.parse(geolocation+"?q=" + Uri.encode(getDetail().getGooglePlace()));
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        //mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }else{
                            ShowConfirmations.showConfirmationMessage(getString(R.string.generic_error),OrderDetailActivity.this);}
                    }
                }
            });
        }

        if (order!=null){
            text_order.setText(order.getIdOrder());

        }
    }
    private void getDetailOrder(){
        RestServiceWrapper.getDetailOrder(order.getIdOrder() ,new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, retrofit2.Response<OrderDetailResponse> response) {
                Log.d(TAG, "Respuesta: " + response);
                if (response != null && response.isSuccessful()) {
                    if (response.body().getStatus().equals(String.valueOf(Constants.uno))) {
                        detail = response.body().getResult();
                        setDetailedValues(detail);
                        initProcess(false);
                    }else{
                        onError(response.body().getMessage());
                    }
                }else{
                    onError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                Log.d(TAG, "ERROR: " + t.getStackTrace().toString() + " --->" + t.getCause() + "  -->" + t.getMessage() + " --->");
                onError( t.getMessage());
            }
        });
    }
    private void setDetailedValues(OrderDetail detail){
        StringBuffer buffer = new StringBuffer();
        if (detail!=null){
            text_name_view.setText(detail.getNameClient());
            text_email_view.setText(detail.getEmailClient());
            text_name_contact.setText(detail.getContactName());
            text_phone_contact.setText(detail.getContactPhone());
            text_address.setText(detail.getGooglePlace());
            text_total.setText(StringOperations.getAmountFormat(detail.getTotal()));
            text_hour.setText(detail.getHourCreation());
            text_date.setText(detail.getDateCreation());
            if (detail.getProducts().length()>0){
                String[] list_products = detail.getProducts().split(",");
                for (int i = 0;i<list_products.length;i++) {
                    buffer.append(list_products[i]+"\n");
                }
                text_products.setText(buffer.toString());
            }
            //agrega datos de contacto a la entrega


        }else{
            onError(getString(R.string.generic_error));
        }

    }
    private void onError(String msgErr){
        ShowConfirmations.showConfirmationMessage(msgErr,this);
        initProcess(false);
        onBackPressed();
    }
    private void initProcess(boolean flag){
        container_loading.setVisibility(flag?View.VISIBLE:View.GONE);
        layout_principal.setVisibility(flag?View.GONE:View.VISIBLE);
        no_connection_layout.setVisibility(View.GONE);
    }
    private void showNoConnectionLayout(){
        container_loading.setVisibility(View.GONE);
        layout_principal.setVisibility(View.GONE);
        no_connection_layout.setVisibility(View.VISIBLE);
    }
    private void init(){
        layout_principal = (LinearLayout) findViewById(R.id.layout_principal);
        container_loading = (FrameLayout) findViewById(R.id.container_loading);
        no_connection_layout = (RelativeLayout) findViewById(R.id.no_connection_layout);
        text_order = (TextView) findViewById(R.id.text_order);
        text_hour = (TextView) findViewById(R.id.text_hour);
        text_date = (TextView) findViewById(R.id.text_date);
        image_status = (ImageView) findViewById(R.id.image_status);
        text_status = (TextView) findViewById(R.id.text_status);
        text_name_view = (TextView) findViewById(R.id.text_name_view);
        text_email_view = (TextView) findViewById(R.id.text_email_view);
        text_name_contact = (TextView) findViewById(R.id.text_name_contact);
        text_phone_contact = (TextView) findViewById(R.id.text_phone_contact);

        text_address = (TextView) findViewById(R.id.text_address);
        layout_address = (LinearLayout) findViewById(R.id.layout_address);
        text_products = (TextView) findViewById(R.id.text_products);
        text_total = (TextView) findViewById(R.id.text_total);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent( getApplicationContext() , OrdersActivity.class);
        i.putExtra(Constants.flow,CongratsActivity.flow_congrats);
        startActivity(i);
        finish();
        //NavUtils.navigateUpTo(OrderDetailActivity.this, new Intent(getApplicationContext(), OrdersActivity.class));
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

    public OrderDetail getDetail() {
        return detail;
    }
}
