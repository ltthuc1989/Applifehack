package com.ezyplanet.core.data.network;

import java.util.List;

/**
 * Created by Thuc on 4/12/2016.
 */
public class ErrorResponse {

    /**
     * attribute : password
     * full_messages : ["Password can't be blank"]
     */

    private String attribute;
    private List<String> full_messages;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public List<String> getFull_messages() {
        return full_messages;
    }

    public void setFull_messages(List<String> full_messages) {
        this.full_messages = full_messages;
    }
}
