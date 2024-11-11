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
@Table(name = "t_user_event_log")
public class UserEventLog{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private String userName;

    private String realName;

    private String content;

    private Date createTime;


    public UserEventLog(Integer id, String userName, String realName, Date date) {
    }
}
