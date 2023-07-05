package network.vena.cooperation.adminApi.provider;

import network.vena.cooperation.adminApi.param.QueryParam;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

public class AuthInvitedPipelineProvider {


    public String InviteQuery(@Param("Q") QueryParam queryParam) {
        SQL sql = new SQL() {{
            SELECT("au.api_key,\n" +
                    "au.id,\n" +
                    "IFNULL( au.phone, au.email ) account,\n" +
                    "aic.invitation_code,\n" +
                    "dd.`level`,\n" +
                    "au.create_time,\n" +
                    "dd.children_purchase + dd.grandchildren_purchase AS hashrateTotal,\n" +
                    "dd.children_purchase,\n" +
                    "dd.grandchildren_purchase\n");
            FROM("\tauth_user AS au,\n" +
                    "\tauth_invitation_code AS aic,\n" +
                    "\tdistribution_detail AS dd,\n" +
                    "\tauth_invited_pipeline AS aip");

            if (StringUtils.isBlank(queryParam.getInviteAccount())) {
                WHERE(" au.api_key = aip.api_key ");
            } else {
                WHERE(" au.api_key = aip.invite_api_key ");
            }

            AND();
            WHERE(" aip.api_key = aic.api_key AND aic.api_key = dd.api_key ");

            ORDER_BY(" au.create_time DESC ");
        }};
        return sql.toString();
    }

    public String findInviteAccount$InviteCount$award(@Param("apiKey") String apiKey) {
        SQL sql = new SQL() {{
            SELECT("*");
            FROM(
                    "(\n" +
                            "\tSELECT\n" +
                            "\t\tau.account AS inviteAccount \n" +
                            "\tFROM\n" +
                            "\t\tauth_invited_pipeline AS aip,\n" +
                            "\t\tauth_user AS au \n" +
                            "\tWHERE\n" +
                            "\t\taip.api_key = #{apiKey} \n" +
                            "\t\tAND au.api_key = aip.invite_api_key \n" +
                            "\t) AS AA,\n" +
                            "\t(\n" +
                            "\tSELECT\n" +
                            "\t\tSUM( A.total ) AS inviteCount \n" +
                            "\tFROM\n" +
                            "\t\t(\n" +
                            "\t\tSELECT\n" +
                            "\t\t\tcount( 1 ) AS total \n" +
                            "\t\tFROM\n" +
                            "\t\t\tauth_invited_pipeline \n" +
                            "\t\tWHERE\n" +
                            "\t\t\tinvite_api_key = #{apiKey} UNION ALL\n" +
                            "\t\tSELECT\n" +
                            "\t\t\tcount( 1 ) AS total \n" +
                            "\t\tFROM\n" +
                            "\t\t\tauth_invited_pipeline \n" +
                            "\t\tWHERE\n" +
                            "\t\t\tinvite_api_key IN ( SELECT api_key FROM auth_invited_pipeline WHERE invite_api_key = #{apiKey} ) \n" +
                            "\t\t) AS A \n" +
                            "\t) AS BB,\n" +
                            "\t( SELECT SUM( quantity ) AS award FROM balance_modify_pipeline WHERE type = 12 AND api_key = #{apiKey} ) AS CC"
            );

        }};
        return sql.toString();
    }

    public String inviteDetailByApiKey(@Param("Q") QueryParam queryParam) {

        return buildSQL(queryParam);

        /*if (ObjectUtils.isEmpty(queryParam.getRelation())||0==queryParam.getRelation()) {
            String one = buidlQuery1(queryParam.setRelation(1));
            String two = buidlQuery2(queryParam.setRelation(2));
            String sql = one + " UNION ALL " + two;
            System.out.println(sql);
            return sql;
        }else{
            return buidlQuery(queryParam);
        }*/
    }

    private String buildSQL(@Param("Q") QueryParam queryParam) {
        if (ObjectUtils.isEmpty(queryParam.getRelation())) {
            return children(queryParam) + " UNION ALL " + grandchild(queryParam);
        } else if (1 == queryParam.getRelation()) {
            return children(queryParam);
        } else if (2 == queryParam.getRelation()) {
            return grandchild(queryParam);
        }
        return null;
    }

