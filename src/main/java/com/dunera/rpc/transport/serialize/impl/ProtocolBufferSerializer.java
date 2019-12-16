package com.dunera.rpc.transport.serialize.impl;

import com.dunera.rpc.transport.serialize.Serializer;

public class ProtocolBufferSerializer implements Serializer {

    @Override
    public byte[] encode(Object object) {
        return new byte[0];
    }

    @Override
    public <T> T decode(byte[] data, Class<T> clazz) {
        return null;
    }


}
