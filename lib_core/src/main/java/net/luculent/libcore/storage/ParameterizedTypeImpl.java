package net.luculent.libcore.storage;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Description: 泛型type解析器
 * @Author: yanlei.xia
 * @CreateDate: 2021/10/13 15:04
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Class raw;
    private final Type[] args;

    public ParameterizedTypeImpl(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
