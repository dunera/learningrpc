package com.dunera.rpc.transport.message;

import com.dunera.rpc.common.NumberByteUtil;

/**
 * 报文头
 * 长度8个字节，
 * 前4个字节存放报文长度
 * 第5个字节存储版本号
 * 第6个字节存放加密方式
 * 第7个字节存放序列化方式
 * 第8个字节保留备用
 */
public class Header {

    /**
     * 长度
     */
    private int length;

    /**
     * 版本
     */
    private byte version;

    /**
     * 加密方式
     */
    private int encryptionType;

    /**
     * 序列化方式
     */
    private int serializationType;

    /**
     * 保留备用
     */
    private int reserve;

    public Header(int length, int encryptionType, int serializationType, int reserve) {
        this.length = length;
        this.encryptionType = encryptionType;
        this.serializationType = serializationType;
        this.reserve = reserve;
    }

    public Header(byte[] header) {
        byte[] data = new byte[4];
        System.arraycopy(header, 0, data, 0, data.length);
        this.length = NumberByteUtil.byteArrayToInt(data);
        this.version = header[4];
        this.encryptionType = NumberByteUtil.byteToInt(header[5]);
        this.serializationType = NumberByteUtil.byteToInt(header[6]);
        this.reserve = NumberByteUtil.byteToInt(header[7]);
    }

    /**
     * 返回报文同时将报文体长度初始化好
     */
    public byte[] getHeader(byte[] data) {
        byte[] header = new byte[8 + data.length];
        header[4] = NumberByteUtil.intToByte(10);
        header[5] = NumberByteUtil.intToByte(encryptionType);
        header[6] = NumberByteUtil.intToByte(serializationType);
        header[7] = NumberByteUtil.intToByte(reserve);
        return header;
    }

    @Override
    public String toString() {
        return "Header{" +
                "length=" + length +
                ", version=" + version +
                ", encryptionType=" + encryptionType +
                ", serializationType=" + serializationType +
                '}';
    }

    public int getLength() {
        return length;
    }

    public byte getVersion() {
        return version;
    }

    public int getEncryptionType() {
        return encryptionType;
    }

    public int getSerializationType() {
        return serializationType;
    }

    public int getReserve() {
        return reserve;
    }
}
