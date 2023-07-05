package network.vena.cooperation.adminApi.controller;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiOperation;
import network.vena.cooperation.adminApi.entity.AuthUser;
import network.vena.cooperation.adminApi.entity.GcAsset;
import network.vena.cooperation.adminApi.excel.ExcelGcWithdrawal;
import network.vena.cooperation.adminApi.mapper.AuthUserMapper;
import network.vena.cooperation.adminApi.param.BaseParam;
import network.vena.cooperation.adminApi.service.IGcAssetService;
import network.vena.cooperation.util.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.GcWithdrawal;
import network.vena.cooperation.adminApi.service.IGcWithdrawalService;

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
 * @Description: gc_withdrawal
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/gcWithdrawal")
@Slf4j
public class GcWithdrawalController extends JeecgController<GcWithdrawal, IGcWithdrawalService> {
    @Autowired
    private IGcWithdrawalService gcWithdrawalService;

    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private IGcAssetService gcAssetService;

    /**
     * 分页列表查询
     *
     * @param gcWithdrawal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "提现记录表-查询列表")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GcWithdrawal gcWithdrawal,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<GcWithdrawal> pageList = getGcWithdrawalIPage(gcWithdrawal, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    private IPage<GcWithdrawal> getGcWithdrawalIPage(GcWithdrawal gcWithdrawal, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        String queryAccount = gcWithdrawal.getAccount();
        gcWithdrawal.setAccount(null);
        QueryWrapper<GcWithdrawal> queryWrapper = QueryGenerator.initQueryWrapper(gcWithdrawal, req.getParameterMap());
        if (StringUtils.isNotBlank(queryAccount)) {
            queryWrapper.inSql("api_key", "SELECT api_key FROM `auth_user` WHERE account= '" + queryAccount+"'");
        }
        Page<GcWithdrawal> page = new Page<GcWithdrawal>(pageNo, pageSize);
        IPage<GcWithdrawal> pageList = gcWithdrawalService.page(page, queryWrapper);
        for (GcWithdrawal record : pageList.getRecords()) {
            AuthUser accountAndPhone = authUserMapper.getAccountAndPhone(record.getApiKey());
            if (ObjectUtil.isNotEmpty(accountAndPhone)){
                record.setAccount(accountAndPhone.getAccount());
                record.setPhone(accountAndPhone.getPhone());
            }
        }
        return pageList;
    }

    /**
     * 获取提现余额
     *
     * @return
     */
    @GetMapping(value = "/getWithdrawBalance")
    @ApiOperation(value = "获取提现余额")
    public Result<?> getWithdrawBalance() {
        return Result.ok(gcWithdrawalService.getWithdrawBalance());
    }

/*    *//**
     * 添加
     *
     * @param gcWithdrawal
     * @return
     *//*
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GcWithdrawal gcWithdrawal) {
        gcWithdrawalService.save(gcWithdrawal);
        return Result.ok("添加成功！");
    }*/


    /**
     * 提币免审额度
     *
     * @param
     * @return
     */
    @PostMapping(value = "/cancelAudit")
    @Transactional
    public Result<?> cancelAudit(@RequestBody List<Map<String,String>> param) {
        //修改免提额度
        for (Map<String,String> reqMap:param) {
            BigDecimal amount = new BigDecimal(reqMap.get("amount"));
            gcAssetService.lambdaUpdate().set(GcAsset::getWithdrawalPassAmount,amount).eq(GcAsset::getAsset,reqMap.get("assets")).update();
        }
        return Result.ok("修改成功！");
    }


    /**
     * 查看提币免审额度
     *
     * @param
     * @return
     */
    @PostMapping(value = "/queryCancelAudit")
    @Transactional
    public Result<?> queryCancelAudit() {
        List<GcAsset> gcAssetlist = gcAssetService.lambdaQuery().eq(GcAsset::getAsset, "FIL").or().eq(GcAsset::getAsset, "USDT").list();
        List<Map<String,String>> resList= new ArrayList<>();
        Map<String, String> resMap= new HashMap<>();
        for (GcAsset gcAsset:gcAssetlist) {
            if ("FIL".equals(gcAsset.getAsset()) || "USDT".equals(gcAsset.getAsset())){
                resMap = new HashMap<>();
                resMap.put("assets",gcAsset.getAsset());
                resMap.put("amount",gcAsset.getWithdrawalPassAmount().toString());
                resList.add(resMap);
            }
        }
        return Result.ok(resList);
    }

    /**
     * 审核
     *
     * @param
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> audit(@RequestBody GcWithdrawal gcWithdrawal) {
        try {
            return Result.ok(gcWithdrawalService.audit(gcWithdrawal));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("审核失败!");
        }
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GcWithdrawal gcWithdrawal = gcWithdrawalService.getById(id);
        if (gcWithdrawal == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gcWithdrawal);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gcWithdrawal
     */
    @RequestMapping(value = "/exportXls")
    @AutoLog(value = "提现记录表-导出excel")
    public ModelAndView exportXls(GcWithdrawal gcWithdrawal,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest request) {
        IPage<GcWithdrawal> pageList = getGcWithdrawalIPage(gcWithdrawal, pageNo, pageSize, request);

        List<ExcelGcWithdrawal> excelGcWithdrawals = new ArrayList<>();

        for (GcWithdrawal gcWithdrawal1:pageList.getRecords()) {
            ExcelGcWithdrawal excelGcWithdrawal = new ExcelGcWithdrawal();
            //将需要导出的数据拷贝到ExcelPledgeDetail实体类中
            BeanUtils.copyNewPropertites(gcWithdrawal1,excelGcWithdrawal);
            //并将数据单条数据都添加到集合中
            excelGcWithdrawals.add(excelGcWithdrawal);
        }
          return super.exportXlsByList(request, excelGcWithdrawals, ExcelGcWithdrawal.class, "ExcelGcWithdrawal");
       // return exportXls(request, pageList.getRecords(), GcWithdrawal.class, "gc_withdrawal");
    }

    /**
     * 导出excel
     *
     * @param request
     */
    protected ModelAndView exportXls(HttpServletRequest request, List<GcWithdrawal> pageList, Class<GcWithdrawal> clazz, String title) {
	/*	 // Step.1 组装查询条件
		 QueryWrapper<GcWithdrawal> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<GcWithdrawal> pageList = service.list(queryWrapper);*/
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<GcWithdrawal> exportList = null;

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
    private String getId(GcWithdrawal item) {
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
        return super.importExcel(request, response, GcWithdrawal.class);
    }

}
