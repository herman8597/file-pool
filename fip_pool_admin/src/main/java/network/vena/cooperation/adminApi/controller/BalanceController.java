package network.vena.cooperation.adminApi.controller;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import network.vena.cooperation.adminApi.dto.BalanceDTO;
import network.vena.cooperation.util.BigDecimalUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import network.vena.cooperation.adminApi.entity.Balance;
import network.vena.cooperation.adminApi.service.IBalanceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/balance")
@Slf4j
public class BalanceController extends JeecgController<Balance, IBalanceService> {

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    @Autowired
    private IBalanceService balanceService;
    @AutoLog(value = "资产表-操作资产")
    @ApiOperation(value = "操作资产")
    @PostMapping(value = "/exchangeBalance")
    public Result<?> exchangeBalance(@RequestBody BalanceDTO balanceDTO) {
        if (
                !Set.of(13, 18).contains(balanceDTO.getType())
                        || BigDecimalUtil.less(balanceDTO.getAvailable(), BigDecimal.ZERO)
                        || BigDecimalUtil.less(balanceDTO.getFrozen(), BigDecimal.ZERO)

        ) {
            return Result.error("参数有误");
        }
        if (balanceDTO.getType() == 18) {
            balanceDTO.setAvailable(balanceDTO.getAvailable().multiply(new BigDecimal(-1)));
            balanceDTO.setRemark("系统扣除");
        }
        if (balanceDTO.getType() == 13) {
            balanceDTO.setRemark("系统充值");
        }
       /* if (balanceDTO.getType() == 21){
            balanceDTO.setRemark("质押");
        }
        if (balanceDTO.getType()==22){
            balanceDTO.setRemark("GAS");
        }*/
        balanceDTO.setCreateTime(new Date());
        try {
            balanceService.exchange(balanceDTO);
            return Result.ok("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("操作失败");
        }
    }

    /**
     * 分页列表查询
     *
     * @param balance
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Balance balance,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Balance> queryWrapper = QueryGenerator.initQueryWrapper(balance, req.getParameterMap());
        Page<Balance> page = new Page<Balance>(pageNo, pageSize);
        IPage<Balance> pageList = balanceService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param balance
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Balance balance) {
        balanceService.save(balance);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param balance
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Balance balance) {
        balanceService.updateById(balance);
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
        balanceService.removeById(id);
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
        this.balanceService.removeByIds(Arrays.asList(ids.split(",")));
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
        Balance balance = balanceService.getById(id);
        if (balance == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(balance);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param balance
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Balance balance) {
        return super.exportXls(request, balance, Balance.class, "balance");
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
        return super.importExcel(request, response, Balance.class);
    }

}
