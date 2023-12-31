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

package io.filpool.system.enums;

import io.filpool.framework.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 启用禁用状态枚举
 *
 * @author geekidea
 * @date 2019-10-24
 **/
@Getter
@AllArgsConstructor
public enum StateEnum implements BaseEnum {

    /** 禁用 **/
    DISABLE(0, "禁用"),
    /** 启用 **/
    ENABLE(1, "启用");

    private Integer code;
    private String desc;

}
