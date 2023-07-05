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

import network.vena.cooperation.util.BeanUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.AdvertisementInfo;
import network.vena.cooperation.adminApi.service.IAdvertisementInfoService;

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
 * @Description: advertisement_info
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/advertisementInfo")
@Slf4j
public class AdvertisementInfoController extends JeecgController<AdvertisementInfo, IAdvertisementInfoService> {
	@Autowired
	private IAdvertisementInfoService advertisementInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param advertisementInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "广告表-查询列表")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AdvertisementInfo advertisementInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AdvertisementInfo> queryWrapper = QueryGenerator.initQueryWrapper(advertisementInfo, req.getParameterMap());
		queryWrapper.select("id,title,`type`,content,pc_content,`rank`,if_use,create_time,update_time,image ");
		Page<AdvertisementInfo> page = new Page<AdvertisementInfo>(pageNo, pageSize);
		IPage<AdvertisementInfo> pageList = advertisementInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param advertisementInfo
	 * @return
	 */
	@AutoLog(value = "广告表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AdvertisementInfo advertisementInfo) {
		advertisementInfo.setCreateTime(new Date()).setUpdateTime(new Date());
		advertisementInfoService.save(advertisementInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param advertisementInfo
	 * @return
	 */
	@AutoLog(value = "广告表-修改")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AdvertisementInfo advertisementInfo) {
		advertisementInfo.setUpdateTime(new Date());
		AdvertisementInfo one = advertisementInfoService.lambdaQuery().eq(AdvertisementInfo::getId, advertisementInfo.getId()).one();
		BeanUtils.copyNewPropertites(one,advertisementInfo);
		advertisementInfoService.updateById(advertisementInfo);
		System.out.println(advertisementInfo.getId());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告表-删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		advertisementInfoService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "广告表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.advertisementInfoService.removeByIds(Arrays.asList(ids.split(",")));
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
		AdvertisementInfo advertisementInfo = advertisementInfoService.getById(id);
		if(advertisementInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(advertisementInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param advertisementInfo
    */
	@AutoLog(value = "广告表-导出excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdvertisementInfo advertisementInfo) {
        return super.exportXls(request, advertisementInfo, AdvertisementInfo.class, "advertisement_info");
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
        return super.importExcel(request, response, AdvertisementInfo.class);
    }

}
