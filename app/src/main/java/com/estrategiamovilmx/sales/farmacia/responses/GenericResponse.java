package com.estrategiamovilmx.sales.farmacia.responses;

import com.estrategiamovilmx.sales.farmacia.items.Result;

import java.util.List;

/**
 * Created by administrator on 09/08/2017.
 */
public class GenericResponse {
    private String status;
    private Result result;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GenericResponse{" +
                "status='" + status + '\'' +
                ", result=" + result +
                ", message='" + message + '\'' +
                '}';
    }
}



