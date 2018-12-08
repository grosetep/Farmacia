package com.estrategiamovilmx.sales.farmacia.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.estrategiamovilmx.sales.farmacia.R;
import com.estrategiamovilmx.sales.farmacia.items.ConfigItem;
import com.estrategiamovilmx.sales.farmacia.responses.ConfigurationResponse;
import com.estrategiamovilmx.sales.farmacia.responses.SubscriptionStatusResponse;
import com.estrategiamovilmx.sales.farmacia.retrofit.RestServiceWrapper;
import com.estrategiamovilmx.sales.farmacia.tools.ApplicationPreferences;
import com.estrategiamovilmx.sales.farmacia.tools.Connectivity;
import com.estrategiamovilmx.sales.farmacia.tools.Constants;
import com.estrategiamovilmx.sales.farmacia.tools.ShowConfirmations;
import com.estrategiamovilmx.sales.farmacia.ui.interfaces.DialogCallbackInterface;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;

public class StartActivity extends AppCompatActivity implements DialogCallbackInterface {
    private static final String TAG = StartActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (Connectivity.isNetworkAvailable(getApplicationContext())) {
            getSubscriptionStatus();
        }else {
            new ShowConfirmations(StartActivity.this, this).openRetry();
        }
    }
    private void getConfiguration(){
        RestServiceWrapper.getConfiguration(new Callback<ConfigurationResponse>() {
            @Override
            public void onResponse(Call<ConfigurationResponse> call, retrofit2.Response<ConfigurationResponse> response) {

                if (response != null && response.isSuccessful()) {
                    ConfigurationResponse config_response = response.body();

                    if (config_response != null && config_response.getStatus().equals(Constants.success)) {

                        if (config_response.getResult() != null && config_response.getStatus().equals(Constants.success)) {
                            Gson gson = new Gson();
                            ApplicationPreferences.saveLocalPreference(getApplicationContext(), Constants.configuration_object, gson.toJson(config_response.getResult(), ConfigItem.class));
                            init();
                        } else {
                            onError(getString(R.string.error_invalid_login, config_response.getMessage()));
                        }

                    } else if (config_response != null && config_response.getStatus().equals(Constants.no_data)) {
                        String response_error = config_response.getMessage();
                        Log.d(TAG, "Mensage:" + response_error);
                        onError(getString(R.string.error_invalid_login, response_error));
                    } else {
                        String response_error = response.message();
                        Log.d(TAG, "Error:" + response_error);
                        onError(getString(R.string.error_invalid_login, response_error));
                    }


                } else {
                    onError(getString(R.string.error_invalid_login, getString(R.string.error_generic)));
                }
            }

            @Override
            public void onFailure(Call<ConfigurationResponse> call, Throwable t) {
                Log.d(TAG, "ERROR: " + t.getStackTrace().toString() + " --->" + t.getCause() + "  -->" + t.getMessage() + " --->");
                onError(getString(R.string.error_invalid_login, t.getMessage()));
            }
        });
    }
    private void onError(String response_error){
        ShowConfirmations.showConfirmationMessage(getString(R.string.error_invalid_login, response_error), StartActivity.this);
    }
    private void init(){
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            };
            timerThread.start();
    }


    @Override
    public void method_positive() {
        if (Connectivity.isNetworkAvailable(getApplicationContext())){
            init();
        }else {

                new ShowConfirmations(this, this).openRetry();
        }
    }

    @Override
    public void method_negative(Activity activity) {
        activity.finish();
    }
    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int mm = calendar.get(Calendar.MONTH);
        int yy = calendar.get(Calendar.YEAR);
        //String Date= DateFormat.getDateTimeInstance().format(new Date());
        StringBuilder date = new StringBuilder().append(yy).append("-").append(mm + 1).append("-").append(dd);
        return date.toString();
    }
/*Si es renta se valida el estatus de suscripcion
* Sino, solo obtiene la configuracion e inicia normalmente
* */
    private void getSubscriptionStatus() {
        String system_date_local = ApplicationPreferences.getLocalStringPreference(getApplicationContext(), Constants.system_date);
        if (Constants.is_subscriber){
            if (system_date_local.isEmpty() || (!system_date_local.equals(getCurrentDate()))) {//primera vez o fechas diferentes, hacer peticion y guardar date en local preferences
                RestServiceWrapper.getSuscriptionStatus(Constants.merchant_key, new Callback<SubscriptionStatusResponse>() {
                    @Override
                    public void onResponse(Call<SubscriptionStatusResponse> call, retrofit2.Response<SubscriptionStatusResponse> response) {
                        if (response != null && response.isSuccessful()) {
                            SubscriptionStatusResponse subscription_response = response.body();

                            if (subscription_response != null && subscription_response.getStatus().equals(Constants.success)) {
                                ApplicationPreferences.saveLocalPreference(getApplicationContext(), Constants.system_date, getCurrentDate());

                                if (subscription_response.getResult() != null && subscription_response.getStatus().equals(Constants.success)) {
                                    if (subscription_response.getResult().getValid().equals(String.valueOf(true))) {
                                        getConfiguration();
                                    }else{
                                        Intent intent = new Intent(StartActivity.this, SubscriptionActivity.class);
                                        finish();
                                        startActivity(intent);
                                    }
                                } else {
                                    String response_error = subscription_response.getMessage();
                                    onError(getString(R.string.error_invalid_login, response_error));
                                }

                            } else if (subscription_response != null && subscription_response.getStatus().equals(Constants.no_data)) {
                                String response_error = subscription_response.getMessage();
                                onError(getString(R.string.error_invalid_login, response_error));
                            } else {
                                String response_error = response.message();
                                onError(getString(R.string.error_invalid_login, response_error));
                            }


                        } else {
                            Log.d(TAG, "error en la peticion..." + response.errorBody().toString());
                            onError(getString(R.string.error_invalid_login, getString(R.string.error_generic)));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubscriptionStatusResponse> call, Throwable t) {
                        Log.d(TAG, "ERROR: " + t.getStackTrace().toString() + " --->" + t.getCause() + "  -->" + t.getMessage() + " --->");
                        onError(getString(R.string.error_invalid_login, t.getMessage()));
                    }
                });

            }else{getConfiguration();}
        }else{
            getConfiguration();//no se valida suscripcion
        }
    }
}
