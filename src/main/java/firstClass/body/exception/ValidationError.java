package firstClass.body.exception;

import org.springframework.validation.FieldError;

/**
 * @Author: wkit
 * @Date: 2019-10-15 20:57
 */
public class ValidationError {

    private String attributeName;

    private String message;

    public ValidationError() {
    }

    public ValidationError(final String attributeName, final String message) {
        this.attributeName = attributeName;
        this.message = message;
    }

    public ValidationError(FieldError error) {
        this.attributeName = error.getField();
        this.message = error.getDefaultMessage();
    }

    public String getExternalPropertyName() {
        return attributeName;
    }

    public void setExternalPropertyName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "attributeName='" + attributeName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
