package com.dunera.rpc.transport.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RPCFutureCache {

    public static volatile Map<String, RPCFuture<Response>> rpcFutureCache = new ConcurrentHashMap<>();

}
