package com.shuyan.edumind.controller.student;

import com.shuyan.edumind.base.BaseApiController;
import com.shuyan.edumind.base.RestResponse;
import com.shuyan.edumind.domain.ExamPaperQuestionCustomerAnswer;
import com.shuyan.edumind.domain.Subject;
import com.shuyan.edumind.domain.TextContent;
import com.shuyan.edumind.domain.question.QuestionObject;
import com.shuyan.edumind.service.ExamPaperQuestionCustomerAnswerService;
import com.shuyan.edumind.service.QuestionService;
import com.shuyan.edumind.service.SubjectService;
import com.shuyan.edumind.service.TextContentService;
import com.shuyan.edumind.utility.DateTimeUtil;
import com.shuyan.edumind.utility.HtmlUtil;
import com.shuyan.edumind.utility.JsonUtil;
import com.shuyan.edumind.viewmodel.admin.question.QuestionEditRequestVM;
import com.shuyan.edumind.viewmodel.student.exam.ExamPaperSubmitItemVM;
import com.shuyan.edumind.viewmodel.student.question.answer.QuestionAnswerVM;
import com.shuyan.edumind.viewmodel.student.question.answer.QuestionPageStudentRequestVM;
import com.shuyan.edumind.viewmodel.student.question.answer.QuestionPageStudentResponseVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController("StudentQuestionAnswerController")
@RequestMapping(value = "/api/student/question/answer")
public class QuestionAnswerController extends BaseApiController {

    private final ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService;
    private final QuestionService questionService;
    private final TextContentService textContentService;
    private final SubjectService subjectService;

    @Autowired
    public QuestionAnswerController(ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService, QuestionService questionService, TextContentService textContentService, SubjectService subjectService) {
        this.examPaperQuestionCustomerAnswerService = examPaperQuestionCustomerAnswerService;
        this.questionService = questionService;
        this.textContentService = textContentService;
        this.subjectService = subjectService;
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<Page<QuestionPageStudentResponseVM>> pageList(@RequestBody QuestionPageStudentRequestVM model) {
        model.setCreateUser(getCurrentUser().getId());
        Pageable pageable = PageRequest.of(model.getPageIndex(),model.getPageSize());
        Page<ExamPaperQuestionCustomerAnswer> pageInfo = examPaperQuestionCustomerAnswerService.studentPage(model, pageable);

        Page<QuestionPageStudentResponseVM> page = pageInfo.map( q -> {
            Subject subject = subjectService.findById(q.getSubjectId());
            QuestionPageStudentResponseVM vm = modelMapper.map(q, QuestionPageStudentResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(q.getCreateTime()));
            TextContent textContent = textContentService.findById(q.getQuestionTextContentId());
            QuestionObject questionObject = JsonUtil.toJsonObject(textContent.getContent(), QuestionObject.class);
            String clearHtml = HtmlUtil.clear(questionObject.getTitleContent());
            vm.setShortTitle(clearHtml);
            vm.setSubjectName(subject.getName());
            return vm;
        });

        return RestResponse.ok(page);
    }


    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<QuestionAnswerVM> select(@PathVariable Integer id) {
        QuestionAnswerVM vm = new QuestionAnswerVM();
        ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer = examPaperQuestionCustomerAnswerService.findById(id);
        ExamPaperSubmitItemVM questionAnswerVM = examPaperQuestionCustomerAnswerService.examPaperQuestionCustomerAnswerToVM(examPaperQuestionCustomerAnswer);
        QuestionEditRequestVM questionVM = questionService.getQuestionEditRequestVM(examPaperQuestionCustomerAnswer.getQuestionId());
        vm.setQuestionVM(questionVM);
        vm.setQuestionAnswerVM(questionAnswerVM);
        return RestResponse.ok(vm);
    }

}
