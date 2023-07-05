package network.vena.cooperation.adminApi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import network.vena.cooperation.adminApi.entity.AwardFreeze;
import network.vena.cooperation.adminApi.service.IAwardFreezeService;
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
 * @Description: award_freeze
 * @Author: jeecg-boot
 * @Date:   2020-07-17
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/awardFreeze")
@Slf4j
public class AwardFreezeController extends JeecgController<AwardFreeze, IAwardFreezeService> {
	@Autowired
	private IAwardFreezeService awardFreezeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param awardFreeze
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AwardFreeze awardFreeze,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AwardFreeze> queryWrapper = QueryGenerator.initQueryWrapper(awardFreeze, req.getParameterMap());
		Page<AwardFreeze> page = new Page<AwardFreeze>(pageNo, pageSize);
		IPage<AwardFreeze> pageList = awardFreezeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param awardFreeze
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AwardFreeze awardFreeze) {
		awardFreezeService.save(awardFreeze);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param awardFreeze
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AwardFreeze awardFreeze) {
		awardFreezeService.updateById(awardFreeze);
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
		awardFreezeService.removeById(id);
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
		this.awardFreezeService.removeByIds(Arrays.asList(ids.split(",")));
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
		AwardFreeze awardFreeze = awardFreezeService.getById(id);
		if(awardFreeze==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(awardFreeze);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param awardFreeze
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AwardFreeze awardFreeze) {
        return super.exportXls(request, awardFreeze, AwardFreeze.class, "award_freeze");
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
        return super.importExcel(request, response, AwardFreeze.class);
    }

}
