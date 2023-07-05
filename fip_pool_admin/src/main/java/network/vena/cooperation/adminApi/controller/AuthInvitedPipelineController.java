package network.vena.cooperation.adminApi.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import network.vena.cooperation.adminApi.entity.BalanceModifyPipeline;
import network.vena.cooperation.adminApi.mapper.*;
import network.vena.cooperation.adminApi.param.QueryParam;
import network.vena.cooperation.adminApi.service.impl.DistributionConfigServiceImpl;
import network.vena.cooperation.adminApi.service.impl.WeightServiceImpl;
import network.vena.cooperation.adminApi.vo.BalanceModifyPipelineRemarkVo;
import network.vena.cooperation.base.vo.InviteDetailVO;
import network.vena.cooperation.util.BeanUtils;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.AuthInvitedPipeline;
import network.vena.cooperation.adminApi.service.IAuthInvitedPipelineService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: auth_invited_pipeline
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/authInvitedPipeline")
@Slf4j
public class AuthInvitedPipelineController extends JeecgController<AuthInvitedPipeline, IAuthInvitedPipelineService> {
    @Autowired
    private IAuthInvitedPipelineService authInvitedPipelineService;

    @Autowired
    private AuthInvitedPipelineMapper authInvitedPipelineMapper;

    @Autowired
    private DistributionDetailMapper distributionDetailMapper;

    @Autowired
    private DistributionConfigServiceImpl distributionConfigService;

    @Autowired
    private WeightServiceImpl weightService;

    @Autowired
    private BalanceModifyPipelineMapper balanceModifyPipelineMapper;

    @Autowired
    private WeightMapper weightMapper;


