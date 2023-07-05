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

import network.vena.cooperation.adminApi.entity.AuthUser;
import network.vena.cooperation.adminApi.mapper.AuthUserMapper;
import network.vena.cooperation.adminApi.service.impl.AuthUserServiceImpl;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.GcDeposit;
import network.vena.cooperation.adminApi.service.IGcDepositService;

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
 * @Description: gc_deposit
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/gcDeposit")
@Slf4j
public class GcDepositController extends JeecgController<GcDeposit, IGcDepositService> {
    @Autowired
    private IGcDepositService gcDepositService;

    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private AuthUserServiceImpl authUserService;

    /**
     * 分页列表查询
     *
     * @param gcDeposit
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GcDeposit gcDeposit,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<GcDeposit> pageList = getGcDepositIPage(gcDeposit, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    private IPage<GcDeposit> getGcDepositIPage(GcDeposit gcDeposit, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        String queryAccount = gcDeposit.getAccount();
        gcDeposit.setAccount(null);
        QueryWrapper<GcDeposit> queryWrapper = QueryGenerator.initQueryWrapper(gcDeposit, req.getParameterMap());
        if (StringUtils.isNotBlank(queryAccount)) {
            queryWrapper.inSql("api_key", "SELECT api_key FROM `auth_user` WHERE account= '" + queryAccount+"'");
        }
        Page<GcDeposit> page = new Page<GcDeposit>(pageNo, pageSize);
        if (!StringUtils.isEmpty(gcDeposit.getAccount())) {
            String apiKey = authUserService.lambdaQuery().select(AuthUser::getApiKey).eq(AuthUser::getAccount, gcDeposit.getAccount()).one().getApiKey();
            queryWrapper.eq("api_key", apiKey);
        }
        IPage<GcDeposit> pageList = gcDepositService.page(page, queryWrapper);
        for (GcDeposit record : pageList.getRecords()) {
            String account = authUserMapper.getAccount(record.getApiKey());
            record.setAccount(account);
        }
        return pageList;
    }

    /**
     * 添加
     *
     * @param gcDeposit
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GcDeposit gcDeposit) {
        gcDepositService.save(gcDeposit);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gcDeposit
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GcDeposit gcDeposit) {
        gcDepositService.updateById(gcDeposit);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        gcDepositService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gcDepositService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GcDeposit gcDeposit = gcDepositService.getById(id);
        if (gcDeposit == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gcDeposit);
    }

    /**
     * 导出excel
     *
     * @param gcDeposit
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(GcDeposit gcDeposit,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest req) {
        IPage<GcDeposit> pageList = getGcDepositIPage(gcDeposit, pageNo, pageSize, req);

        return exportXls(req, pageList.getRecords(), GcDeposit.class, "gc_deposit");
    }

    protected ModelAndView exportXls(HttpServletRequest request, List<GcDeposit> pageList, Class<GcDeposit> clazz, String title) {
        // Step.1 组装查询条件
        /*QueryWrapper<T> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());*/
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        //List<GcDeposit> pageList = service.list(queryWrapper);
        List<GcDeposit> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }


    /**
     * 获取对象ID
     *
     * @return
     */
    private String getId(GcDeposit item) {
        try {
            return PropertyUtils.getProperty(item, "id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        return super.importExcel(request, response, GcDeposit.class);
    }

}
