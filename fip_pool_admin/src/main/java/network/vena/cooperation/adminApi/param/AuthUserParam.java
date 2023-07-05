package network.vena.cooperation.adminApi.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthUserParam {
    private String apiKey;
    private Integer level;
    private Integer lock;
    private String marketLevel;
    private String operatingLevel;
}
