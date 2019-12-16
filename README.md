## 项目定位
- 实现一个简版rpc框架，提供基础的rpc功能，为更深入地理解rpc与分布式系统原理

## 本框架包含的基础功能与服务

- 客户端支持
    - 配置支持与管理
    - 服务发现
    - 负载均衡，服务路由，集群容错
    - 代理远程调用
    - 序列化与反序列化
    - 网络通信传输
    
- 服务端支持
    - 配置支持与管理
    - 注册对外提供的服务
    - 网络监听
    - 协议解析
    - 序列化与反序列化

- 注册中心
    - 服务注册、发现

## 项目结构
- client        （客户端对应实现相关代码）
  - loadbalance （负载均衡相关）
- common        （通用基础类）
- demo          （示例）
- exception     （异常相关）
- proxy         （代理相关）
- registry      （注册中心相关）
- server        （服务端对应实现相关代码）
- transport     （通信相关代码）
  - serialize   （序列化相关）
  - message     （消息、报文）

## 项目流程
1、注册中心启动，对外提供服务注册与发现功能
2、服务提供方启动，并对外暴露服务，同时异步将服务提供者信息注册到注册中心
3、服务消费方启动，从注册中心获取服务提供者信息
4、服务消费方请求服务提供方进行rpc通信

## 项目技术点
1、运用Java的SPI机制，使得一些功能的实现做到可插拔，可以更换实现
2、基于netty的通信框架，