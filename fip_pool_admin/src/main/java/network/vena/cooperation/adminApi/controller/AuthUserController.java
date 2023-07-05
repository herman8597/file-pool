package network.vena.cooperation.adminApi.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import network.vena.cooperation.adminApi.entity.*;
import network.vena.cooperation.adminApi.excel.InvitedPipelineExcel;
import network.vena.cooperation.adminApi.excel.UserExcel;
import network.vena.cooperation.adminApi.mapper.AuthUserMapper;
import network.vena.cooperation.adminApi.mapper.BalanceModifyPipelineMapper;
import network.vena.cooperation.adminApi.mapper.DistributionDetailMapper;
import network.vena.cooperation.adminApi.param.AuthUserParam;
import network.vena.cooperation.adminApi.service.IAuthInvitationCodeService;
import network.vena.cooperation.adminApi.service.IAuthUserService;
import network.vena.cooperation.adminApi.service.impl.AuthInvitedPipelineServiceImpl;
import network.vena.cooperation.adminApi.service.impl.DistributionConfigServiceImpl;
import network.vena.cooperation.adminApi.service.impl.DistributionDetailServiceImpl;
import network.vena.cooperation.adminApi.service.impl.WeightServiceImpl;
import network.vena.cooperation.base.vo.AuthUserEditVo;
import network.vena.cooperation.base.vo.AuthUserVo;
import network.vena.cooperation.util.BeanUtils;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: auth_user
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/authUser")
@Slf4j
public class AuthUserController extends JeecgController<AuthUser, IAuthUserService> {
    @Autowired
    private IAuthUserService authUserService;

    @Autowired
    private DistributionConfigServiceImpl distributionConfigService;

    @Autowired
    private DistributionDetailMapper distributionDetailMapper;

    @Autowired
    private BalanceModifyPipelineMapper balanceModifyPipelineMapper;

    @Autowired
    private AuthInvitedPipelineServiceImpl authInvitedPipelineService;

    @Autowired
    private WeightServiceImpl weightService;

    @Autowired
    private IAuthInvitationCodeService authInvitationCodeService;

    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private OperatingIncomeController operatingIncomeController;


