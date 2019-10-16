package firstClass.body.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: wkit
 * @Date: 2019-10-15 21:35
 */
public class ClassUtil {

    public static Class getConcreteClassFromGenericType(Class serviceClass, int classIndex) {
        Type superclass = serviceClass.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types == null || types.length == 0) {
            return null;
        }
        if (types.length <= classIndex) {
            throw new IllegalArgumentException(String.format("%s doesn't have %d generic type(s)", serviceClass.getSimpleName(), classIndex + 1));
        }
        return (Class) types[classIndex];
    }


}
