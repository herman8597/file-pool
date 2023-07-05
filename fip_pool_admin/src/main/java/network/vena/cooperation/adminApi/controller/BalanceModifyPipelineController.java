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
import network.vena.cooperation.adminApi.entity.BalanceModifyPipeline;
import network.vena.cooperation.adminApi.service.IBalanceModifyPipelineService;

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
 * @Description: balance_modify_pipeline
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/balanceModifyPipeline")
@Slf4j
public class BalanceModifyPipelineController extends JeecgController<BalanceModifyPipeline, IBalanceModifyPipelineService> {
	@Autowired
	private IBalanceModifyPipelineService balanceModifyPipelineService;
	
	/**
	 * 分页列表查询
	 *
	 * @param balanceModifyPipeline
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BalanceModifyPipeline balanceModifyPipeline,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BalanceModifyPipeline> queryWrapper = QueryGenerator.initQueryWrapper(balanceModifyPipeline, req.getParameterMap());
		Page<BalanceModifyPipeline> page = new Page<BalanceModifyPipeline>(pageNo, pageSize);
		IPage<BalanceModifyPipeline> pageList = balanceModifyPipelineService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param balanceModifyPipeline
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BalanceModifyPipeline balanceModifyPipeline) {
		balanceModifyPipelineService.save(balanceModifyPipeline);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param balanceModifyPipeline
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BalanceModifyPipeline balanceModifyPipeline) {
		balanceModifyPipelineService.updateById(balanceModifyPipeline);
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
		balanceModifyPipelineService.removeById(id);
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
		this.balanceModifyPipelineService.removeByIds(Arrays.asList(ids.split(",")));
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
		BalanceModifyPipeline balanceModifyPipeline = balanceModifyPipelineService.getById(id);
		if(balanceModifyPipeline==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(balanceModifyPipeline);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param balanceModifyPipeline
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BalanceModifyPipeline balanceModifyPipeline) {
        return super.exportXls(request, balanceModifyPipeline, BalanceModifyPipeline.class, "balance_modify_pipeline");
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
        return super.importExcel(request, response, BalanceModifyPipeline.class);
    }

}
