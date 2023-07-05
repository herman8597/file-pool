package network.vena.cooperation.adminApi.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.TextInfo;
import network.vena.cooperation.adminApi.service.ITextInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.service.impl.SysDictServiceImpl;
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
 * @Description: text_info
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/textInfo")
@Slf4j
public class TextInfoController extends JeecgController<TextInfo, ITextInfoService> {
    @Autowired
    private ITextInfoService textInfoService;

	@Autowired
    private SysDictServiceImpl sysDictService;

    /**
     * 分页列表查询
     *
     * @param textInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TextInfo textInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TextInfo> queryWrapper = QueryGenerator.initQueryWrapper(textInfo, req.getParameterMap());
        Page<TextInfo> page = new Page<TextInfo>(pageNo, pageSize);
        IPage<TextInfo> pageList = textInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param textInfo
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TextInfo textInfo) {
		Map<String, String> value$text = sysDictService.queryDictItemsByCode("textInfoList_type").stream()
				.collect(Collectors.toMap(DictModel::getValue, DictModel::getText));
		String title = value$text.get(textInfo.getType().toString());
		textInfo.setTitle(title);
		textInfo.setUpdateTime(new Date());
		textInfoService.save(textInfo);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param textInfo
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TextInfo textInfo) {
		Map<String, String> value$text = sysDictService.queryDictItemsByCode("textInfoList_type").stream()
				.collect(Collectors.toMap(DictModel::getValue, DictModel::getText));
		String title = value$text.get(textInfo.getType().toString());
		textInfo.setTitle(title);
		textInfo.setUpdateTime(new Date());
        textInfoService.updateById(textInfo);
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
        textInfoService.removeById(id);
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
        this.textInfoService.removeByIds(Arrays.asList(ids.split(",")));
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
        TextInfo textInfo = textInfoService.getById(id);
        if (textInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(textInfo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param textInfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TextInfo textInfo) {
        return super.exportXls(request, textInfo, TextInfo.class, "text_info");
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
        return super.importExcel(request, response, TextInfo.class);
    }

}
