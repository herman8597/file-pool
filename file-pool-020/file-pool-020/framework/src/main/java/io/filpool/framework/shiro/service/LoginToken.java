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

package io.filpool.framework.shiro.service;

import java.io.Serializable;

/**
 * 获取登录token
 *
 * @author geekidea
 * @date 2020/3/25
 **/
public interface LoginToken extends Serializable {

    /**
     * 获取登录token
     *
     * @return
     */
    String getToken();

}
