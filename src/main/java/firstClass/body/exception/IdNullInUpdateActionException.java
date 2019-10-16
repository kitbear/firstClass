package firstClass.body.exception;

/**
 * Thrown if Id is null when updating action
 * @Author: wkit
 * @Date: 2019-10-15 22:02
 */
public class IdNullInUpdateActionException extends ValidationException {
    public IdNullInUpdateActionException(){
        super(new ValidationError("id", "should.not.be.null"));
    }

}
