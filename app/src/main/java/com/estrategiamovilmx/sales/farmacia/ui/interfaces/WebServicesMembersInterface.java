package com.estrategiamovilmx.sales.farmacia.ui.interfaces;

import com.estrategiamovilmx.sales.farmacia.responses.SubscriptionStatusResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebServicesMembersInterface   {

   /*Members method*/
   @GET("getSuscriptionStatus/{merchant_key}")
   Call<SubscriptionStatusResponse> getSuscriptionStatus(
           @Path("merchant_key") String merchant_key);
}