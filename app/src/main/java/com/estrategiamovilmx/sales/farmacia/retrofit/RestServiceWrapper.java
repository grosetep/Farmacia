package com.estrategiamovilmx.sales.farmacia.retrofit;

import com.estrategiamovilmx.sales.farmacia.requests.CartRequest;
import com.estrategiamovilmx.sales.farmacia.requests.ChangeStatusOrderRequest;
import com.estrategiamovilmx.sales.farmacia.requests.CreateOrderRequest;
import com.estrategiamovilmx.sales.farmacia.requests.RegisterDeviceRequest;
import com.estrategiamovilmx.sales.farmacia.requests.UserOperationRequest;
import com.estrategiamovilmx.sales.farmacia.requests.UpdateShoppingCartRequest;
import com.estrategiamovilmx.sales.farmacia.responses.ConfigurationResponse;
import com.estrategiamovilmx.sales.farmacia.responses.CreateOrderResponse;
import com.estrategiamovilmx.sales.farmacia.responses.GenericResponse;
import com.estrategiamovilmx.sales.farmacia.responses.GetCartResponse;
import com.estrategiamovilmx.sales.farmacia.responses.GetContactsResponse;
import com.estrategiamovilmx.sales.farmacia.responses.GetOrdersResponse;
import com.estrategiamovilmx.sales.farmacia.responses.GetPaymentMethodResponse;
import com.estrategiamovilmx.sales.farmacia.responses.GetProductsResponse;
import com.estrategiamovilmx.sales.farmacia.responses.GetShippingAddressResponse;
import com.estrategiamovilmx.sales.farmacia.responses.OrderDetailResponse;
import com.estrategiamovilmx.sales.farmacia.responses.SubscriptionStatusResponse;
import com.estrategiamovilmx.sales.farmacia.responses.UserResponse;
import com.estrategiamovilmx.sales.farmacia.tools.Constants;
import com.estrategiamovilmx.sales.farmacia.ui.interfaces.WebServicesInterface;
import com.estrategiamovilmx.sales.farmacia.ui.interfaces.WebServicesMembersInterface;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by administrator on 21/07/2017.
 */
public class RestServiceWrapper {



    public static void getAllProducts(String start,String end, Callback function){
        //
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GetProductsResponse> call = client.getAllProducts(start, end);
        call.enqueue(function);
    }
    public static void getLocationsByUser(String id_user, Callback function){
        //
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GetShippingAddressResponse> call = client.getLocationsByUser(id_user);
        call.enqueue(function);
    }
    public static void getContactsByUser(String id_user, Callback function){
        //
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GetContactsResponse> call = client.getContactsByUser(id_user);
        call.enqueue(function);
    }
    public static void getPaymentMethods( Callback function){
        //
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GetPaymentMethodResponse> call = client.getPaymentMethods();
        call.enqueue(function);
    }
    public  static void shoppingCart(CartRequest cart_request, Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GenericResponse> call = client.shoppingCart(cart_request);
        call.enqueue(function);
    }

    public  static void getShoppingCart(String id_user, Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GetCartResponse> call = client.getShoppingCart(id_user);
        call.enqueue(function);
    }
    public  static void createOrder(CreateOrderRequest order, Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<CreateOrderResponse> call = client.createOrder(order);
        call.enqueue(function);
    }
    public  static void updateShoppingCart(UpdateShoppingCartRequest update_request, Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GenericResponse> call = client.updateShoppingCart(update_request);
        call.enqueue(function);
    }
    public  static void getOrders(String id_user,String id_profile,int start, int end,String days, Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GetOrdersResponse> call = client.getOrders(id_user, id_profile, start, end, days);
        call.enqueue(function);
    }
    public  static void getDeliverManOrders(String id_user,String id_profile,String type_query ,int start, int end,String days,Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GetOrdersResponse> call = client.getDeliverManOrders(id_user, id_profile, type_query, start, end, days);
        call.enqueue(function);
    }
    public  static void userOperation(UserOperationRequest request, Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<UserResponse> call = client.userOperation(request);
        call.enqueue(function);
    }
    public  static void getConfiguration(Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<ConfigurationResponse> call = client.getConfiguration();
        call.enqueue(function);
    }
    public  static void recoveryPassword(String email,String newPassword,String encryptedPassword,Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GenericResponse> call = client.recoveryPassword(email, newPassword, encryptedPassword);
        call.enqueue(function);
    }
    public  static void ChangeStatusOrder(ChangeStatusOrderRequest request, Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GenericResponse> call = client.ChangeStatusOrder(request);
        call.enqueue(function);
    }
    public  static void registerDevice(RegisterDeviceRequest request, Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<GenericResponse> call = client.registerDevice(request);
        call.enqueue(function);
    }
    public  static void getDetailOrder(String id_user, Callback function){
        WebServicesInterface client = RetrofitClient.getClient(Constants.RETROFIT_SERVICE_REST).create(WebServicesInterface.class);
        Call<OrderDetailResponse> call = client.getDetailOrder(id_user);
        call.enqueue(function);
    }
    /*Members rest service*/
    public  static void getSuscriptionStatus(String merchant_key,Callback function){
        WebServicesMembersInterface client = RetrofitClientMembers.getClient(Constants.MENBERS_DOMAIN_REST).create(WebServicesMembersInterface.class);
        Call<SubscriptionStatusResponse> call = client.getSuscriptionStatus(merchant_key);
        call.enqueue(function);
    }
}
