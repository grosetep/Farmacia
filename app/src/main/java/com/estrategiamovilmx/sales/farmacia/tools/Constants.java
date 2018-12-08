package com.estrategiamovilmx.sales.farmacia.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 10/07/2017.
 */
public class Constants {
    public static final String PUERTO_HOST = "";//:63343
    public static final String IP = "www.estrategiamovilmx.com";
    public static final String HTTPS = "https://";
    public static final String HTTP = "http://";
    public static final String SUBDOMAIN = "/drogstore/farmaxtraservice/services.php/";
    public static final String MENBERS_DOMAIN_REST =Constants.HTTPS + Constants.IP + Constants.PUERTO_HOST + "/members/services/services.php/";
    public static final String RETROFIT_SERVICE_REST = Constants.HTTPS + Constants.IP + Constants.PUERTO_HOST + Constants.SUBDOMAIN;
    /**
     * URLs del Web Service
     */
    /*Publications*/
    public static final String GET_PRODUCTS = HTTPS + IP + PUERTO_HOST + "/drogstore/farmaxtraservice/getProducts.php";
    /* Upload images*/
    public static final String UPLOAD_IMAGE = HTTP + IP + PUERTO_HOST + "/drogstore/farmaxtraservice/uploadImage.php";
    public static final Integer DURATION = 1000;

    public static final String GALLERY = "gallery";
    public static final String SELECTED_CONTACT = "select_contact";
    public static final String SELECTED_SHIPPING = "select_shipping";
    public static final String SELECTED_PRODUCT = "select_product";
    public static final String SELECTED_PAYMENT_METHOD = "select_payment_method";
    public static final String SELECTED_SHOPPING_CART = "select_shopping_cart";
    public static final String ORDER_NO = "order_no";

    public static final int MY_SOCKET_TIMEOUT_MS = 15000;
    public static final String products_category_selected = "products_category_selected";
    public static final int cero = 0;
    public static final int uno = 1;
    public static final int load_more_tax_extended = 15;
    public static final int address_max_length = 70;

    /* resource types*/
    public static final String resource_local= "local";
    public static final String resource_remote = "remote";

    /* response status*/
    public static final String success = "1";
    public static final String no_data = "2";

    /*Geolocalication*/
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;
    public static final String PACKAGE_NAME =
            "com.estrategiamovilmx.sales.farmacia";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
    public static final String LOCATION_LATITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LATITUDE_DATA_EXTRA";
    public static final String LOCATION_LONGITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LONGITUDE_DATA_EXTRA";
    public static final String LOCATION_NAME_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_NAME_DATA_EXTRA";
    public static final String FETCH_TYPE_EXTRA = PACKAGE_NAME + ".FETCH_TYPE_EXTRA";
    public static final String FETCH_TYPE_REQUEST = "CP_REQUEST";

    /* Colors*/
    public static final String colorSuccess = "#7fc120";

    /*Firebase*/
    public static final String firebase_token = "firebase_token";

    /*UserItem*/

    public static final String seedValue = "sales170820172130";
    public static final String reset_password = "nomeolvides108322";
    public static final String user_object = "user";
    public static final String configuration_object = "config";

    /* status orders:'revision','aceptado','rechazado','en_camino','no_entregado','entregado','cancelado'*/
    public static  final String days_to_show_orders = "days_to_show_orders";
    public static  final String days_to_show_orders_default = "5";
    public static final Map<String, Integer> estatus_shipping = new HashMap<String, Integer>()
    {{
        put(status_review,1);
        put(status_accepted,2);
        put(status_rejected,3);
        put(status_on_way,4);
        put(status_deliver,5);
        put(status_no_deliver,6);
        put(status_cancel,7);
    }};

    public static final String status_review = "revision";
    public static final String status_accepted = "aceptado";
    public static final String status_rejected = "rechazado";
    public static final String status_on_way = "en_camino";
    public static final String status_deliver = "entregado";
    public static final String status_no_deliver = "no_entregado";
    public static final String status_cancel = "cancelado";
    /* profiles user*/
    public static final String profile_client= "client";
    public static final String profile_deliver_man = "deliver_man";
    public static final String profile_admin = "admin";

    /* flows*/
    public static final String flow = "flow";
    /* loading number elements */
    public static final int load_more_tax = 7;
    /* Suscription Validation */
    public static final boolean is_subscriber = true;
    public static final String merchant_key = "merchant_01112017";
    public static final String app_label = "demo";
    public static final String system_date = "system_date";
}
