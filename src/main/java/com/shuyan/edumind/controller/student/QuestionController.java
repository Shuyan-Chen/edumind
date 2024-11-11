package com.shuyan.edumind.controller.student;

import com.shuyan.edumind.base.BaseApiController;
import com.shuyan.edumind.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("StudentQuestionController")
@RequestMapping(value = "/api/student/question")
public class QuestionController extends BaseApiController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
}
