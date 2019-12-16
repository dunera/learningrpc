package com.dunera.rpc.transport.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.dunera.rpc.transport.serialize.Serializer;

public class JsonSerializer implements Serializer {

    @Override
    public byte[] encode(Object object) {
        return JSON.toJSONString(object).getBytes();
    }

    @Override
    public <T> T decode(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data), clazz);
    }
}
