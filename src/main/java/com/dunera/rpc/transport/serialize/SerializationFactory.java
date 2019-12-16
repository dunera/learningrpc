package com.dunera.rpc.transport.serialize;

import com.dunera.rpc.exception.RPCException;
import com.dunera.rpc.transport.serialize.impl.JsonSerializer;
import com.dunera.rpc.transport.serialize.impl.ProtocolBufferSerializer;

public class SerializationFactory {

    public static Serializer getSerializer(SerializationType serializationType) {
        if (serializationType == null) {
            return new JsonSerializer();
        }
        switch (serializationType) {
            case JSON:
                return new JsonSerializer();
            case PROTOCOL_BUFFER:
                return new ProtocolBufferSerializer();
        }
        throw new RPCException("unknown serialization type :" + serializationType.name());
    }

}
