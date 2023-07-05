package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 云钱包资产记录 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "云钱包资产记录分页参数")
public class AssetsRecordPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
