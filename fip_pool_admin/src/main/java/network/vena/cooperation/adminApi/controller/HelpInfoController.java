package network.vena.cooperation.adminApi.controller;

import java.util.Arrays;
import java.util.Date;
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
import network.vena.cooperation.adminApi.entity.HelpInfo;
import network.vena.cooperation.adminApi.service.IHelpInfoService;

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
 * @Description: help_info
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/helpInfo")
@Slf4j
public class HelpInfoController extends JeecgController<HelpInfo, IHelpInfoService> {
	@Autowired
	private IHelpInfoService helpInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param helpInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HelpInfo helpInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<HelpInfo> queryWrapper = QueryGenerator.initQueryWrapper(helpInfo, req.getParameterMap());
		queryWrapper.select("id,`type`,title,en_title,description,en_description,image,content,`rank`,if_use,update_time,click_count");
		Page<HelpInfo> page = new Page<HelpInfo>(pageNo, pageSize);
		IPage<HelpInfo> pageList = helpInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param helpInfo
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HelpInfo helpInfo) {
		helpInfo.setUpdateTime(new Date());
		helpInfoService.save(helpInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param helpInfo
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HelpInfo helpInfo) {
		helpInfo.setUpdateTime(new Date());
		helpInfoService.updateById(helpInfo);
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
		helpInfoService.removeById(id);
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
		this.helpInfoService.removeByIds(Arrays.asList(ids.split(",")));
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
		HelpInfo helpInfo = helpInfoService.getById(id);
		if(helpInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(helpInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param helpInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HelpInfo helpInfo) {
        return super.exportXls(request, helpInfo, HelpInfo.class, "help_info");
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
        return super.importExcel(request, response, HelpInfo.class);
    }

}