    @Autowired
    private DistributionDetailServiceImpl distributionDetailService;
    @Value("${spring.profiles.active}")
    private String active;

/*
    @AutoLog(value = "004导出用户数据")
    @PostMapping(value = "/testTwo")
    public void testTwo(HttpServletRequest req) throws IOException, WriteException {

        // 打开文件
        WritableWorkbook book = Workbook.createWorkbook(new File(
                "C:\\Users\\xstc\\Desktop\\userFour.xls"));

        WritableSheet sheet = book.createSheet("user", 0);
        Label label1 = new Label(0, 0, "用户账号");
        Label label2 = new Label(1, 0, "上级邀请人");
        Label label3 = new Label(2, 0, "经验值数量FIL");
        Label label4 = new Label(3, 0, "累计分销算力FIL");
        Label label5 = new Label(4, 0, "经验值数量XCH");
        Label label6 = new Label(5, 0, "累计分销算力XCH");
        Label label7 = new Label(6, 0, "伞下业绩（社区）");


        sheet.addCell(label1);
        sheet.addCell(label2);
        sheet.addCell(label3);
        sheet.addCell(label4);
        sheet.addCell(label5);
        sheet.addCell(label6);
        sheet.addCell(label7);

        //先查询全部的用户数据
        List<AuthUser> list = authUserService.lambdaQuery().list();

        //最终返回的list
//        List<UserExcel> listResult = new ArrayList<>();

        int i=1;

        for (AuthUser authUser:list) {
            UserExcel userExcel = new UserExcel();
            userExcel.setUserAccount(authUser.getAccount());

            //查询该用户的上级邀请人
            AuthInvitedPipeline one = authInvitedPipelineService.lambdaQuery().eq(AuthInvitedPipeline::getApiKey, authUser.getApiKey()).one();
            if (ObjectUtil.isNotEmpty(one)){
                //根据邀请人的apike获取邀请人的账号
                AuthUser two = authUserService.lambdaQuery().eq(AuthUser::getApiKey, one.getInviteApiKey()).one();
                if (ObjectUtil.isNotEmpty(two)){
                    userExcel.setInviteApiAccount(two.getAccount());
                }
            }

            //查询该用户的经验值数量FIL
            List<Weight> weightFilList = weightService.lambdaQuery()
                    .eq(Weight::getApiKey, authUser.getApiKey())
                    .eq(Weight::getStatus,1)
                    .eq(Weight::getPowerType,1)
                    .list();
            if (ObjectUtil.isNotEmpty(weightFilList)){
                BigDecimal filReduce = weightFilList.stream().map(x -> {
                    if (x.getUnit().equals("GB")) {
                        BigDecimal divide = BigDecimalUtil.divide(x.getQuantity(), 1000);
                        return divide;
                    } else {
                        return x.getQuantity();
                    }
                }).reduce(BigDecimal.ZERO, BigDecimal::add);

//                BigDecimal filReduce = weightFilList.stream().map(Weight::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                userExcel.setFilExperience(filReduce);
            }else{
                userExcel.setFilExperience(BigDecimal.ZERO);
            }

            //查询累计分销算力FIL
            AuthUserVo authUserDetail = authUserService.getAuthUserDetail(authUser.getId().toString());
            if (ObjectUtil.isNotEmpty(authUserDetail)){
                userExcel.setHashrateTotal(authUserDetail.getAwardVO().getHashrateTotal());
            }

            //查询经验值数量XCH
            List<Weight> weightXchList = weightService.lambdaQuery()
                    .eq(Weight::getApiKey, authUser.getApiKey())
                    .eq(Weight::getStatus,1)
                    .eq(Weight::getPowerType, 2)
                    .list();
            if (ObjectUtil.isNotEmpty(weightXchList)){
//                BigDecimal xchReduce = weightXchList.stream().map(Weight::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal xchReduce = weightXchList.stream().map(z -> {
                    if (z.getUnit().equals("GB")) {
                        BigDecimal divide = BigDecimalUtil.divide(z.getQuantity(), 1000);
                        return divide;
                    } else {
                        return z.getQuantity();
                    }
                }).reduce(BigDecimal.ZERO, BigDecimal::add);

                if (xchReduce.compareTo(BigDecimal.ZERO)>0){
                    userExcel.setXchExperience(xchReduce.divide(new BigDecimal(8)));
                }
                userExcel.setXchHashrateTotal(xchReduce);
            }else{
                userExcel.setXchHashrateTotal(BigDecimal.ZERO);
            }

            //伞下业绩（社区）
            OperatingIncome operatingIncome = new OperatingIncome();
            operatingIncome.setAccount(authUser.getAccount());

//            List<OperatingIncome> records = operatingIncomeController.getOperatingIncomeIPage(operatingIncome, 1, 10, req).getRecords();
//            if (ObjectUtil.isNotEmpty(records)){
//                OperatingIncome operatingIncome1 = records.get(0);
//                if (ObjectUtil.isNotEmpty(operatingIncome)){
//                    userExcel.setTotal(operatingIncome1.getTotal());
//                }
//            }
//            sheet.addCell(new Number(0, i,));
            if (ObjectUtil.isNotEmpty(userExcel.getUserAccount())){
                sheet.addCell(new Label(0, i, userExcel.getUserAccount()));
            }
            if (ObjectUtil.isNotEmpty(userExcel.getInviteApiAccount())){
                sheet.addCell(new Label(1, i, userExcel.getInviteApiAccount()));
            }
            if (ObjectUtil.isNotEmpty(userExcel.getFilExperience())){
                sheet.addCell(new Label(2, i, userExcel.getFilExperience().toString()));
            }else{
                sheet.addCell(new Label(2, i, "0"));
            }
            if (ObjectUtil.isNotEmpty(userExcel.getHashrateTotal())){
                sheet.addCell(new Label(3, i, userExcel.getHashrateTotal().toString()));
            }else{
                sheet.addCell(new Label(3, i, "0"));
            }
            if (ObjectUtil.isNotEmpty(userExcel.getXchExperience())){
                sheet.addCell(new Label(4, i, userExcel.getXchExperience().toString()));
            }else{
                sheet.addCell(new Label(4, i, "0"));
            }
            if (ObjectUtil.isNotEmpty(userExcel.getXchHashrateTotal())){
                sheet.addCell(new Label(5, i, userExcel.getXchHashrateTotal().toString()));
            }else{
                sheet.addCell(new Label(5, i, "0"));
            }
//            if (ObjectUtil.isNotEmpty(userExcel.getTotal())){
//                sheet.addCell(new Label(6, i, userExcel.getTotal().toString()));
//            }else{
//                sheet.addCell(new Label(6, i,"0"));
//            }
            i++;
        }
        book.write();
        book.close();
//        return super.exportXlsByList(req, listResult, UserExcel.class, "UserExcel");
    }*/
/*
    @AutoLog(value = "004导出用户数据")
    @PostMapping(value = "/testTwo")
    public ModelAndView testTwo(HttpServletRequest req){
        //先查询全部的用户数据
        List<AuthUser> list = authUserService.lambdaQuery().in(AuthUser::getId,115382,115383,115384,115385).list();

        //最终返回的list
        List<UserExcel> listResult = new ArrayList<>();

        for (AuthUser authUser:list) {
            UserExcel userExcel = new UserExcel();
            userExcel.setUserAccount(authUser.getAccount());

            //查询该用户的上级邀请人
            AuthInvitedPipeline one = authInvitedPipelineService.lambdaQuery().eq(AuthInvitedPipeline::getApiKey, authUser.getApiKey()).one();
            if (ObjectUtil.isNotEmpty(one)){
                //根据邀请人的apike获取邀请人的账号
                AuthUser two = authUserService.lambdaQuery().eq(AuthUser::getApiKey, one.getInviteApiKey()).one();
                if (ObjectUtil.isNotEmpty(two)){
                    userExcel.setInviteApiAccount(two.getAccount());
                }
            }

            //查询该用户的经验值数量FIL
            List<Weight> weightFilList = weightService.lambdaQuery()
                    .eq(Weight::getApiKey, authUser.getApiKey())
                    .eq(Weight::getType, 1)
                    .eq(Weight::getPowerType,1)
                    .list();
            if (ObjectUtil.isNotEmpty(weightFilList)){
                BigDecimal filReduce = weightFilList.stream().map(Weight::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                userExcel.setFilExperience(filReduce);
            }

            //查询累计分销算力FIL
            AuthUserVo authUserDetail = authUserService.getAuthUserDetail(authUser.getId().toString());
            if (ObjectUtil.isNotEmpty(authUserDetail)){
                userExcel.setHashrateTotal(authUserDetail.getAwardVO().getHashrateTotal());
            }

            //查询经验值数量XCH
            List<Weight> weightXchList = weightService.lambdaQuery()
                    .eq(Weight::getApiKey, authUser.getApiKey())
                    .eq(Weight::getType, 1)
                    .eq(Weight::getPowerType, 2)
                    .list();
            if (ObjectUtil.isNotEmpty(weightFilList)){
                BigDecimal xchReduce = weightXchList.stream().map(Weight::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                userExcel.setXchExperience(xchReduce);
                userExcel.setXchHashrateTotal(xchReduce);
            }

            //伞下业绩（社区）
            OperatingIncome operatingIncome = new OperatingIncome();
            operatingIncome.setAccount(authUser.getAccount());

            List<OperatingIncome> records = operatingIncomeController.getOperatingIncomeIPage(operatingIncome, 1, 10, req).getRecords();
            if (ObjectUtil.isNotEmpty(records)){
                OperatingIncome operatingIncome1 = records.get(0);
                if (ObjectUtil.isNotEmpty(operatingIncome)){
                    userExcel.setTotal(operatingIncome1.getTotal());
                }
            }
            listResult.add(userExcel);
        }
        return super.exportXlsByList(req, listResult, UserExcel.class, "UserExcel");
    }
*/


