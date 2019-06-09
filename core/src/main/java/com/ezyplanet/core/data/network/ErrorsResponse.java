package com.ezyplanet.core.data.network;

import java.util.List;

/**
 * Created by Thuc on 7/13/2016.
 */
public class ErrorsResponse {

    /**
     * attribute : invalid_request
     * message : Amount must convert to at least 50 cents. ₫52 converts to approximately $0.00.
     */

    private List<ErrorsEntity> errors;

    public List<ErrorsEntity> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorsEntity> errors) {
        this.errors = errors;
    }

    public static class ErrorsEntity {
        private String attribute;
        private String message;

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
