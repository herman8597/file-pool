package network.vena.cooperation.adminApi.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import io.swagger.annotations.ApiOperation;
import network.vena.cooperation.adminApi.dto.BalanceDTO;
import network.vena.cooperation.adminApi.entity.*;
import network.vena.cooperation.adminApi.mapper.AuthUserMapper;
import network.vena.cooperation.adminApi.service.IBalanceModifyPipelineService;
import network.vena.cooperation.adminApi.service.IBalanceService;
import network.vena.cooperation.adminApi.service.impl.AuthUserServiceImpl;
import network.vena.cooperation.adminApi.service.impl.BalanceServiceImpl;
import network.vena.cooperation.adminApi.service.impl.ReplenishmentRecordServiceImpl;
import network.vena.cooperation.adminApi.service.impl.WeightServiceImpl;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import network.vena.cooperation.adminApi.service.IReplenishmentRecordService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 补单记录
 * @Author: jeecg-boot
 * @Date: 2020-04-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/replenishmentRecord")
@Slf4j
public class ReplenishmentRecordController extends JeecgController<ReplenishmentRecord, IReplenishmentRecordService> {
    @Autowired
    private ReplenishmentRecordServiceImpl replenishmentRecordService;

    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
	private WeightServiceImpl weightService;

    @Autowired
    private IBalanceService balanceService;

    @Autowired
    private AuthUserServiceImpl authUserService;

    @Autowired
    private IBalanceModifyPipelineService balanceModifyPipelineService;


