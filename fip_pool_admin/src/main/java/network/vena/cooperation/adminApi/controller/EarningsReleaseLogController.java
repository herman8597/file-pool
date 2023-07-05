package network.vena.cooperation.adminApi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import network.vena.cooperation.adminApi.excel.ExcelEarningsReleaseLog;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.EarningsReleaseLog;
import network.vena.cooperation.adminApi.service.IEarningsReleaseLogService;

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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

/**
 * @Description: earnings_release_log
 * @Author: jeecg-boot
 * @Date: 2020-10-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/earningsReleaseLog")
@Slf4j
public class EarningsReleaseLogController extends JeecgController<EarningsReleaseLog, IEarningsReleaseLogService> {
    @Autowired
    private IEarningsReleaseLogService earningsReleaseLogService;

    /**
     * 分页列表查询
     *
     * @param earningsReleaseLog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(EarningsReleaseLog earningsReleaseLog,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<EarningsReleaseLog> pageList = getEarningsReleaseLogIPage(earningsReleaseLog, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(EarningsReleaseLog earningsReleaseLog,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest req) {
        IPage<EarningsReleaseLog> pageList = getEarningsReleaseLogIPage(earningsReleaseLog, pageNo, pageSize, req);
		ArrayList<ExcelEarningsReleaseLog> excelEarningsReleaseLogs = new ArrayList<ExcelEarningsReleaseLog>();
		for (EarningsReleaseLog record : pageList.getRecords()) {
			ExcelEarningsReleaseLog excelEarningsReleaseLog = new ExcelEarningsReleaseLog();
			BeanUtils.copyProperties(record,excelEarningsReleaseLog);
			excelEarningsReleaseLogs.add(excelEarningsReleaseLog);
		}
        return super.exportXlsByList(req, excelEarningsReleaseLogs, ExcelEarningsReleaseLog.class, "earnings_release_log");
    }


    private IPage<EarningsReleaseLog> getEarningsReleaseLogIPage(EarningsReleaseLog earningsReleaseLog, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<EarningsReleaseLog> queryWrapper = QueryGenerator.initQueryWrapper(earningsReleaseLog, req.getParameterMap());
        queryWrapper.select("DATE_FORMAT(create_time,'%Y-%m-%d') as create_time ,sum(amount) as amount,asset ");
        queryWrapper.groupBy("create_time,asset");
        Page<EarningsReleaseLog> page = new Page<EarningsReleaseLog>(pageNo, pageSize);
        IPage<EarningsReleaseLog> result = earningsReleaseLogService.page(page, queryWrapper);
        for (EarningsReleaseLog record : result.getRecords()) {

        }
        return result;
    }

    /**
     * 添加
     *
     * @param earningsReleaseLog
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody EarningsReleaseLog earningsReleaseLog) {
        earningsReleaseLogService.save(earningsReleaseLog);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param earningsReleaseLog
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EarningsReleaseLog earningsReleaseLog) {
        earningsReleaseLogService.updateById(earningsReleaseLog);
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
        earningsReleaseLogService.removeById(id);
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
        this.earningsReleaseLogService.removeByIds(Arrays.asList(ids.split(",")));
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
        EarningsReleaseLog earningsReleaseLog = earningsReleaseLogService.getById(id);
        if (earningsReleaseLog == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(earningsReleaseLog);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param earningsReleaseLog
     */


    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EarningsReleaseLog.class);
    }

}
