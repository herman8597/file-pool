package io.filpool.pool.model;

import lombok.Data;

/**
 * Json RPC 请求矿架
 */
@Data
public class JsonRpc {

    public JsonRpc() {
        id = System.currentTimeMillis() / 1000;
        params = new String[]{};
    }

    private String jsonrpc = "2.0";
    private String method;
    private Object[] params;
    private Long id;
}
