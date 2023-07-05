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

package io.filpool.framework.util;

import io.filpool.framework.common.exception.FILPoolException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 密码加密工具类
 *
 * @author geekidea
 * @date 2018-11-08
 */
@Slf4j
public class PasswordUtil {

    /**
     * 密码加盐，再加密
     *
     * @param pwd
     * @param salt
     * @return
     */
    public static String encrypt(String pwd, String salt) throws Exception {
        if (StringUtils.isBlank(pwd)) {
            throw new FILPoolException("transfer.pwd.not-empty");
        }
        if (StringUtils.isBlank(salt)) {
            throw new FILPoolException("transfer.salt.not-empty");
        }
        return DigestUtils.sha256Hex(pwd + salt);
    }

    public static void main(String[] args) {
        String encrypt = null;
        try {
            encrypt = encrypt("123456", "daa60a589da30e100995fa5b6b7b324f");
        } catch (Exception e) {
            e.printStackTrace();
        }
        float a = 0.1f + 0.2f;
        System.out.println(a);
        System.out.println(encrypt);
    }

}