    @AutoLog(value = "导出用户邀请关系")
    @GetMapping(value = "/userInvitedExcel")
    public ModelAndView test(HttpServletRequest request){
        List<AuthInvitedPipeline> list = authInvitedPipelineService.lambdaQuery().list();

        List<InvitedPipelineExcel> listRes = new ArrayList<>();

        for (AuthInvitedPipeline authInvitedPipeline:list) {
            InvitedPipelineExcel invitedPipelineExcel = new InvitedPipelineExcel();

            AuthUser one = authUserService.lambdaQuery().eq(AuthUser::getApiKey, authInvitedPipeline.getApiKey()).one();
            if (ObjectUtils.isNotEmpty(one)){
                invitedPipelineExcel.setApiAccount(one.getAccount());
            }

            AuthUser one1 = authUserService.lambdaQuery().eq(AuthUser::getApiKey, authInvitedPipeline.getInviteApiKey()).one();
            if (ObjectUtils.isNotEmpty(one1)){
                invitedPipelineExcel.setInviteApiAccount(one1.getAccount());
            }

            listRes.add(invitedPipelineExcel);
        }

        //导出
       return super.exportXlsByList(request, listRes, InvitedPipelineExcel.class, "InvitedPipelineExcel");
    }


    /**
     * 分页列表查询
     *
     * @param authUser
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户表-查询列表")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AuthUser authUser,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<AuthUser> pageList = getAuthUserIPage(authUser, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    private IPage<AuthUser> getAuthUserIPage(AuthUser authUser, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<AuthUser> queryWrapper = QueryGenerator.initQueryWrapper(authUser, req.getParameterMap());
        queryWrapper.select("id", "phone", "email", "nickname", "node_source"
                , "api_key", "blocked", "deleted", "area_code", "create_time", "update_time"
                , "account", "role_type", "ga", "vena_fee", "balance_fee", "default_account", "gender"
                , "image", "ga_secret", "ip_count", "device_count", "`lock`", "default_account_no", "language", "color"
                , "currency", "country_language", "is_otc", "effective", "purchase_limit");
        Page<AuthUser> page = new Page<AuthUser>(pageNo, pageSize);
        IPage<AuthUser> pageList = authUserService.page(page, queryWrapper);
        for (AuthUser record : pageList.getRecords()) {
            AuthInvitedPipeline result = authInvitedPipelineService.getBaseMapper().getAccount$code$inviteer$inviteCount$award$level(record.getApiKey());
            if (!ObjectUtils.isEmpty(result)) {
                BeanUtils.copyNewPropertites(result, record);
            }
            if (ObjectUtils.isEmpty(record.getLevel())) {
                Integer level = distributionConfigService.getLevel(record.getApiKey());
                record.setLevel(level.toString());
            }
            DistributionDetail byApiKey = distributionDetailService.getByApiKey(record.getApiKey());
            record.setMarketLevel(byApiKey.getMarketLevel());
            record.setOperatingLevel(byApiKey.getOperatingLevel());
            AuthInvitationCode authInvitationCode = authInvitationCodeService.lambdaQuery().select(AuthInvitationCode::getInvitationCode).eq(AuthInvitationCode::getApiKey, record.getApiKey()).one();
            if (!ObjectUtils.isEmpty(authInvitationCode)) {
                record.setInvitationCode(authInvitationCode.getInvitationCode());
            }
            String inviterAccount = authUserMapper.getInviterAccount(record.getApiKey());
            if (StringUtils.isNotBlank(inviterAccount)) {
                record.setInviter(inviterAccount);
            }
            if (ObjectUtils.isEmpty(record.getLevel())) {
                Integer level = distributionConfigService.getLevel(record.getApiKey());
                record.setLevel(level.toString());
            }
            BigDecimal total = distributionDetailMapper.sumHashrateTotal(record.getApiKey());
            if (!ObjectUtils.isEmpty(total) && BigDecimalUtil.greater(total, BigDecimal.ZERO)) {
                record.setHashrateTotal(BigDecimalUtil.format(total, 3));
            }
        }
        return pageList;
    }

    @AutoLog(value = "用户表-邀请关系列表")
    @GetMapping(value = "/queryInvitePageList")
    public Result<?> queryInvitePageList(AuthUser authUser,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {
        IPage<AuthUser> pageList = getAuthUserInviteIPage(authUser, pageNo, pageSize, req);
        return Result.ok(pageList);
    }


    private IPage<AuthUser> getAuthUserInviteIPage(AuthUser authUser, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        authUser.setLevel(null);
        QueryWrapper<AuthUser> queryWrapper = QueryGenerator.initQueryWrapper(authUser, req.getParameterMap());
        queryWrapper.select("id", "account", "api_key", "create_time");

        if (!StringUtils.equalsAny(active, "dev", "hulihui")) {
            queryWrapper.inSql("api_key", "select api_key FROM (SELECT MAX(A.`level`) AS `level`,api_key " +
                    "FROM (SELECT `level`,api_key FROM distribution_detail  UNION ALL SELECT `level_direct`,api_key FROM distribution_cheat  ) " +
                    "AS A GROUP BY api_key ) as CC WHERE CC.`level`='" + 5 + "'");
        }


        Page<AuthUser> page = new Page<AuthUser>(pageNo, pageSize);
        IPage<AuthUser> pageList = authUserService.page(page, queryWrapper);
        for (AuthUser record : pageList.getRecords()) {
            AuthInvitedPipeline result = authInvitedPipelineService.getBaseMapper().getAccount$code$inviteer$inviteCount$award$level(record.getApiKey());
            if (!ObjectUtils.isEmpty(result)) {
                BeanUtils.copyNewPropertites(result, record);
            }
            if (ObjectUtils.isEmpty(record.getLevel())) {
                Integer level = distributionConfigService.getLevel(record.getApiKey());
                record.setLevel(level.toString());
            }
            BigDecimal award = balanceModifyPipelineMapper.sumRewardsDistribution(record.getApiKey());


            List<String> oneApiKeys = authInvitedPipelineService.lambdaQuery().select(AuthInvitedPipeline::getApiKey)
                    .eq(AuthInvitedPipeline::getInviteApiKey, record.getApiKey()).list()
                    .stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());

            BigDecimal one = BigDecimal.ZERO;
            BigDecimal two = BigDecimal.ZERO;

            Integer inviteCount = recursion(oneApiKeys, 0);

            record.setInviteCount(inviteCount);

            if (!ObjectUtils.isEmpty(oneApiKeys)) {
                one = BigDecimalUtil.divide(weightService.getBaseMapper().sumQuantityInApiKeys(oneApiKeys), 1000);
                List<String> twoApiKeys = authInvitedPipelineService.lambdaQuery().select(AuthInvitedPipeline::getApiKey).in(AuthInvitedPipeline::getInviteApiKey, oneApiKeys).list()
                        .stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
                if (!ObjectUtils.isEmpty(twoApiKeys)) {
                    two = BigDecimalUtil.divide(weightService.getBaseMapper().sumQuantityInApiKeys(twoApiKeys), 1000);
                }
            }


            record.setAward(award).setDistributionHashrate(BigDecimalUtil.add(one, two)).setChildrenPurchase(one).setGrandchildrenPurchase(two);


        }
        return pageList;
    }


    private Integer recursion(List<String> apiKeys, Integer inviteCount) {
        if (!ObjectUtils.isEmpty(apiKeys)) {
            inviteCount += apiKeys.size();
            List<String> childrenApiKeys = authInvitedPipelineService.lambdaQuery().select(AuthInvitedPipeline::getApiKey).in(AuthInvitedPipeline::getInviteApiKey, apiKeys).list()
                    .stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
            return recursion(childrenApiKeys, inviteCount);
        }
        return inviteCount;
    }
    /**
     * 添加
     *
     * @param authUser
     * @return
     */
/*    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AuthUser authUser) {
        authUserService.save(authUser);
        return Result.ok("添加成功！");
    }*/