    private String grandchild(QueryParam queryParam) {
        SQL sql = new SQL() {{
            SELECT("api_key,2 AS relation");
            FROM("auth_invited_pipeline");
            WHERE("invite_api_key IN (SELECT api_key FROM auth_invited_pipeline WHERE invite_api_key=#{Q.apiKey})");
        }};
        return sql.toString();
    }

    private String children(@Param("Q") QueryParam queryParam) {
        SQL sql = new SQL() {{
            SELECT("api_key,1 AS relation");
            FROM("auth_invited_pipeline");
            WHERE("invite_api_key=#{Q.apiKey}");
        }};
        return sql.toString();
    }

   /* private String buidlQuery1(@Param("Q") QueryParam queryParam) {
        SQL sql = new SQL() {{
            SELECT("au.id," +
                    "au.api_key," +
                    "aip.invite_api_key," +
                    "au.account," +
                    "dd.`level`," +
                    "1 AS relation");
*//*
            SELECT("au.id," +
                    "au.api_key," +
                    "aip.invite_api_key," +
                    "au.account," +
                    "dd.`level`," +
                    "#{Q.relation} AS relation," +
                    "dd.self_purchase," +
                    "dd.children_purchase + dd.grandchildren_purchase AS teamHashrate");
*//*
            FROM("auth_user AS au," +
                    "auth_invited_pipeline AS aip," +
                    "distribution_detail AS dd "
            );

            if (queryParam.getRelation() == 1) {
                WHERE("aip.invite_api_key = #{Q.apiKey} ");
            } else {
                WHERE(" aip.invite_api_key IN ( SELECT api_key FROM auth_invited_pipeline WHERE invite_api_key = #{Q.apiKey} ) ");
            }

            AND();
            WHERE("au.api_key = aip.api_key");
            AND();
            WHERE("au.api_key = dd.api_key");

        }};
        return sql.toString();
    }

    private String buidlQuery2(@Param("Q") QueryParam queryParam) {
        SQL sql = new SQL() {{
            SELECT("au.id," +
                    "au.api_key," +
                    "aip.invite_api_key," +
                    "au.account," +
                    "dd.`level`," +
                    "2 AS relation");
*//*
            SELECT("au.id," +
                    "au.api_key," +
                    "aip.invite_api_key," +
                    "au.account," +
                    "dd.`level`," +
                    "#{Q.relation} AS relation," +
                    "dd.self_purchase," +
                    "dd.children_purchase + dd.grandchildren_purchase AS teamHashrate");
*//*
            FROM("auth_user AS au," +
                    "auth_invited_pipeline AS aip," +
                    "distribution_detail AS dd "
            );

            if (queryParam.getRelation() == 1) {
                WHERE("aip.invite_api_key = #{Q.apiKey} ");
            } else {
                WHERE(" aip.invite_api_key IN ( SELECT api_key FROM auth_invited_pipeline WHERE invite_api_key = #{Q.apiKey} ) ");
            }

            AND();
            WHERE("au.api_key = aip.api_key");
            AND();
            WHERE("au.api_key = dd.api_key");

        }};
        return sql.toString();
    }
    private String buidlQuery(@Param("Q") QueryParam queryParam) {
        SQL sql = new SQL() {{
            SELECT("au.id," +
                    "au.api_key," +
                    "aip.invite_api_key," +
                    "au.account," +
                    "dd.`level`," +
                    "#{Q.relation} AS relation");
*//*
            SELECT("au.id," +
                    "au.api_key," +
                    "aip.invite_api_key," +
                    "au.account," +
                    "dd.`level`," +
                    "#{Q.relation} AS relation," +
                    "dd.self_purchase," +
                    "dd.children_purchase + dd.grandchildren_purchase AS teamHashrate");
*//*
            FROM("auth_user AS au," +
                    "auth_invited_pipeline AS aip," +
                    "distribution_detail AS dd "
            );

            if (queryParam.getRelation() == 1) {
                WHERE("aip.invite_api_key = #{Q.apiKey} ");
            } else {
                WHERE(" aip.invite_api_key IN ( SELECT api_key FROM auth_invited_pipeline WHERE invite_api_key = #{Q.apiKey} ) ");
            }

            AND();
            WHERE("au.api_key = aip.api_key");
            AND();
            WHERE("au.api_key = dd.api_key");

        }};
        return sql.toString();
    }*/

}
