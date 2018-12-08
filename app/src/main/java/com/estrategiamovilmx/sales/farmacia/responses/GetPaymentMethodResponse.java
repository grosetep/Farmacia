package com.estrategiamovilmx.sales.farmacia.responses;

import com.estrategiamovilmx.sales.farmacia.model.PaymentMethod;

import java.util.List;

/**
 * Created by administrator on 04/08/2017.
 */
public class GetPaymentMethodResponse {
    private String status;
    private List<PaymentMethod> result = null;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PaymentMethod> getResult() {
        return result;
    }

    public void setResult(List<PaymentMethod> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
