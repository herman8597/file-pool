package network.vena.cooperation.adminApi.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import network.vena.cooperation.adminApi.service.impl.TotalRevenueLogServiceImpl;
import network.vena.cooperation.common.constant.PojoConstants;
import network.vena.cooperation.util.BigDecimalUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.TotalRevenueLog;
import network.vena.cooperation.adminApi.service.ITotalRevenueLogService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

/**
 * @Description: total_revenue_log
 * @Author: jeecg-boot
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/totalRevenueLog")
@Slf4j
public class TotalRevenueLogController extends JeecgController<TotalRevenueLog, ITotalRevenueLogService> {
    @Autowired
    private TotalRevenueLogServiceImpl totalRevenueLogService;

    /**
     * 分页列表查询
     *
     * @param totalRevenueLog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TotalRevenueLog totalRevenueLog,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TotalRevenueLog> queryWrapper = QueryGenerator.initQueryWrapper(totalRevenueLog, req.getParameterMap());
        Page<TotalRevenueLog> page = new Page<TotalRevenueLog>(pageNo, pageSize);
        IPage<TotalRevenueLog> pageList = totalRevenueLogService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param totalRevenueLog
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TotalRevenueLog totalRevenueLog) {
       /* TotalRevenueLog one = totalRevenueLogService.getBaseMapper().getByDate(totalRevenueLog.getGrantDate());
        if (ObjectUtils.isNotEmpty(one)) {
            return Result.error("当前日期已存在");
        }*/
//        totalRevenueLog.setAsset(PojoConstants.COIN_FIL);
        totalRevenueLogService.save(totalRevenueLog);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param totalRevenueLog
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TotalRevenueLog totalRevenueLog) {
        /*TotalRevenueLog one = totalRevenueLogService.getBaseMapper().getByDate(totalRevenueLog.getGrantDate());
        if (ObjectUtils.isNotEmpty(one)) {
            return Result.error("当前日期已存在");
        }*/

        TotalRevenueLog one = totalRevenueLogService.lambdaQuery().eq(TotalRevenueLog::getId, totalRevenueLog.getId()).one();
        if (1==one.getIsGrant()) {
            return Result.error("已发放的奖励不可以编辑");
        }
        totalRevenueLogService.updateById(totalRevenueLog);

        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        TotalRevenueLog totalRevenueLog = totalRevenueLogService.lambdaQuery().eq(TotalRevenueLog::getId, id).one();
        if (BigDecimalUtil.equal(1, totalRevenueLog.getIsGrant())) {
            return Result.error("已发放的奖励不可以删除!");
        }
        totalRevenueLogService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            TotalRevenueLog byId = totalRevenueLogService.getById(id);
            if (BigDecimalUtil.equal(byId.getIsGrant(), 1)) {
                return Result.error("已发放的奖励不可以删除!");
            }
        }
        this.totalRevenueLogService.removeByIds(idList);
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TotalRevenueLog totalRevenueLog = totalRevenueLogService.getById(id);
        if (totalRevenueLog == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(totalRevenueLog);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param totalRevenueLog
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TotalRevenueLog totalRevenueLog) {
        return super.exportXls(request, totalRevenueLog, TotalRevenueLog.class, "total_revenue_log");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TotalRevenueLog.class);
    }

}
