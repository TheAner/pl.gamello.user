package gg.gamello.user.exception;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorMessage {

    private String error;

    private Map<String, String> details;

    private ErrorMessage() { }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String error;

        private Map<String, String> details;

        private Builder() {
            error = new String();
            details = new HashMap<>();
        }

        public Builder error(String error){
            this.error = error;
            return this;
        }

        public Builder addDetail(String field, String cause){
            details.put(field, cause);
            return this;
        }

        public Builder details(Map<String, String> details) {
            this.details.putAll(details);
            return this;
        }

        public ErrorMessage build(){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.error = this.error;
            errorMessage.details = this.details;
            return errorMessage;
        }
    }
}