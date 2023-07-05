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
import network.vena.cooperation.adminApi.entity.GcDepositAddress;
import network.vena.cooperation.adminApi.service.IGcDepositAddressService;

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
 * @Description: gc_deposit_address
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/gcDepositAddress")
@Slf4j
public class GcDepositAddressController extends JeecgController<GcDepositAddress, IGcDepositAddressService> {
	@Autowired
	private IGcDepositAddressService gcDepositAddressService;
	
	/**
	 * 分页列表查询
	 *
	 * @param gcDepositAddress
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GcDepositAddress gcDepositAddress,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GcDepositAddress> queryWrapper = QueryGenerator.initQueryWrapper(gcDepositAddress, req.getParameterMap());
		Page<GcDepositAddress> page = new Page<GcDepositAddress>(pageNo, pageSize);
		IPage<GcDepositAddress> pageList = gcDepositAddressService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param gcDepositAddress
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GcDepositAddress gcDepositAddress) {
		gcDepositAddressService.save(gcDepositAddress);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param gcDepositAddress
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GcDepositAddress gcDepositAddress) {
		gcDepositAddressService.updateById(gcDepositAddress);
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
		gcDepositAddressService.removeById(id);
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
		this.gcDepositAddressService.removeByIds(Arrays.asList(ids.split(",")));
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
		GcDepositAddress gcDepositAddress = gcDepositAddressService.getById(id);
		if(gcDepositAddress==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(gcDepositAddress);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param gcDepositAddress
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GcDepositAddress gcDepositAddress) {
        return super.exportXls(request, gcDepositAddress, GcDepositAddress.class, "gc_deposit_address");
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
        return super.importExcel(request, response, GcDepositAddress.class);
    }

}
