package network.vena.cooperation;

import cn.hutool.core.util.ObjectUtil;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import network.vena.cooperation.adminApi.controller.AuthUserController;
import network.vena.cooperation.adminApi.controller.EarningsPledgeDetailController;
import network.vena.cooperation.adminApi.entity.*;
import network.vena.cooperation.adminApi.excel.UserExcel;
import network.vena.cooperation.adminApi.mapper.BalanceMapper;
import network.vena.cooperation.adminApi.service.IEarningsPledgeDetailService;
import network.vena.cooperation.adminApi.service.IWeightService;
import network.vena.cooperation.adminApi.service.impl.AuthInvitedPipelineServiceImpl;
import network.vena.cooperation.adminApi.service.impl.AuthUserServiceImpl;
import network.vena.cooperation.adminApi.service.impl.BalanceServiceImpl;
import network.vena.cooperation.adminApi.vo.AddFrozen;
import network.vena.cooperation.adminApi.vo.BalanceKouChu;
import network.vena.cooperation.adminApi.vo.PledgeKouChuTwo;
import network.vena.cooperation.base.vo.AuthUserVo;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ExcelReaderUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CooperationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CooperationApplicationTests {

    @Autowired
    private network.vena.cooperation.adminApi.service.impl.DistributionDetailServiceImpl DistributionDetailServiceImpl;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private BalanceServiceImpl balanceService;

    @Autowired
    private IEarningsPledgeDetailService earningsPledgeDetailService;

    @Autowired
    private EarningsPledgeDetailController earningsPledgeDetailController;

    @Autowired
    private AuthUserServiceImpl authUserService;

    @Autowired
    private AuthInvitedPipelineServiceImpl authInvitedPipelineService;

    @Autowired
    private AuthUserController authUserController;


    /***
     * 导出004的邀请关系
     */
/*    @Test
    public void test(){
        List<AuthInvitedPipeline> list = authInvitedPipelineService.lambdaQuery().list();

        List<InvitedPipelineExcel> listRes = new ArrayList<>();

        for (AuthInvitedPipeline authInvitedPipeline:list) {
            InvitedPipelineExcel invitedPipelineExcel = new InvitedPipelineExcel();

            AuthUser one = authUserService.lambdaQuery().eq(AuthUser::getApiKey, authInvitedPipeline.getApiKey()).one();
            if (ObjectUtil.isNotEmpty(one)){
                invitedPipelineExcel.setApiAccount(one.getAccount());
            }

            AuthUser one1 = authUserService.lambdaQuery().eq(AuthUser::getApiKey, authInvitedPipeline.getInviteApiKey()).one();
            if (ObjectUtil.isNotEmpty(one1)){
                invitedPipelineExcel.setInviteApiAccount(one1.getAccount());
            }

            listRes.add(invitedPipelineExcel);
        }

        //导出
         super.exportXlsByList(request, listRes, InvitedPipelineExcel.class, "InvitedPipelineExcel");
    }*/
    @Test
//    @Transactional(rollbackFor = RuntimeException.class)
    //修改资产
    public void contextLoads() {
        List<BalanceKouChu> kouchu = balanceMapper.kouchu();
        System.out.println(kouchu);
        for (BalanceKouChu balanceKouChu : kouchu) {
            String apiKey = balanceKouChu.getApiKey();
            BigDecimal subTotal = balanceKouChu.getSubTotal();
            Balance xch = balanceService.lambdaQuery().eq(Balance::getApiKey, apiKey).eq(Balance::getAsset, "XCH").one();
//            //当前xch资产
//            BigDecimal available = xch.getAvailable();
//            //扣除之后的资产
//            BigDecimal subtract = available.subtract(subTotal);
            boolean update = balanceService.lambdaUpdate().setSql("available = available - " + subTotal.stripTrailingZeros().toPlainString())
                    .eq(Balance::getApiKey, apiKey).eq(Balance::getAsset, "XCH").update();
            System.out.println(update);
            //修改
//            boolean xch1 = balanceService.lambdaUpdate().set(Balance::getAvailable, subtract).eq(Balance::getAsset, "XCH").eq(Balance::getApiKey, apiKey).update();
        }
    }


    @Test
    public void balancePledge() {
        File file = new File("C:\\Users\\xstc\\Downloads\\balancePledge.xls");
        try {
            List<PledgeKouChuTwo> excelUserEntities = ExcelReaderUtil.readExcel2Bean(new FileInputStream(file), PledgeKouChuTwo.class);
            for (PledgeKouChuTwo pledgeKouChuTwo : excelUserEntities) {
                String sumTotal = pledgeKouChuTwo.getSumTotal();
                BigDecimal bigDecimal = new BigDecimal(sumTotal);
                BigDecimal daikouchu = bigDecimal.multiply(new BigDecimal(7.305).setScale(4, RoundingMode.HALF_UP));
                System.out.println(daikouchu);
                boolean update = balanceService.lambdaUpdate().setSql("frozen=frozen-" + daikouchu.stripTrailingZeros().toPlainString())
                        .eq(Balance::getApiKey, pledgeKouChuTwo.getApiKey()).eq(Balance::getAsset, "FIL").update();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void addFrozen() {
        File file = new File("C:\\Users\\xstc\\Desktop\\第三次增加冻结.xlsx");
        try {
            List<AddFrozen> addFrozens = ExcelReaderUtil.readExcel2Bean(new FileInputStream(file), AddFrozen.class);

            for (AddFrozen addFrozen : addFrozens) {
                BigDecimal bigDecimal = new BigDecimal(addFrozen.getSubTotal());
                BigDecimal abs = bigDecimal.abs();

                boolean fil = balanceService.lambdaUpdate().setSql("frozen=frozen+" + abs.stripTrailingZeros().toPlainString())
                        .eq(Balance::getApiKey, addFrozen.getApiKey()).eq(Balance::getAsset, "FIL").update();
                System.out.println(fil);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test03() throws IOException, IllegalAccessException, InstantiationException {
        File file = new File("C:\\Users\\xstc\\Downloads\\balancePledge.xls");
        List<PledgeKouChuTwo> excelUserEntities = ExcelReaderUtil.readExcel2Bean(new FileInputStream(file), PledgeKouChuTwo.class);

        for (PledgeKouChuTwo pledgeKouChuTwo : excelUserEntities) {
            Integer integer = balanceMapper.updateEarning(pledgeKouChuTwo.getApiKey());
            System.out.println(integer);
        }
    }


    @Test
    @Transactional
    public void test04() throws IOException, IllegalAccessException, InstantiationException {
        File file = new File("C:\\Users\\xstc\\Downloads\\kouchu.xls");
        List<PledgeKouChuTwo> excelUserEntities = ExcelReaderUtil.readExcel2Bean(new FileInputStream(file), PledgeKouChuTwo.class);
        for (PledgeKouChuTwo pledgeKouChuTwo : excelUserEntities) {
            String apiKey = pledgeKouChuTwo.getApiKey();
            EarningsPledgeDetail fil = earningsPledgeDetailService.lambdaQuery().eq(EarningsPledgeDetail::getApiKey, apiKey).eq(EarningsPledgeDetail::getAsset, "FIL").one();
            //质押减剩余
            BigDecimal subtract = fil.getMiningPledge().subtract(new BigDecimal(pledgeKouChuTwo.getKouchuhoushengyu()));
            //存儲數據
            EarningsPledgeDetail earningsPledgeDetail = new EarningsPledgeDetail();
            earningsPledgeDetail.setAmount(subtract);
            earningsPledgeDetail.setApiKey(pledgeKouChuTwo.getApiKey());
            earningsPledgeDetail.setFlag(false);
            earningsPledgeDetail.setOperType(1);

            earningsPledgeDetailController.changePledge(earningsPledgeDetail);
        }
    }


}
