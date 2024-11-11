package com.shuyan.edumind.viewmodel.admin.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MessageSendVM {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    @Size(min = 1, message = "接收人不能为空")
    private List<Integer> receiveUserIds;
}

