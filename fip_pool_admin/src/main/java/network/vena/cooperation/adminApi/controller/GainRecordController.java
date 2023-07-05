package network.vena.cooperation.adminApi.controller;

import java.util.ArrayList;
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
import network.vena.cooperation.adminApi.entity.DictInfo;
import network.vena.cooperation.adminApi.entity.UserIdInfo;
import network.vena.cooperation.adminApi.excel.ExcelGainRecord;
import network.vena.cooperation.adminApi.service.impl.AuthUserServiceImpl;
import network.vena.cooperation.adminApi.service.impl.DictInfoServiceImpl;
import network.vena.cooperation.adminApi.service.impl.UserIdInfoServiceImpl;
import network.vena.cooperation.util.BeanUtils;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.GainRecord;
import network.vena.cooperation.adminApi.service.IGainRecordService;

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
 * @Description: gain_record
 * @Author: jeecg-boot
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/gainRecord")
@Slf4j
public class GainRecordController extends JeecgController<GainRecord, IGainRecordService> {
    @Autowired
    private IGainRecordService gainRecordService;
    @Autowired
    private AuthUserServiceImpl authUserService;
    @Autowired
    private UserIdInfoServiceImpl userIdInfoService;

    /**
     * 分页列表查询
     *
     * @param gainRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GainRecord gainRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<GainRecord> pageList = getGainRecordIPage(gainRecord, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    private IPage<GainRecord> getGainRecordIPage(GainRecord gainRecord, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        String accountParam = gainRecord.getAccount();
        if (StringUtils.isNotBlank(accountParam)) {
            gainRecord.setAccount(null);
        }
        QueryWrapper<GainRecord> queryWrapper = QueryGenerator.initQueryWrapper(gainRecord, req.getParameterMap());
        if (StringUtils.isNotBlank(accountParam)) {
            SqlInjectionUtil.filterContent(accountParam);
            queryWrapper.inSql("api_key", "SELECT api_key FROM auth_user WHERE account= '" + accountParam + "'");
        }
        Page<GainRecord> page = new Page<GainRecord>(pageNo, pageSize);
        IPage<GainRecord> result = gainRecordService.page(page, queryWrapper);
        for (GainRecord record : result.getRecords()) {
            AuthUser authUserByCache = authUserService.getAuthUserByCache(record.getApiKey());
            if (!ObjectUtils.isEmpty(authUserByCache)) {
                record.setAccount(authUserByCache.getAccount());
            }
        }
        return result;
    }



    @GetMapping(value = "/listByUser")
    public Result<?> queryPageListByUser(GainRecord gainRecord,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {
        IPage<GainRecord> pageList = getListByUserIPage(gainRecord, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    @RequestMapping(value = "/listByUserExportXls")
    public ModelAndView listByUserExportXls(GainRecord gainRecord,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {
        IPage<GainRecord> pageList = getListByUserIPage(gainRecord, pageNo, pageSize, req);
        ArrayList<ExcelGainRecord> excelGainRecords = new ArrayList<>();
        for (GainRecord record : pageList.getRecords()) {
            ExcelGainRecord excelGainRecord = new ExcelGainRecord();
            BeanUtils.copyProperties(record,excelGainRecord);
            excelGainRecords.add(excelGainRecord);
        }
        return super.exportXlsByList(req,excelGainRecords,ExcelGainRecord.class,"gain_record");
    }

    private IPage<GainRecord> getListByUserIPage(GainRecord gainRecord, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        String accountParam = gainRecord.getAccount();
        if (StringUtils.isNotBlank(accountParam)) {
            gainRecord.setAccount(null);
        }
        String realName = gainRecord.getRealName();
        if (StringUtils.isNotBlank(realName)) {
            gainRecord.setRealName(null);
        }
        QueryWrapper<GainRecord> queryWrapper = QueryGenerator.initQueryWrapper(gainRecord, req.getParameterMap());
        queryWrapper.select("api_key , asset, SUM(IFNULL(issued_amount,0)) AS issuedAmount ,MAX(hashrate) AS hashrate ,SUM(amount) AS amount");
        if (StringUtils.isNotBlank(accountParam)) {
            SqlInjectionUtil.filterContent(accountParam);
            queryWrapper.inSql("api_key", "SELECT api_key FROM auth_user WHERE account= '" + accountParam + "'");
        }
        if (StringUtils.isNotBlank(realName)) {
            SqlInjectionUtil.filterContent(realName);
            queryWrapper.inSql("api_key", "SELECT DISTINCT api_key FROM user_id_info WHERE real_name='" + realName + "' AND audit_status =1 ");
        }
        queryWrapper.groupBy("api_key,asset");
        Page<GainRecord> page = new Page<GainRecord>(pageNo, pageSize);
        IPage<GainRecord> result = gainRecordService.page(page, queryWrapper);
        for (GainRecord record : result.getRecords()) {
            AuthUser authUserByCache = authUserService.getAuthUserByCache(record.getApiKey());
            if (!ObjectUtils.isEmpty(authUserByCache)) {
                record.setAccount(authUserByCache.getAccount());
            }
            UserIdInfo userIdInfo = userIdInfoService.getUserIdInfo(record.getApiKey());
            if (!ObjectUtils.isEmpty(userIdInfo)) {
                record.setRealName(userIdInfo.getRealName());
            }
        }
        return result;
    }

    /**
     * 添加
     *
     * @param gainRecord
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GainRecord gainRecord) {
        gainRecordService.save(gainRecord);
        return Result.ok("添加成功！");
    }

    /**
     * 添加
     *
     * @param gainRecord
     * @return
     */
    @PostMapping(value = "/changeAward")
    public Result<?> changeAward(@RequestBody GainRecord gainRecord) {
        if (BigDecimalUtil.equal(gainRecord.getAmount(), 0)) {
            return Result.error("数量有误！");
        }
        DictInfo gain_coin = new DictInfo().setDictKey("gain_coin").setValue(gainRecord.getAmount().toPlainString()).setDictCode("1");
        dictInfoService.save(gain_coin);
        return Result.ok("添加成功！");
    }

    @Autowired
    private DictInfoServiceImpl dictInfoService;

    /**
     * 编辑
     *
     * @param gainRecord
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GainRecord gainRecord) {
        gainRecordService.updateById(gainRecord);
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
        gainRecordService.removeById(id);
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
        this.gainRecordService.removeByIds(Arrays.asList(ids.split(",")));
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
        GainRecord gainRecord = gainRecordService.getById(id);
        if (gainRecord == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(gainRecord);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gainRecord
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(GainRecord gainRecord,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest req) {
        IPage<GainRecord> pageList = getGainRecordIPage(gainRecord, pageNo, pageSize, req);
        return super.exportXlsByList(req, pageList.getRecords(), GainRecord.class, "gain_record");
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
        return super.importExcel(request, response, GainRecord.class);
    }

}
