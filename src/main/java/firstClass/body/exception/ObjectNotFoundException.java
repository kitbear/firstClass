package firstClass.body.exception;

/**
 * @Author: wkit
 * @Date: 2019-10-15 22:08
 */
public class ObjectNotFoundException extends RuntimeException {
    private Class targetObject;

    public ObjectNotFoundException(Class targetObject, Long targetObjectID) {
        super(targetObject.getSimpleName() + "[id=]"+ targetObjectID+"] not found");
        this.targetObject = targetObject;
    }

    public ObjectNotFoundException(Class targetObject, String message) {
        super(targetObject.getSimpleName() + "[" + message + "] not found");
        this.targetObject = targetObject;
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public Class getTargetObject(){
        return targetObject;
    }
}
