package com.ezyplanet.core.data.network;

/**
 * Created by Thuc on 5/12/2016.
 */
public class BaseErrorResponse {


    /**
     * error : invalid_email_or_password
     * error_description : Invalid email or password
     */

    private String error;
    private String error_description;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
