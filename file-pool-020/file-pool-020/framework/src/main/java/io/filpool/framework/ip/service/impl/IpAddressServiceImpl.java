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

package io.filpool.framework.ip.service.impl;

import io.filpool.config.constant.CommonConstant;
import io.filpool.framework.ip.entity.IpAddress;
import io.filpool.framework.ip.mapper.IpAddressMapper;
import io.filpool.framework.ip.service.IpAddressService;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * IP地址 服务实现类
 *
 * @author geekidea
 * @since 2020-03-25
 */
@Slf4j
@Service
public class IpAddressServiceImpl extends BaseServiceImpl<IpAddressMapper, IpAddress> implements IpAddressService {

    @Autowired
    private IpAddressMapper ipAddressMapper;


    @Override
    public IpAddress getByIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            return null;
        }
        if (CommonConstant.LOCALHOST_IP.equals(ip)) {
            return new IpAddress().setArea(CommonConstant.LOCALHOST_IP_NAME);
        }
        if (CommonConstant.LAN_IP.equals(ip)) {
            return new IpAddress().setArea(CommonConstant.LAN_IP_NAME);
        }
        return ipAddressMapper.getByIp(ip);
    }

    @Override
    public String getAreaByIp(String ip) {
        IpAddress ipAddress = getByIp(ip);
        if (ipAddress != null) {
            return ipAddress.getArea();
        }
        return null;
    }

    @Override
    public String getOperatorByIp(String ip) {
        IpAddress ipAddress = getByIp(ip);
        if (ipAddress != null) {
            return ipAddress.getOperator();
        }
        return null;
    }
}