    /**
     * 分页列表查询
     *
     * @param authInvitedPipeline
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "邀请关系-查询列表")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AuthInvitedPipeline authInvitedPipeline,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<AuthInvitedPipeline> pageList = getAuthInvitedPipelineIPage(authInvitedPipeline, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    private IPage<AuthInvitedPipeline> getAuthInvitedPipelineIPage(AuthInvitedPipeline authInvitedPipeline, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        String account = authInvitedPipeline.getAccount();
        if (StringUtils.isNotBlank(account)) {
            authInvitedPipeline.setAccount(null);
        }
        String inviter = authInvitedPipeline.getInviter();
        if (StringUtils.isNotBlank(inviter)) {
            authInvitedPipeline.setInviter(null);
        }
        Integer queryLevle = authInvitedPipeline.getLevel();
        if (!ObjectUtils.isEmpty(queryLevle)) {
            authInvitedPipeline.setLevel(null);
        }


        QueryWrapper<AuthInvitedPipeline> queryWrapper = QueryGenerator.initQueryWrapper(authInvitedPipeline, req.getParameterMap());
        Page<AuthInvitedPipeline> page = new Page<AuthInvitedPipeline>(pageNo, pageSize);
        if (StringUtils.isNotBlank(account)) {
            SqlInjectionUtil.filterContent(account);
            queryWrapper.inSql("api_key", "SELECT api_key FROM auth_user WHERE account= '" + account + "'");
        }
        if (StringUtils.isNotBlank(inviter)) {
            SqlInjectionUtil.filterContent(inviter);
            queryWrapper.inSql("invite_api_key", "SELECT api_key FROM auth_user WHERE account= '" + inviter + "'");
        }

        if (!ObjectUtils.isEmpty(queryLevle) && StringUtils.isNotBlank(inviter)) {
            SqlInjectionUtil.filterContent(queryLevle.toString());
            queryWrapper.inSql("invite_api_key", "select api_key FROM (SELECT MAX(A.`level`) AS `level`,api_key " +
                    "FROM (SELECT `level`,api_key FROM distribution_detail  UNION ALL SELECT `level_direct`,api_key FROM distribution_cheat  ) " +
                    "AS A GROUP BY api_key ) as CC WHERE CC.`level`='" + queryLevle + "'");
        } else if (!ObjectUtils.isEmpty(queryLevle)) {
            SqlInjectionUtil.filterContent(queryLevle.toString());
            queryWrapper.inSql("api_key", "select api_key FROM (SELECT MAX(A.`level`) AS `level`,api_key " +
                    "FROM (SELECT `level`,api_key FROM distribution_detail  UNION ALL SELECT `level_direct`,api_key FROM distribution_cheat  ) " +
                    "AS A GROUP BY api_key ) as CC WHERE CC.`level`='" + queryLevle + "'");
        }


        IPage<AuthInvitedPipeline> pageList = authInvitedPipelineService.page(page, queryWrapper);
        for (AuthInvitedPipeline record : pageList.getRecords()) {
            AuthInvitedPipeline result = authInvitedPipelineMapper.getAccount$code$inviteer$inviteCount$award$level(record.getApiKey());
            if (!ObjectUtils.isEmpty(result)) {
                BeanUtils.copyNewPropertites(result, record);
            }


            BigDecimal award = balanceModifyPipelineMapper.sumRewardsDistribution(record.getApiKey());


            List<String> oneApiKeys = authInvitedPipelineService.lambdaQuery().select(AuthInvitedPipeline::getApiKey)
                    .eq(AuthInvitedPipeline::getInviteApiKey, record.getApiKey()).list()
                    .stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());

            BigDecimal one = BigDecimal.ZERO;
            BigDecimal XchOne = BigDecimal.ZERO;

            BigDecimal two = BigDecimal.ZERO;
            BigDecimal XchTwo = BigDecimal.ZERO;


            if (!ObjectUtils.isEmpty(oneApiKeys)) {
                //统计fil一级分销
                one = BigDecimalUtil.divide(weightMapper.sumQuantityInApiKeysFil(oneApiKeys), 1000);
                XchOne = BigDecimalUtil.divide(weightMapper.sumQuantityInApiKeysXch(oneApiKeys), 1000);
                List<String> twoApiKeys = authInvitedPipelineService.lambdaQuery().select(AuthInvitedPipeline::getApiKey).in(AuthInvitedPipeline::getInviteApiKey, oneApiKeys).list()
                        .stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
                if (!ObjectUtils.isEmpty(twoApiKeys)) {
                //统计fil二级分销
                    two = BigDecimalUtil.divide(weightMapper.sumQuantityInApiKeysFil(twoApiKeys), 1000);
                    XchTwo = BigDecimalUtil.divide(weightMapper.sumQuantityInApiKeysXch(twoApiKeys), 1000);
                }
            }
            /*List<BalanceModifyPipeline> rebateRecord = balanceModifyPipelineMapper.getRebateRecord(record.getApiKey());

            for (BalanceModifyPipeline balanceModifyPipeline : rebateRecord) {
                if (!ObjectUtils.isEmpty(balanceModifyPipeline.getRemark())) {
                    BalanceModifyPipelineRemarkVo balanceModifyPipelineRemarkVo = JSONObject.parseObject(balanceModifyPipeline.getRemark(), BalanceModifyPipelineRemarkVo.class);
                    BigDecimal divide = BigDecimalUtil.divide(balanceModifyPipelineRemarkVo.getOrderQuantity(), 1000);
                    if (StringUtils.equals(balanceModifyPipelineRemarkVo.getRound(), "1")) {
                        one = BigDecimalUtil.add(one, divide);
                    } else if (StringUtils.equals(balanceModifyPipelineRemarkVo.getRound(), "2")) {
                        two = BigDecimalUtil.add(two,divide);
                    }
                }
            }*/