    /**
     * 编辑
     *
     * @param authUser
     * @return
     */
/*    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AuthUser authUser) {
        authUserService.updateById(authUser);
        return Result.ok("编辑成功!");
    }*/

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
  /*  @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        authUserService.removeById(id);
        return Result.ok("删除成功!");
    }*/

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
/*    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.authUserService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }*/

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AuthUser authUser = authUserService.getById(id);
        if (authUser == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(authUser);
    }

    /**
     * 通过id查询详情
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户表-查询用户详情")
    @ApiOperation("用户详情")
    @GetMapping(value = "/getAuthUserDetail")
    public Result<?> getAuthUserDetail(@RequestParam(name = "id", required = true) String id) {
        AuthUserVo authUserVo = authUserService.getAuthUserDetail(id);
        if (authUserVo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(authUserVo);
    }

    /**
     * 通过id查询详情
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户表-查询用户详情-企亚算力查询")
    @ApiOperation("用户详情")
    @GetMapping(value = "/getAuthUserDetailTwo")
    public Result<?> getAuthUserDetailTwo(@RequestParam(name = "id", required = true) String id) {
        BigDecimal authUserDetailTwo = authUserService.getAuthUserDetailTwo(id);
        if (authUserDetailTwo==null){
            authUserDetailTwo=BigDecimal.ZERO;
        }
        return Result.ok(authUserDetailTwo);
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation("用户编辑详情视图")
    @GetMapping(value = "/editVo")
    public Result<?> editVo(@RequestParam(name = "id", required = true) String id) {

        AuthUserEditVo editVo = authUserService.getEditVo(id);
        if (!ObjectUtils.isEmpty(editVo)) {
            return Result.ok(editVo);
        }
        return Result.error("未找到对应数据");
    }

    /**
     * 通过id查询
     *
     * @return
     */
    @AutoLog(value = "用户表-编辑用户详情")
    @ApiOperation("编辑用户详情")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AuthUserParam authUserParam) {
        authUserService.edit(authUserParam);

        return Result.ok();
    }

    /**
     * 导出excel
     *
     * @param request
     * @param authUser
     */
    @AutoLog(value = "用户表-导出excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(AuthUser authUser,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest request) {
        IPage<AuthUser> pageList = getAuthUserIPage(authUser, pageNo, pageSize, request);
        return exportXls(request, pageList.getRecords(), AuthUser.class, "auth_user");
    }

    protected ModelAndView exportXls(HttpServletRequest request, List<AuthUser> pageList, Class<AuthUser> clazz, String title) {
        // Step.1 组装查询条件
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		/*QueryWrapper<AuthUser> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
        queryWrapper.select("id", "phone", "email", "nickname", "node_source"
                , "api_key", "blocked", "deleted", "area_code", "create_time", "update_time"
                , "account", "role_type", "ga", "vena_fee", "balance_fee", "default_account", "gender"
                , "image", "ga_secret", "ip_count", "device_count", "`lock`", "default_account_no", "language", "color"
                , "currency", "country_language", "is_otc", "effective", "purchase_limit");
        // Step.2 获取导出数据
		List<AuthUser> pageList = authUserService.list(queryWrapper);
		Integer count=0;
		for (AuthUser record :pageList) {
			if (count>500) break;
			Integer levle = distributionConfigMapper.getLevle(record.getApiKey());
			AuthInvitationCode authInvitationCode = authInvitationCodeService.lambdaQuery().select(AuthInvitationCode::getInvitationCode).eq(AuthInvitationCode::getApiKey, record.getApiKey()).one();
			record.setInvitationCode(authInvitationCode.getInvitationCode());
			String inviterAccount = authUserMapper.getInviterAccount(record.getApiKey());
			record.setInviter(inviterAccount);
			record.setLevel(null == levle ? "-1" : levle.toString());
			count++;
		}*/
        List<AuthUser> exportList = null;

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

    @AutoLog(value = "用户表-导出excel")
    @RequestMapping(value = "/exportXlsInvite")
    public ModelAndView exportXlsInvite(AuthUser authUser,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest request) {
        IPage<AuthUser> pageList = getAuthUserInviteIPage(authUser, pageNo, pageSize, request);
        return exportXls(request, pageList.getRecords(), AuthUser.class, "auth_user");
    }


    /**
     * 获取对象ID
     *
     * @return
     */
    private String getId(AuthUser item) {
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
        return super.importExcel(request, response, AuthUser.class);
    }

}
