package firstClass.body.service.decorator;

/**
 * @Author: wkit
 * @Date: 2019-10-15 21:51
 */
public interface AbstractEntityDecorator<T> {
    void decoratePreCreate(T object);

    void decoratePreUpdate(T object, T persisted);

    void decoratePostCreate(T object);

    void decoratePostUpdate(T object, T persisted);
}
