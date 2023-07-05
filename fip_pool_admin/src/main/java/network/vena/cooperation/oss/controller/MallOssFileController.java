package network.vena.cooperation.oss.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import network.vena.cooperation.oss.entity.MallOssFile;
import network.vena.cooperation.oss.service.IMallOssFileService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 文件资源管理
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/oss/mallOssFile")
@Slf4j
public class MallOssFileController extends JeecgController<MallOssFile, IMallOssFileService> {
	@Autowired
	private IMallOssFileService mallOssFileService;
	
	/**
	 * 分页列表查询
	 *
	 * @param mallOssFile
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MallOssFile mallOssFile,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MallOssFile> queryWrapper = QueryGenerator.initQueryWrapper(mallOssFile, req.getParameterMap());
		Page<MallOssFile> page = new Page<MallOssFile>(pageNo, pageSize);
		IPage<MallOssFile> pageList = mallOssFileService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param mallOssFile
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MallOssFile mallOssFile) {
		mallOssFileService.save(mallOssFile);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param mallOssFile
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody MallOssFile mallOssFile) {
		mallOssFileService.updateById(mallOssFile);
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
		mallOssFileService.removeById(id);
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
		this.mallOssFileService.removeByIds(Arrays.asList(ids.split(",")));
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
		MallOssFile mallOssFile = mallOssFileService.getById(id);
		if(mallOssFile==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(mallOssFile);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param mallOssFile
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MallOssFile mallOssFile) {
        return super.exportXls(request, mallOssFile, MallOssFile.class, "文件资源管理");
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
        return super.importExcel(request, response, MallOssFile.class);
    }

}
