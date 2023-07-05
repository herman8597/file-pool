package network.vena.cooperation.common;

import cn.hutool.core.util.RandomUtil;
import lombok.Setter;
import org.jeecg.common.util.RedisUtil;

import java.util.HashSet;
import java.util.Set;

public class InvitationCodeGenerate {

    private static int length = 6;
    private static int count = 10000;
    private static String invitationKey = "invitation_key";

    String[] sourceAarry = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    };

    @Setter
    private RedisUtil redisUtil;

    private Set<String> existSet;

    public String generate() {
        String code = "";
        for (int i = 0; i < length; i++) {
            code += sourceAarry[RandomUtil.randomInt(0, sourceAarry.length - 1)];
        }
        return code;
    }



    /**
     * 邀请码批量生成
     * @return
     */
    private Set<String> generateInvitationCode() {
        Set<String> newSet = new HashSet<>();
        do {
            String code = generate();
            if (!this.existSet.contains(code)) {
                newSet.add(code);
            }
        } while (newSet.size() < count);
        return newSet;
    }
}
