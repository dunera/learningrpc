package com.dunera.rpc.registry;

public class RegistryConfig {

    private String host;

    private int port;

    private String registryType;

    public RegistryConfig(String host, int port, String registryType) {
        this.host = host;
        this.port = port;
        this.registryType = registryType;
    }

    public String getHost() {
        return host;
    }

    public RegistryConfig host(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public RegistryConfig port(int port) {
        this.port = port;
        return this;
    }

    public String getRegistryType() {
        return registryType;
    }

    public RegistryConfig registryType(String type) {
        this.registryType = type;
        return this;
    }
}

