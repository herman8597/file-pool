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

package io.filpool.framework.common.exception;

import io.filpool.framework.common.api.ApiCode;

/**
 * 业务异常
 *
 * @author geekidea
 * @date 2018-11-08
 */
public class BusinessException extends SpringBootPlusException {
	private static final long serialVersionUID = -2303357122330162359L;

	public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(ApiCode apiCode) {
        super(apiCode);
    }

}
