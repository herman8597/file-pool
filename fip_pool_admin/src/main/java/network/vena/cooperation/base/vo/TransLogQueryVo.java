package network.vena.cooperation.base.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TransLogQueryVo {
    private Integer id;

    private String symbol;

    private String createTime;

    private String transHash;

    private String fromAddress;

    private String toAddress;

    private BigDecimal fee;

    private BigDecimal amount;

    private Integer type;//交易状态：1成功，2 失败，3 等待确认

    private Integer blockNum; // 区块高度

    private String blockHash;  // 区块哈希

}
