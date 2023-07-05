package network.vena.cooperation.adminApi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import network.vena.cooperation.adminApi.entity.UserIdInfo;
import network.vena.cooperation.adminApi.service.IUserIdInfoService;
import network.vena.cooperation.adminApi.service.impl.AuthUserServiceImpl;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: user_id_info
 * @Author: jeecg-boot
 * @Date:   2020-07-01
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/userIdInfo")
@Slf4j
public class UserIdInfoController extends JeecgController<UserIdInfo, IUserIdInfoService> {
	@Autowired
	private IUserIdInfoService userIdInfoService;
	@Autowired
	private SysUserServiceImpl sysUserService;
	@Autowired
	private AuthUserServiceImpl authUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param userIdInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(UserIdInfo userIdInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<UserIdInfo> queryWrapper = QueryGenerator.initQueryWrapper(userIdInfo, req.getParameterMap());
		Page<UserIdInfo> page = new Page<UserIdInfo>(pageNo, pageSize);
		IPage<UserIdInfo> pageList = userIdInfoService.page(page, queryWrapper);
		for (UserIdInfo record : pageList.getRecords()) {
			if (!ObjectUtils.isEmpty(record.getStaffId())) {
				SysUser one = sysUserService.lambdaQuery().eq(SysUser::getId, record.getStaffId()).one();
				if (!ObjectUtils.isEmpty(one)) {
					record.setStaff(one.getRealname());
				}
			}
			if (!ObjectUtils.isEmpty(record.getApiKey())) {
				String account = authUserService.getBaseMapper().getAccount(record.getApiKey());
				if (StringUtils.isNotBlank(account)) {
					record.setAccount(account);
				}
			}
		}
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param userIdInfo
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody UserIdInfo userIdInfo) {
		userIdInfoService.save(userIdInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param userIdInfo
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody UserIdInfo userIdInfo) {
		return userIdInfoService.audit(userIdInfo);
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		userIdInfoService.removeById(id);
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
		this.userIdInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	//创建仓库
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		UserIdInfo userIdInfo = userIdInfoService.getById(id);
		if(userIdInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(userIdInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param userIdInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, UserIdInfo userIdInfo) {
        return super.exportXls(request, userIdInfo, UserIdInfo.class, "user_id_info");
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
        return super.importExcel(request, response, UserIdInfo.class);
    }

}
