package network.vena.cooperation.common.poster.kernal;


import network.vena.cooperation.common.poster.contracts.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class BlankResult extends JsonAble implements Result {
    private String code = Result.SUCCESSFUL;
    private String msg = null;
    private Map<String, String> data;

    public BlankResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean isSuccessful() {
        return code.equals(Result.SUCCESSFUL);
    }

    /**
     * 获取 data 里面的字段
     *
     * @param name
     * @return String
     */
    public String get(String name) {
        return data.get(name);
    }
}