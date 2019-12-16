package com.dunera.rpc.transport.serialize;

public enum SerializationType {

    JSON(1),

    PROTOCOL_BUFFER(2);

    SerializationType(int val) {
        this.val = val;
    }

    private int val;

    public static SerializationType getSerializationType(Integer val) {
        if (val != null) {
            for (SerializationType type : SerializationType.values()) {
                if (val == type.val) {
                    return type;
                }
            }
        }
        return null;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
