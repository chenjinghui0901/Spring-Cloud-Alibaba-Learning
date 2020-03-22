package com.lebron.usercenter.domain.entity.rocketmq;

import java.util.Date;
import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@Table(name = "rocketmq_transaction_log")
public class RocketmqTransactionLog {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 事务id
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 事务id
     */
    private String log;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}