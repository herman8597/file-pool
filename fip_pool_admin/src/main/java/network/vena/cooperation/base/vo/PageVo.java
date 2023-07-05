package network.vena.cooperation.base.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PageVo {
    @Min(1)
    @NotNull
    private Integer pageNum;
    @Min(1)
    @NotNull
    private Integer pageSize;
}
