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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.PledgeLog;
import network.vena.cooperation.adminApi.service.IPledgeLogService;

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
 * @Description: pledge_log
 * @Author: jeecg-boot
 * @Date:   2020-11-30
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/pledgeLog")
@Slf4j
public class PledgeLogController extends JeecgController<PledgeLog, IPledgeLogService> {
	@Autowired
	private IPledgeLogService pledgeLogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pledgeLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PledgeLog pledgeLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PledgeLog> queryWrapper = QueryGenerator.initQueryWrapper(pledgeLog, req.getParameterMap());
		Page<PledgeLog> page = new Page<PledgeLog>(pageNo, pageSize);
		IPage<PledgeLog> pageList = pledgeLogService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pledgeLog
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PledgeLog pledgeLog) {
		pledgeLogService.save(pledgeLog);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pledgeLog
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PledgeLog pledgeLog) {
		pledgeLogService.updateById(pledgeLog);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pledgeLogService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pledgeLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PledgeLog pledgeLog = pledgeLogService.getById(id);
		if(pledgeLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pledgeLog);
	}

    /**
    * 导出excel
    *
    * @param pledgeLog
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(PledgeLog pledgeLog,
								  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								  HttpServletRequest req) {
		QueryWrapper<PledgeLog> queryWrapper = QueryGenerator.initQueryWrapper(pledgeLog, req.getParameterMap());
		Page<PledgeLog> page = new Page<PledgeLog>(pageNo, pageSize);
		IPage<PledgeLog> pageList = pledgeLogService.page(page, queryWrapper);
        return super.exportXlsByList(req, pageList.getRecords(), PledgeLog.class, "pledge_log");
    }
/*
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PledgeLog pledgeLog) {
        return super.exportXls(request, pledgeLog, PledgeLog.class, "pledge_log");
    }
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
        return super.importExcel(request, response, PledgeLog.class);
    }

}
