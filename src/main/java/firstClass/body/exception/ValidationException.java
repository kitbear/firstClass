package firstClass.body.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wkit
 * @Date: 2019-10-15 21:07
 */
public class ValidationException extends RuntimeException {

    private List<ValidationError> validationErrors = new ArrayList<>();

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public ValidationException(ValidationError validationError) {
        validationErrors.add(validationError);
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    @Override
    public String toString() {
        return "ValidationException{" +
                "validationErrors=" + validationErrors +
                '}';
    }
}
