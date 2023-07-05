package network.vena.cooperation.adminApi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import network.vena.cooperation.adminApi.entity.FilGoods;
import network.vena.cooperation.adminApi.service.IFilGoodsService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: fil_goods
 * @Author: jeecg-boot
 * @Date:   2020-12-31
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/filGoods")
@Slf4j
public class FilGoodsController extends JeecgController<FilGoods, IFilGoodsService> {
	@Autowired
	private IFilGoodsService filGoodsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param filGoods
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FilGoods filGoods,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FilGoods> queryWrapper = QueryGenerator.initQueryWrapper(filGoods, req.getParameterMap());
		Page<FilGoods> page = new Page<FilGoods>(pageNo, pageSize);
		IPage<FilGoods> pageList = filGoodsService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param filGoods
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FilGoods filGoods) {
		filGoodsService.save(filGoods);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param filGoods
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FilGoods filGoods) {
		filGoodsService.updateById(filGoods);
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
		filGoodsService.removeById(id);
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
		this.filGoodsService.removeByIds(Arrays.asList(ids.split(",")));
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
		FilGoods filGoods = filGoodsService.getById(id);
		if(filGoods==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(filGoods);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param filGoods
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FilGoods filGoods) {
        return super.exportXls(request, filGoods, FilGoods.class, "fil_goods");
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
        return super.importExcel(request, response, FilGoods.class);
    }

}
