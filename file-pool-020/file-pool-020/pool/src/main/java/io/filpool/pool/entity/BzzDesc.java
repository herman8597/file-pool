package io.filpool.pool.entity;

import lombok.Data;

@Data
public class BzzDesc {
    //全网节点数
     private long beenodesAll;
    //全网兑现支票次数
     private long chequeCashed;
    //发出gBZZ数
     private String gbzzSent;


    //24小时新增节点（含历史节点）
      private long beenodes24All;
    //24小时兑现支票次数
      private long cheque24Cashed;
    //24小时发出gBZZ数
      private String gbzz24Sent;
}
