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
import network.vena.cooperation.adminApi.entity.DistributionCheat;
import network.vena.cooperation.adminApi.service.IDistributionCheatService;

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
 * @Description: distribution_cheat
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/distributionCheat")
@Slf4j
public class DistributionCheatController extends JeecgController<DistributionCheat, IDistributionCheatService> {
	@Autowired
	private IDistributionCheatService distributionCheatService;
	
	/**
	 * 分页列表查询
	 *
	 * @param distributionCheat
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DistributionCheat distributionCheat,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DistributionCheat> queryWrapper = QueryGenerator.initQueryWrapper(distributionCheat, req.getParameterMap());
		Page<DistributionCheat> page = new Page<DistributionCheat>(pageNo, pageSize);
		IPage<DistributionCheat> pageList = distributionCheatService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param distributionCheat
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DistributionCheat distributionCheat) {
		distributionCheatService.save(distributionCheat);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param distributionCheat
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DistributionCheat distributionCheat) {
		distributionCheatService.updateById(distributionCheat);
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
		distributionCheatService.removeById(id);
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
		this.distributionCheatService.removeByIds(Arrays.asList(ids.split(",")));
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
		DistributionCheat distributionCheat = distributionCheatService.getById(id);
		if(distributionCheat==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(distributionCheat);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param distributionCheat
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DistributionCheat distributionCheat) {
        return super.exportXls(request, distributionCheat, DistributionCheat.class, "distribution_cheat");
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
        return super.importExcel(request, response, DistributionCheat.class);
    }

}
