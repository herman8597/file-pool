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
import network.vena.cooperation.adminApi.entity.CollectPipeline;
import network.vena.cooperation.adminApi.service.ICollectPipelineService;

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
 * @Description: collect_pipeline
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/collectPipeline")
@Slf4j
public class CollectPipelineController extends JeecgController<CollectPipeline, ICollectPipelineService> {
	@Autowired
	private ICollectPipelineService collectPipelineService;
	
	/**
	 * 分页列表查询
	 *
	 * @param collectPipeline
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CollectPipeline collectPipeline,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CollectPipeline> queryWrapper = QueryGenerator.initQueryWrapper(collectPipeline, req.getParameterMap());
		Page<CollectPipeline> page = new Page<CollectPipeline>(pageNo, pageSize);
		IPage<CollectPipeline> pageList = collectPipelineService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param collectPipeline
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CollectPipeline collectPipeline) {
		collectPipelineService.save(collectPipeline);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param collectPipeline
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CollectPipeline collectPipeline) {
		collectPipelineService.updateById(collectPipeline);
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
		collectPipelineService.removeById(id);
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
		this.collectPipelineService.removeByIds(Arrays.asList(ids.split(",")));
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
		CollectPipeline collectPipeline = collectPipelineService.getById(id);
		if(collectPipeline==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(collectPipeline);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param collectPipeline
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CollectPipeline collectPipeline) {
        return super.exportXls(request, collectPipeline, CollectPipeline.class, "collect_pipeline");
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
        return super.importExcel(request, response, CollectPipeline.class);
    }

}
