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
import network.vena.cooperation.adminApi.entity.CapitalPool;
import network.vena.cooperation.adminApi.service.ICapitalPoolService;

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
 * @Description: capital_pool
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/capitalPool")
@Slf4j
public class CapitalPoolController extends JeecgController<CapitalPool, ICapitalPoolService> {
	@Autowired
	private ICapitalPoolService capitalPoolService;
	
	/**
	 * 分页列表查询
	 *
	 * @param capitalPool
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CapitalPool capitalPool,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CapitalPool> queryWrapper = QueryGenerator.initQueryWrapper(capitalPool, req.getParameterMap());
		Page<CapitalPool> page = new Page<CapitalPool>(pageNo, pageSize);
		IPage<CapitalPool> pageList = capitalPoolService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param capitalPool
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CapitalPool capitalPool) {
		capitalPoolService.save(capitalPool);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param capitalPool
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CapitalPool capitalPool) {
		capitalPoolService.updateById(capitalPool);
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
		capitalPoolService.removeById(id);
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
		this.capitalPoolService.removeByIds(Arrays.asList(ids.split(",")));
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
		CapitalPool capitalPool = capitalPoolService.getById(id);
		if(capitalPool==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(capitalPool);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param capitalPool
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CapitalPool capitalPool) {
        return super.exportXls(request, capitalPool, CapitalPool.class, "capital_pool");
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
        return super.importExcel(request, response, CapitalPool.class);
    }

}
