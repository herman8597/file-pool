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
import network.vena.cooperation.adminApi.entity.AuthChannelPipeline;
import network.vena.cooperation.adminApi.service.IAuthChannelPipelineService;

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
 * @Description: auth_channel_pipeline
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/authChannelPipeline")
@Slf4j
public class AuthChannelPipelineController extends JeecgController<AuthChannelPipeline, IAuthChannelPipelineService> {
	@Autowired
	private IAuthChannelPipelineService authChannelPipelineService;
	
	/**
	 * 分页列表查询
	 *
	 * @param authChannelPipeline
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AuthChannelPipeline authChannelPipeline,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AuthChannelPipeline> queryWrapper = QueryGenerator.initQueryWrapper(authChannelPipeline, req.getParameterMap());
		Page<AuthChannelPipeline> page = new Page<AuthChannelPipeline>(pageNo, pageSize);
		IPage<AuthChannelPipeline> pageList = authChannelPipelineService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param authChannelPipeline
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AuthChannelPipeline authChannelPipeline) {
		authChannelPipelineService.save(authChannelPipeline);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param authChannelPipeline
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AuthChannelPipeline authChannelPipeline) {
		authChannelPipelineService.updateById(authChannelPipeline);
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
		authChannelPipelineService.removeById(id);
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
		this.authChannelPipelineService.removeByIds(Arrays.asList(ids.split(",")));
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
		AuthChannelPipeline authChannelPipeline = authChannelPipelineService.getById(id);
		if(authChannelPipeline==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(authChannelPipeline);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param authChannelPipeline
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AuthChannelPipeline authChannelPipeline) {
        return super.exportXls(request, authChannelPipeline, AuthChannelPipeline.class, "auth_channel_pipeline");
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
        return super.importExcel(request, response, AuthChannelPipeline.class);
    }

}
