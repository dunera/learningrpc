package com.dunera.rpc.transport.serialize;

public interface Serializer {

    /**
     * 序列化
     *
     * @param object 对象
     * @return 序列化后的对象
     */
    byte[] encode(Object object);

    /**
     * 反序列化，只有类型，返回对象
     *
     * @param data  原始字节数组
     * @param clazz 期望的类型
     * @return 反序列化后的对象
     */
    <T> T decode(byte[] data, Class<T> clazz);

}
