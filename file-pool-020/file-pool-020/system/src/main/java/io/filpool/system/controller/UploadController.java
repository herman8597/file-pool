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

package io.filpool.system.controller;

import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.util.AliyunOssUtil;
import io.filpool.framework.util.HWObsUtil;
import io.filpool.framework.util.UUIDUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

/**
 * 上传控制器
 *
 * @author geekidea
 * @date 2019/8/20
 * @since 1.2.1-RELEASE
 */
@Slf4j
@RestController
@RequestMapping("/upload")
@Module("system")
@Api(value = "文件上传", tags = {"文件上传"})
public class UploadController {

    @Autowired
    private AliyunOssUtil ossUtil;
    @Autowired
    private HWObsUtil obsUtil;
    @Value("${upload.active}")
    private Integer active;

    /**
     * 上传单个文件
     *
     * @return
     */
    @PostMapping
    @OperationLog(name = "上传单个文件", type = OperationLogType.UPLOAD)
    @ApiOperation(value = "上传单个文件", response = ApiResult.class)
    public ApiResult<String> uploadFile(@ApiParam("需要上传的文件") @RequestParam("file") MultipartFile file) throws Exception {

        if (StringUtils.isEmpty(file.getName())) {
            throw new FILPoolException("upload.file.emptyName");
        }
        String path = null;
        try {
            if (active == 1) {
                File uploadFile = ossUtil.multipartFileToFile(file);
                String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
                String uploadPath = "file/" + UUIDUtil.getUuid() + suffix;
                path = ossUtil.uploadFile(uploadFile, uploadPath);
            } else {
                File uploadFile = obsUtil.multipartFileToFile(file);
                String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
                String uploadPath = "file/" + UUIDUtil.getUuid() + suffix;
                path = obsUtil.uploadFile(uploadFile, uploadPath);
            }
        } catch (Exception e) {
            throw new FILPoolException("upload.file.failed");
        }
        return ApiResult.ok(path);
    }
}
