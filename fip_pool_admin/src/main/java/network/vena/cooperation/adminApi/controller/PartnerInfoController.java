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
import network.vena.cooperation.adminApi.entity.PartnerInfo;
import network.vena.cooperation.adminApi.service.IPartnerInfoService;

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
 * @Description: partner_info
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/partnerInfo")
@Slf4j
public class PartnerInfoController extends JeecgController<PartnerInfo, IPartnerInfoService> {
	@Autowired
	private IPartnerInfoService partnerInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param partnerInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PartnerInfo partnerInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PartnerInfo> queryWrapper = QueryGenerator.initQueryWrapper(partnerInfo, req.getParameterMap());
		queryWrapper.select("id,title,link,image,`rank`,if_use,update_time");
		Page<PartnerInfo> page = new Page<PartnerInfo>(pageNo, pageSize);
		IPage<PartnerInfo> pageList = partnerInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param partnerInfo
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PartnerInfo partnerInfo) {
		partnerInfo.setUpdateTime(new Date());
		partnerInfoService.save(partnerInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param partnerInfo
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PartnerInfo partnerInfo) {
		partnerInfo.setUpdateTime(new Date());
		partnerInfoService.updateById(partnerInfo);
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
		partnerInfoService.removeById(id);
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
		this.partnerInfoService.removeByIds(Arrays.asList(ids.split(",")));
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
		PartnerInfo partnerInfo = partnerInfoService.getById(id);
		if(partnerInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(partnerInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param partnerInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PartnerInfo partnerInfo) {
        return super.exportXls(request, partnerInfo, PartnerInfo.class, "partner_info");
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
        return super.importExcel(request, response, PartnerInfo.class);
    }

}