            record.setAward(award).setHashrateTotal(BigDecimalUtil.add(one, two)).setChildrenPurchase(one).setGrandchildrenPurchase(two).setChildrenPurchaseXch(XchOne).setGrandchildrenPurchaseXch(XchTwo).setHashrateTotalXch(BigDecimalUtil.add(XchOne,XchTwo));
            if (ObjectUtils.isEmpty(record.getLevel())) {
                Integer level = distributionConfigService.getLevel(record.getApiKey());
                record.setLevel(level);
            }
        }
        return pageList;
    }


    /**
     * 邀请好友列表
     *
     *  001项目
     * @return
     */
    @AutoLog(value = "邀请关系-邀请好友列表")
    @GetMapping(value = "/inviteDetailByApiKey")
    @ApiOperation("邀请好友列表")
    public Result<?> inviteDetailByApiKey(@RequestParam(value = "relation", required = false) Integer relation, @RequestParam("account") String account,
                                          @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                          HttpServletRequest request
    ) {
        QueryParam queryParam = new QueryParam().setRelation(relation).setAccount(account).setPageNo(pageNo).setPageSize(pageSize);
        return Result.ok(authInvitedPipelineService.inviteDetailByApiKeyPage(queryParam));
    }

    /**
     * 邀请好友列表
     * 001项目
     *
     * @return
     */
    @AutoLog(value = "邀请关系-导出excel")
    @GetMapping(value = "/exportXlsInviteDetailByApiKey")
    @ApiOperation("导出邀请好友列表")
    public ModelAndView exportXlsInviteDetailByApiKey(@RequestParam(value = "relation", required = false) Integer relation, @RequestParam("account") String account,
                                                      @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                                      HttpServletRequest request
    ) {
        QueryParam queryParam = new QueryParam().setRelation(relation).setAccount(account).setPageNo(pageNo).setPageSize(pageSize);
        List<InviteDetailVO> exportList = authInvitedPipelineService.inviteDetailByApiKeyPage(queryParam).getRecords();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "邀请好友列表"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, InviteDetailVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("邀请好友列表" + "报表", "导出人:" + sysUser.getRealname(), "邀请好友列表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }



    /**
     * 邀请好友列表
     * 002项目
     * @return
     */
    @AutoLog(value = "邀请关系-邀请好友列表")
    @GetMapping(value = "/inviteDetail")
    @ApiOperation("邀请好友列表")
    public Result<?> inviteDetail(@RequestParam(value = "relation", required = false) Integer relation, @RequestParam("account") String account,
                                  @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize
    ) {
        QueryParam queryParam = new QueryParam().setFixRelation(relation).setAccount(account).setPageNo(pageNo).setPageSize(pageSize);
        return Result.ok(authInvitedPipelineService.inviteDetailByApiKey(queryParam));
    }

    /**
     * 邀请好友列表
     * 002项目
     *
     * @return
     */
    @AutoLog(value = "邀请好友明细-导出excel")
    @GetMapping(value = "/exportXlsinviteDetail")
    @ApiOperation("邀请好友明细")
    public ModelAndView exportXlsinviteDetail(@RequestParam(value = "relation", required = false) Integer relation, @RequestParam("account") String account,
                                              @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize
    ) {
        QueryParam queryParam = new QueryParam().setFixRelation(relation).setAccount(account).setPageNo(pageNo).setPageSize(pageSize);
        List<InviteDetailVO> exportList = authInvitedPipelineService.inviteDetailByApiKey(queryParam).getInviteDetailVOS();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "邀请好友明细"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, InviteDetailVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("邀请好友明细" + "报表", "导出人:" + sysUser.getRealname(), "邀请好友明细"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }



    /**
     * 添加
     *
     * @param authInvitedPipeline
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AuthInvitedPipeline authInvitedPipeline) {
        authInvitedPipelineService.save(authInvitedPipeline);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param authInvitedPipeline
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AuthInvitedPipeline authInvitedPipeline) {
        authInvitedPipelineService.updateById(authInvitedPipeline);
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
        authInvitedPipelineService.removeById(id);
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
        this.authInvitedPipelineService.removeByIds(Arrays.asList(ids.split(",")));
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
        AuthInvitedPipeline authInvitedPipeline = authInvitedPipelineService.getById(id);
        if (authInvitedPipeline == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(authInvitedPipeline);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param authInvitedPipeline
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(AuthInvitedPipeline authInvitedPipeline,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest request) {
        IPage<AuthInvitedPipeline> pageList = getAuthInvitedPipelineIPage(authInvitedPipeline, pageNo, pageSize, request);
        return exportXls(request, pageList.getRecords(), AuthInvitedPipeline.class, "auth_invited_pipeline");
    }

    /**
     * 导出excel
     *
     * @param request
     */
    protected ModelAndView exportXls(HttpServletRequest request, List<AuthInvitedPipeline> pageList, Class<AuthInvitedPipeline> clazz, String title) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
      /*  // Step.1 组装查询条件
        QueryWrapper<T> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());

        // Step.2 获取导出数据
        List<T> pageList = service.list(queryWrapper);*/
        List<AuthInvitedPipeline> exportList = null;

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
    private String getId(AuthInvitedPipeline item) {
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
        return super.importExcel(request, response, AuthInvitedPipeline.class);
    }

}