    /**
     * 分页列表查询
     *
     * @param replenishmentRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog("补单管理-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ReplenishmentRecord replenishmentRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<ReplenishmentRecord> pageList = getReplenishmentRecordIPage(replenishmentRecord, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    private IPage<ReplenishmentRecord> getReplenishmentRecordIPage(ReplenishmentRecord replenishmentRecord, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        String account = replenishmentRecord.getAccount();
        if (StringUtils.isNotBlank(account)) {
            replenishmentRecord.setAccount(null);
        }
        QueryWrapper<ReplenishmentRecord> queryWrapper = QueryGenerator.initQueryWrapper(replenishmentRecord, req.getParameterMap());
        if (StringUtils.isNotBlank(account)) {
            queryWrapper.inSql("api_key", "SELECT api_key FROM `auth_user` WHERE account= '" + account+"'");
        }

        Page<ReplenishmentRecord> page = new Page<ReplenishmentRecord>(pageNo, pageSize);
        IPage<ReplenishmentRecord> pageList = replenishmentRecordService.page(page, queryWrapper);
        for (ReplenishmentRecord record : pageList.getRecords()) {
            record.setAccount(authUserMapper.getAccount(record.getApiKey()));
        }
        return pageList;
    }

    //004的补单
    @PostMapping(value = "/add")
        @ApiOperation("添加补单")
        public Result<?> add (@RequestBody ReplenishmentRecord replenishmentRecord){
            replenishmentRecordService.save(replenishmentRecord);
            return Result.ok("添加成功！");
        }

    /**
     * 011的补单
     *
     * @param replenishmentRecord
     * @return
     */
  /*  @PostMapping(value = "/add")
    @ApiOperation("添加补单")
    public Result<?> add(@RequestBody ReplenishmentRecord replenishmentRecord) {
        //获取用户账号，通过帐号去获取apikey
        String account = replenishmentRecord.getAccount();
        AuthUser authUser = authUserService.lambdaQuery().eq(AuthUser::getPhone, account).or().eq(AuthUser::getEmail,account).one();
        if (ObjectUtils.isEmpty(authUser)) {
            throw new RuntimeException("账号不存在");
        }else{
            String apiKey = authUser.getApiKey();
            Balance balance = balanceService.lambdaQuery().eq(Balance::getApiKey, apiKey).eq(Balance::getAsset,replenishmentRecord.getAsset()).one();
        if (balance!=null){
            //可用余额
            BigDecimal available = new BigDecimal(balance.getAvailable().toString());
            if (available.compareTo(BigDecimal.ZERO)==1){
                //==1说明余额中有钱
                *//*if (available.compareTo(replenishmentRecord.getPledgeQuantity())==-1){
                    return Result.error("余额中的金额小于需要质押金额");
                } *//*
                //从余额中减出的质押数量与gas费用
                if (available.compareTo(BigDecimalUtil.add(replenishmentRecord.getPledgeQuantity(),replenishmentRecord.getGasCost()))==-1){
                    return Result.error("余额中的金额小于需要质押金额与GAS费用");
                }else{
                    //计算需要修改的新余额（余额-（质押金额+GAS费用））
                    BigDecimal newAvai = BigDecimalUtil.sub(available, BigDecimalUtil.add(replenishmentRecord.getPledgeQuantity(),replenishmentRecord.getGasCost()));
                    //减余额
                    balanceService.lambdaUpdate().set(Balance::getAvailable,newAvai).eq(Balance::getApiKey,apiKey).eq(Balance::getAsset,replenishmentRecord.getAsset()).update();
                    //增质押金额与gas费用
                    replenishmentRecordService.save(replenishmentRecord);

                    //资产变动记录
                        //质押资金变动
                    BalanceModifyPipeline balanceModifyPipeline = new BalanceModifyPipeline();
                    balanceModifyPipeline.setApiKey(apiKey);
                    balanceModifyPipeline.setAsset(replenishmentRecord.getAsset());
                    balanceModifyPipeline.setQuantity(replenishmentRecord.getPledgeQuantity());
                    balanceModifyPipeline.setType(44);
                    balanceModifyPipeline.setCreateTime(new Date());
                    balanceModifyPipeline.setStatus(1);
                    balanceModifyPipeline.setRemark("质押货币");
                    balanceModifyPipeline.setChargeAsset(replenishmentRecord.getAsset());
                    balanceModifyPipelineService.save(balanceModifyPipeline);
                        //GAS
                    BalanceModifyPipeline balanceModifyPipeline1 = new BalanceModifyPipeline();
                    balanceModifyPipeline1.setApiKey(apiKey);
                    balanceModifyPipeline1.setAsset(replenishmentRecord.getAsset());
                    balanceModifyPipeline1.setQuantity(replenishmentRecord.getGasCost());
                    balanceModifyPipeline1.setType(45);
                    balanceModifyPipeline1.setCreateTime(new Date());
                    balanceModifyPipeline1.setStatus(1);
                    balanceModifyPipeline1.setRemark("GAS");
                    balanceModifyPipeline1.setChargeAsset(replenishmentRecord.getAsset());
                    balanceModifyPipelineService.save(balanceModifyPipeline1);
                    return Result.ok("新增成功");
                }
            }else{
                return Result.error("余额不足，请前往充值");
            }
        }else{
            return Result.error("该用户的钱包信息不存在");
        }
        }
    }*/


    /**
     * 审核
     *
     * @param replenishmentRecord
     * @return
     */
    @ApiOperation("审核")
    @PutMapping(value = "/edit")
    public Result<?> audit(@RequestBody ReplenishmentRecord replenishmentRecord) {
        return replenishmentRecordService.audit(replenishmentRecord);
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        replenishmentRecordService.removeById(id);
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
        this.replenishmentRecordService.removeByIds(Arrays.asList(ids.split(",")));
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
        ReplenishmentRecord replenishmentRecord = replenishmentRecordService.getById(id);
        if (replenishmentRecord == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(replenishmentRecord);
    }

    /**
     * 导出excel
     *
     * @param replenishmentRecord
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(ReplenishmentRecord replenishmentRecord,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest req) {
        IPage<ReplenishmentRecord> pageList = getReplenishmentRecordIPage(replenishmentRecord, pageNo, pageSize, req);
        return exportXls(req, pageList.getRecords(), ReplenishmentRecord.class, "补单记录");
    }

    protected ModelAndView exportXls(HttpServletRequest request, List<ReplenishmentRecord> pageList, Class<ReplenishmentRecord> clazz, String title) {
        // Step.1 组装查询条件
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<ReplenishmentRecord> exportList = null;

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
    private String getId(ReplenishmentRecord item) {
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
        return super.importExcel(request, response, ReplenishmentRecord.class);
    }

}
