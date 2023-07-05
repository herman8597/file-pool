package network.vena.cooperation.adminApi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import network.vena.cooperation.adminApi.entity.OperatingIncome;
import network.vena.cooperation.adminApi.mapper.AuthUserMapper;
import network.vena.cooperation.adminApi.service.IOperatingIncomeService;
import network.vena.cooperation.util.BeanUtils;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: operating_income
 * @Author: jeecg-boot
 * @Date: 2020-05-20
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/operatingIncome")
@Slf4j
public class OperatingIncomeController extends JeecgController<OperatingIncome, IOperatingIncomeService> {
    @Autowired
    private IOperatingIncomeService operatingIncomeService;

    @Autowired
    private AuthUserMapper authUserMapper;

    /**
     * 分页列表查询
     *
     * @param operatingIncome
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OperatingIncome operatingIncome,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<OperatingIncome> pageList = getOperatingIncomeIPage(operatingIncome, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    IPage<OperatingIncome> getOperatingIncomeIPage(OperatingIncome operatingIncome, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        String account = operatingIncome.getAccount();
        String subordinateAccount = operatingIncome.getSubordinateAccount();
        String originAccount = operatingIncome.getOriginAccount();
        if (StringUtils.isNotBlank(account)) {
            operatingIncome.setAccount(null);
        }
        if (StringUtils.isNotBlank(subordinateAccount)) {
            operatingIncome.setSubordinateAccount(null);
        }
        if (StringUtils.isNotBlank(originAccount)) {
            operatingIncome.setOriginAccount(null);
        }

        QueryWrapper<OperatingIncome> queryWrapper = QueryGenerator.initQueryWrapper(operatingIncome, req.getParameterMap());
        if (StringUtils.isNotBlank(account)) {
            SqlInjectionUtil.filterContent(account);
            queryWrapper.inSql("api_key", "select api_key from auth_user where account='" + account + "'");
        }
        if (StringUtils.isNotBlank(subordinateAccount)) {
            SqlInjectionUtil.filterContent(subordinateAccount);
            queryWrapper.inSql("subordinate", "select api_key from auth_user where account='" + subordinateAccount + "'");
        }
        if (StringUtils.isNotBlank(originAccount)) {
            SqlInjectionUtil.filterContent(originAccount);
            queryWrapper.inSql("origin_api_key", "select api_key from auth_user where account='" + originAccount + "'");
        }
        queryWrapper.lambda().orderByDesc(OperatingIncome::getAsset);
        queryWrapper.orderByAsc("relation");
        Page<OperatingIncome> page = new Page<OperatingIncome>(pageNo, pageSize);
        IPage<OperatingIncome> pageList = operatingIncomeService.page(page, queryWrapper);
        for (OperatingIncome record : pageList.getRecords()) {
            OperatingIncome result = authUserMapper.getAccount$SubordinateAccount$OriginAccount(record.getApiKey(), record.getSubordinate(), record.getOriginApiKey());
            if (!ObjectUtils.isEmpty(result)) {
                BeanUtils.copyNewPropertites(result, record);
            }
        }
        return pageList;
    }

    /**
     * 添加
     *
     * @param operatingIncome
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OperatingIncome operatingIncome) {
        operatingIncomeService.save(operatingIncome);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param operatingIncome
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OperatingIncome operatingIncome) {
        operatingIncomeService.updateById(operatingIncome);
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
        operatingIncomeService.removeById(id);
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
        this.operatingIncomeService.removeByIds(Arrays.asList(ids.split(",")));
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
        OperatingIncome operatingIncome = operatingIncomeService.getById(id);
        if (operatingIncome == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(operatingIncome);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param operatingIncome
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(OperatingIncome operatingIncome,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest request) {
        IPage<OperatingIncome> pageList = getOperatingIncomeIPage(operatingIncome, pageNo, pageSize, request);
        return exportXls(request, pageList.getRecords(), OperatingIncome.class, "operating_income");
    }

    private ModelAndView exportXls(HttpServletRequest request, List<OperatingIncome> pageList, Class<OperatingIncome> clazz, String title) {
        // Step.1 组装查询条件
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<OperatingIncome> exportList = null;

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
    private String getId(OperatingIncome item) {
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
        return super.importExcel(request, response, OperatingIncome.class);
    }

}
