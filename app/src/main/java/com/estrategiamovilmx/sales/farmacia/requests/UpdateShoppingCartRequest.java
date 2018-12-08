package com.estrategiamovilmx.sales.farmacia.requests;

import com.estrategiamovilmx.sales.farmacia.items.CartProductItem;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by administrator on 11/08/2017.
 */
public class UpdateShoppingCartRequest implements Serializable{
    @SerializedName("idUser")
    private String idUser;
    @SerializedName("products")
    private String products;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
