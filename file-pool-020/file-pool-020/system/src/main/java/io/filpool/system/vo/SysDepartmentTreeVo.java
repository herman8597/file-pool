/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.filpool.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * 部门TreeVo
 * </pre>
 *
 * @author geekidea
 * @since 2019-11-1
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SysDepartmentTreeVo对象", description = "部门")
public class SysDepartmentTreeVo implements Serializable {
    private static final long serialVersionUID = -2250233632748939400L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("父id")
    private Long parentId;

    @ApiModelProperty("状态，0：禁用，1：启用")
    private Integer state;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("版本")
    private Integer version;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    private List<SysDepartmentTreeVo> children;

}
