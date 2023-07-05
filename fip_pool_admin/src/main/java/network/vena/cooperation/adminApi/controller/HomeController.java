package network.vena.cooperation.adminApi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import network.vena.cooperation.adminApi.entity.ImageStore;
import network.vena.cooperation.adminApi.param.QueryParam;
import network.vena.cooperation.adminApi.service.impl.HomeServiceImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/adminApi")
@Slf4j
public class HomeController {

    @Autowired
    private HomeServiceImpl homeService;

    @AutoLog(value = "后台首页")
    @PostMapping(value = "/home")
    public Result<?> homeVo(@RequestBody QueryParam queryParam) {
        return Result.ok(homeService.homeVo(queryParam));
    }
}
