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

import com.alibaba.fastjson.JSONObject;
import network.vena.cooperation.adminApi.entity.Status;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.FanyiV3Demo;
import network.vena.cooperation.util.ObjectUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import network.vena.cooperation.adminApi.entity.Goods;
import network.vena.cooperation.adminApi.service.IGoodsService;

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
 * @Description: goods
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/goods")
@Slf4j
public class GoodsController extends JeecgController<Goods, IGoodsService> {
    @Autowired
    private IGoodsService goodsService;

    /**
     * 分页列表查询
     *
     * @param goods
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Goods goods,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        if (!ObjectUtils.isEmpty(goods.getSalesQuantity())) {
            goods.setSalesQuantity(null);
        }
        QueryWrapper<Goods> queryWrapper = QueryGenerator.initQueryWrapper(goods, req.getParameterMap());
        Page<Goods> page = new Page<Goods>(pageNo, pageSize);
        IPage<Goods> pageList = goodsService.page(page, queryWrapper);
        for (Goods record : pageList.getRecords()) {
            record.setSalesQuantity(BigDecimalUtil.sub(record.getQuantity(), record.getRemainingQuantity()));
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param goods
     * @return
     */
/*    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Goods goods) {
        goods.setUpdateTime(new Date());
        goods.setCreateTime(new Date());
        goods.setUnit("TB");
        goodsService.save(goods);
        return Result.ok("添加成功！");
    }*/

    /**
     * 添加
     *
     * @param goods
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Goods goods) throws IOException {
        goods.setUpdateTime(new Date());
        goods.setCreateTime(new Date());
        goods.setUnit("TB");


        //有道智云不能一次性翻译超过三个字段，因此下面四个字段分两次调用接口

        //将需要翻译的字段存到实体类中1
//        Goods goods1 = new Goods();
//        goods1.setTag(goods.getTag());
//        goods1.setName(goods.getName());
//
//        //将需要翻译的字段存到实体类中2
//        Goods goods2 = new Goods();
//        goods2.setHighlight(goods.getHighlight());
//        goods2.setSlogan(goods.getSlogan());
//
//        //将需要翻译的字段存到实体类中3
//        Goods goods3 = new Goods();
//        goods3.setContractDetails(goods.getContractDetails().replaceAll("<[.[^<]]*>", "").replaceAll("\\s", ""));
//
//        //调用接口进行翻译1
//        Object fanyi1 = FanyiV3Demo.fanyi(goods1);
//        Goods goods4 = JSONObject.parseObject(fanyi1.toString(), Goods.class);
//
//        //调用接口进行翻译2
//        Object fanyi2 = FanyiV3Demo.fanyi(goods2);
//        Goods goods5 = JSONObject.parseObject(fanyi2.toString().replaceAll(" \"", "\"").replaceAll("\" ", "\""), Goods.class);
//
//        //调用接口进行翻译3
//        Object fanyi3 = FanyiV3Demo.fanyi(goods3);
//        Goods goods6 = JSONObject.parseObject(fanyi3.toString().replaceAll(" \"", "\"").replaceAll("\" ", "\""), Goods.class);


        //将翻译后的值重新存到对象中
//        goods.setEnName(goods4.getName());
//        goods.setEnTag(goods4.getTag());
//        goods.setEnHighlight(goods5.getHighlight());
//        goods.setEnSlogan(goods5.getSlogan());
//        goods.setEnContractDetails(goods6.getContractDetails());

        goods.setEnName(FanyiV3Demo.fanyi(goods.getName()));
        goods.setEnTag(FanyiV3Demo.fanyi(goods.getTag()));
        goods.setEnHighlight(FanyiV3Demo.fanyi(goods.getHighlight()));
        goods.setEnSlogan(FanyiV3Demo.fanyi(goods.getSlogan()));
        goods.setEnContractDetails(FanyiV3Demo.fanyi(goods.getContractDetails()));


        goodsService.save(goods);
        return Result.ok("添加成功！");
    }


    /**
     * 编辑
     *
     * @param goods
     * @return
     */
/*    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Goods goods) {
        goods.setUpdateTime(new Date());
        goodsService.updateById(goods);
        return Result.ok("编辑成功!");
    }*/


    /**
     * 编辑
     *
     * @param goods
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Goods goods) throws IOException {
        goods.setUpdateTime(new Date());

        //有道智云不能一次性翻译超过三个字段，因此下面四个字段分两次调用接口

//        //将需要翻译的字段存到实体类中1
//        Goods goods1 = new Goods();
//        goods1.setTag(goods.getTag());
//        goods1.setName(goods.getName());
//
//        //将需要翻译的字段存到实体类中2
//        Goods goods2 = new Goods();
//        goods2.setHighlight(goods.getHighlight());
//        goods2.setSlogan(goods.getSlogan());

        //将需要翻译的字段存到实体类中3
//        Goods goods3 = new Goods();
//        goods3.setContractDetails(goods.getContractDetails().replaceAll("<[.[^<]]*>", "").replaceAll("\\s",""));

        //调用接口进行翻译1
//        Object fanyi1 = FanyiV3Demo.fanyi(goods1);
//        Goods goods4 = JSONObject.parseObject(fanyi1.toString(), Goods.class);

        //调用接口进行翻译2
//        Goods goods5 = FanyiV3Demo.fanyi(goods2);
//        Goods goods5 = JSONObject.parseObject(fanyi2.toString(), Goods.class);

        //调用接口进行翻译3
//        Object fanyi3 = FanyiV3Demo.fanyi(goods3);
//        Goods goods6 = JSONObject.parseObject(fanyi3.toString().replaceAll(" \"", "\"").replaceAll("\" ", "\""), Goods.class);

        //将翻译后的值重新存到对象中
        goods.setEnName(FanyiV3Demo.fanyi(goods.getName()));
        goods.setEnTag(FanyiV3Demo.fanyi(goods.getTag()));
        goods.setEnHighlight(FanyiV3Demo.fanyi(goods.getHighlight()));
        goods.setEnSlogan(FanyiV3Demo.fanyi(goods.getSlogan()));
        goods.setEnContractDetails(FanyiV3Demo.fanyi(goods.getContractDetails()));

        goodsService.updateById(goods);
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
        goodsService.removeById(id);
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
        this.goodsService.removeByIds(Arrays.asList(ids.split(",")));
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
        Goods goods = goodsService.getById(id);
        if (goods == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(goods);
    }

    /**
     * 通过id禁用
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/editeStatus")
    public Result<?> editeStatus(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "id", required = true) Integer stutas) {
        goodsService.editeStatus(id, stutas);
        return Result.ok();
    }

    /**
     * 导出excel
     *
     * @param request
     * @param goods
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Goods goods) {
        return super.exportXls(request, goods, Goods.class, "goods");
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
        return super.importExcel(request, response, Goods.class);
    }

}
