package network.vena.cooperation.adminApi.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import network.vena.cooperation.adminApi.dto.WeightDTO;
import network.vena.cooperation.adminApi.entity.AuthUser;
import network.vena.cooperation.adminApi.entity.DistributionConfig;
import network.vena.cooperation.adminApi.mapper.AuthUserMapper;
import network.vena.cooperation.adminApi.mapper.DistributionConfigMapper;
import network.vena.cooperation.adminApi.service.IDistributionConfigService;
import network.vena.cooperation.common.constant.PojoConstants;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import network.vena.cooperation.adminApi.entity.Weight;
import network.vena.cooperation.adminApi.service.IWeightService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: weight
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/weight")
@Slf4j
public class WeightController extends JeecgController<Weight, IWeightService> {
    @Autowired
    private IWeightService weightService;

    @Autowired
    private IDistributionConfigService iDistributionConfigService;

    @Autowired
    private DistributionConfigMapper distributionConfigMapper;

    @Autowired
    private AuthUserMapper authUserMapper;


    /**
     * 分页列表查询
     *
     * @param weight
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog("算力订单-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Weight weight,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<Weight> pageList = getWeightIPage(weight, pageNo, pageSize, req, null);
        return Result.ok(pageList);
    }

    private IPage<Weight> getWeightIPage(Weight weight, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req, String selections) {
        String account = weight.getAccount();
        if (StringUtils.isNotBlank(account)) {
            weight.setAccount(null);
        }
        String inviter = weight.getInviter();
        if (StringUtils.isNotBlank(inviter)) {
            weight.setInviter(null);
        }
        QueryWrapper<Weight> queryWrapper = QueryGenerator.initQueryWrapper(weight, req.getParameterMap());
        if (StringUtils.isNotBlank(account)) {
            SqlInjectionUtil.filterContent(account);
            queryWrapper.inSql("api_key", " SELECT api_key FROM auth_user WHERE account='" + account + "'");
        }
        if (StringUtils.isNotBlank(inviter)) {
            SqlInjectionUtil.filterContent(inviter);
            queryWrapper.inSql("api_key", " SELECT aip.api_key FROM auth_user as au,auth_invited_pipeline as aip WHERE au.account='" + inviter + "' AND au.api_key=aip.invite_api_key");
        }
        if (StringUtils.isNotBlank(selections)) {
            queryWrapper.in("id", Arrays.asList(selections.split(",")));
        }
        queryWrapper.notIn("`type`", 2, 4, 5, 6, 8, 9, 10);
        Page<Weight> page = new Page<Weight>(pageNo, pageSize);
        IPage<Weight> pageList = weightService.page(page, queryWrapper);
        for (Weight record : pageList.getRecords()) {
            replenish(record);
        }
        return pageList;
    }

    private void replenish(Weight record) {
        if (7 == record.getType() && BigDecimalUtil.equal(record.getServiceChargeRate(), 0.149)) {
            record.setType(71);//算力保底
        } else if (7 == record.getType() && BigDecimalUtil.equal(record.getServiceChargeRate(), 0.2)) {
            record.setType(72);//算力不保底
        } else if (1 == record.getType() && BigDecimalUtil.equal(record.getServiceChargeRate(), 0.149)) {
            record.setType(11);//算力保底
        } else if (1 == record.getType() && BigDecimalUtil.equal(record.getServiceChargeRate(), 0.2)) {
            record.setType(12);//算力不保底
        }
        String inviterAccount = authUserMapper.getInviterAccount(record.getApiKey());
        record.setInviter(inviterAccount);
        AuthUser accountAndPhone = authUserMapper.getAccountAndPhoneAndDefaultAccountNo(record.getApiKey());
        if (ObjectUtils.isEmpty(accountAndPhone)) {
            return;
        }
        if (StringUtils.isNotBlank(accountAndPhone.getAccount())) {
            record.setAccount(accountAndPhone.getAccount());
        }
        if (StringUtils.isNotBlank(accountAndPhone.getPhone())) {
            record.setPhone(accountAndPhone.getPhone());
        }
        if (StringUtils.isNotBlank(accountAndPhone.getDefaultAccountNo())) {
            record.setUsername(accountAndPhone.getDefaultAccountNo());
        }
        if ("GB".equals(record.getUnit())) {
            record.setQuantity(BigDecimalUtil.divide(record.getQuantity(), 1000));
        }
    }


    /**
     * 添加
     *
     * @param weight
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Weight weight) {
        weightService.save(weight);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param
     * @return
     */
    @AutoLog("算力-操作算力")
    @PostMapping(value = "/exchangeWeight")
    @ApiOperation(value = "操作算力")
    public Result<?> exchangeWeight(@RequestBody WeightDTO weightDTO) {
/*        Set<String> value5ByKey = distributionConfigMapper.getConfByKey(PojoConstants.config.EARNINGS)
                .stream().map(DistributionConfig::getValue5).collect(Collectors.toSet());
        if (!value5ByKey.contains(weightDTO.getType().toString())) {
            return Result.error("参数有误");
        }*/
        try {
            weightService.exchangeWeight(weightDTO);
            return Result.ok("操作成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ok("操作失败!");
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        weightService.removeById(id);
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
        this.weightService.removeByIds(Arrays.asList(ids.split(",")));
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
        Weight weight = weightService.getById(id);
        if (weight == null) {
            return Result.error("未找到对应数据");
        }
        replenish(weight);
        return Result.ok(weight);
    }

    /**
     * 修改金额
     *
     * @return
     */
    @AutoLog("算力订单-修改金额")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Weight weight) {
        try {
            weightService.edit(weight);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("操作失败");
        }
    }

    /**
     * 导出excel
     *
     * @param request
     * @param weight
     */
    @RequestMapping(value = "/exportXls")
    @AutoLog("算力订单-导出excel")
    public ModelAndView exportXls(Weight weight,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest request) {
        String selections = request.getParameter("selections");
        IPage<Weight> pageList = getWeightIPage(weight, pageNo, pageSize, request, selections);
//        ModelAndView exportXls = exportXls(request, pageList.getRecords(), Weight.class, "weight");
//        return exportXls;
        return super.exportXlsByList(request,pageList.getRecords(),Weight.class,"weight");
    }

    private ModelAndView exportXls(HttpServletRequest request, List<Weight> pageList, Class<Weight> clazz, String title) {
        // Step.1 组装查询条件
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
/*        QueryWrapper<T> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<T> pageList = service.list(queryWrapper);*/
        List<Weight> exportList = pageList;

        // 过滤选中数据

       /* if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }*/

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
    private String getId(Weight item) {
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
        return super.importExcel(request, response, Weight.class);
    }

}
