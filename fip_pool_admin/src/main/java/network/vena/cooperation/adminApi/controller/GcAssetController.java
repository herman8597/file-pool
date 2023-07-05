package network.vena.cooperation.adminApi.controller;

import java.math.BigDecimal;
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

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.GcAsset;
import network.vena.cooperation.adminApi.service.IGcAssetService;

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
 * @Description: gc_asset
 * @Author: jeecg-boot
 * @Date:   2020-04-28
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/gcAsset")
@Slf4j
public class GcAssetController extends JeecgController<GcAsset, IGcAssetService> {
	@Autowired
	private IGcAssetService gcAssetService;
	
	/**
	 * 分页列表查询
	 *
	 * @param gcAsset
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GcAsset gcAsset,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GcAsset> queryWrapper = QueryGenerator.initQueryWrapper(gcAsset, req.getParameterMap());
		Page<GcAsset> page = new Page<GcAsset>(pageNo, pageSize);
		IPage<GcAsset> pageList = gcAssetService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param gcAsset
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GcAsset gcAsset) {
		gcAsset.setUpdateTime(new Date()).setCreateTime(new Date()).setExchangeDecimals(8).setInternalTransferFee(BigDecimal.ZERO);
		gcAssetService.save(gcAsset);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param gcAsset
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GcAsset gcAsset) {
		gcAsset.setUpdateTime(new Date());
		//gcAssetService.updateById(gcAsset);
		UpdateWrapper<GcAsset> gcAssetUpdateWrapper = new UpdateWrapper<>();
		gcAssetUpdateWrapper.eq("asset",gcAsset.getAsset());
		gcAssetService.update(gcAsset,gcAssetUpdateWrapper);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param asset
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="asset",required=true) String asset) {

		//gcAssetService.removeById(id);
		QueryWrapper<GcAsset> gcAssetQueryWrapper = new QueryWrapper<>();
		gcAssetQueryWrapper.eq("asset",asset);
		gcAssetService.remove(gcAssetQueryWrapper);
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
		this.gcAssetService.removeByIds(Arrays.asList(ids.split(",")));
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
		GcAsset gcAsset = gcAssetService.getById(id);
		if(gcAsset==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(gcAsset);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param gcAsset
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GcAsset gcAsset) {
        return super.exportXls(request, gcAsset, GcAsset.class, "gc_asset");
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
        return super.importExcel(request, response, GcAsset.class);
    }

}
