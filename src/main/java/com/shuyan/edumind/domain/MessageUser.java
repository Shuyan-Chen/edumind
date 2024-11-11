package com.shuyan.edumind.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_message_user")
public class MessageUser{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer messageId;

    private Integer receiveUserId;

    private String receiveUserName;

    private String receiveRealName;

    private Boolean readed;

    private Date createTime;

    private Date readTime;


}
