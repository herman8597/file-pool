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

import network.vena.cooperation.util.ObjectUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.DictInfo;
import network.vena.cooperation.adminApi.service.IDictInfoService;

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
 * @Description: dict_info
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/dictInfo")
@Slf4j
public class DictInfoController extends JeecgController<DictInfo, IDictInfoService> {
	@Autowired
	private IDictInfoService dictInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param dictInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DictInfo dictInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DictInfo> queryWrapper = QueryGenerator.initQueryWrapper(dictInfo, req.getParameterMap());
		Page<DictInfo> page = new Page<DictInfo>(pageNo, pageSize);
		IPage<DictInfo> pageList = dictInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param dictInfo
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DictInfo dictInfo) {
		dictInfo.setUpdateTime(new Date());
		dictInfoService.save(dictInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param dictInfo
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DictInfo dictInfo) {
		dictInfo.setUpdateTime(new Date());
		dictInfoService.updateById(dictInfo);
		return Result.ok("编辑成功!");
	}
	@PutMapping(value = "/updateValue")
	public Result<?> updateValue(@RequestBody DictInfo dictInfo) {
		DictInfo one = dictInfoService.lambdaQuery().eq(DictInfo::getId, dictInfo.getId()).one();
		if (ObjectUtils.isEmpty(one)) {
			return Result.error("记录不存在!");
		}
		one.setValue(dictInfo.getValue());
		dictInfoService.updateById(one);
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
		dictInfoService.removeById(id);
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
		this.dictInfoService.removeByIds(Arrays.asList(ids.split(",")));
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
		DictInfo dictInfo = dictInfoService.getById(id);
		if(dictInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(dictInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dictInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DictInfo dictInfo) {
        return super.exportXls(request, dictInfo, DictInfo.class, "dict_info");
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
        return super.importExcel(request, response, DictInfo.class);
    }

}
