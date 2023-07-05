package network.vena.cooperation.common.constant;

import java.util.Arrays;
import java.util.List;

public interface PojoConstants {

    String COIN_ETH = "ETH";
    String COIN_USDT = "USDT";
    String COIN_FILP = "FILP";
    String COIN_FIL = "FIL";
    String COIN_BTC = "BTC";

    Integer ONE_YEAR = 365;

    Integer SUCCEED = 1;


    class ReplenishmentRecord {
        public static final Integer CONSENT = 1; //同意
        public static final Integer REFUSE = 2; //拒绝
        public static final Integer UNREVIEWED = 0; //未审核


        public static final Integer REPLACEMENT_ORDER = 0; //补单
        public static final Integer RECHARGE = 1; //充值


    }

    class Weight {
        public static final Integer NON_PAYMENT = 0;
        public static final Integer ACCOUNT_PAID = 1;
        public static final Integer REPLACEMENT_ORDER = 7;
    }

    class config {
        public static final String EARNINGS = "earnings";
        public static final String LEVEL = "level";
    }

    class BalanceModifyPipeline {

        public static final Integer CONSENT = 1;
        public static final Integer REJECT = 2;
        public static final Integer BUY_TO_CALCULATE_FORCE = 0;
        public static final Integer RECHARGE = 1;
        public static final Integer PROCESSING = 3;
        public static final Integer WITHDRAW = 4;
        public static final Integer MINING_EARNINGS = 17; //挖矿收益
        public static final Integer MINING_REBATE = 18; //挖矿返佣
        public static final Integer FILLING_MONEY = 19; //补款

        public static final List<Integer> increase = Arrays.asList(3, 9, 12, 13, 14, 17, 18);
    }

    class GcWithdrawal {
        public static final String PENDING = "pending";
        public static final String REFUSE = "refuse";
        public static final String WAITING_MANAGER_CHECK = "WAITING_MANAGER_CHECK";
        public static final String WAITING_WALLET_HANDLING = "WAITING_WALLET_HANDLING";
        public static final String MANAGER_REFUSED = "MANAGER_REFUSED";

    }
}
