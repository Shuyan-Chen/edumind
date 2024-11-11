package com.shuyan.edumind.controller.admin;

import com.shuyan.edumind.base.BaseApiController;
import com.shuyan.edumind.base.RestResponse;
import com.shuyan.edumind.domain.ExamPaperAnswer;
import com.shuyan.edumind.domain.Subject;
import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.service.ExamPaperAnswerService;
import com.shuyan.edumind.service.SubjectService;
import com.shuyan.edumind.service.UserService;
import com.shuyan.edumind.utility.DateTimeUtil;
import com.shuyan.edumind.utility.ExamUtil;
import com.shuyan.edumind.viewmodel.admin.paper.ExamPaperAnswerPageRequestVM;
import com.shuyan.edumind.viewmodel.student.exampaper.ExamPaperAnswerPageResponseVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController("AdminExamPaperAnswerController")
@RequestMapping(value = "/api/admin/examPaperAnswer")
public class ExamPaperAnswerController extends BaseApiController {

    private final ExamPaperAnswerService examPaperAnswerService;
    private final SubjectService subjectService;
    private final UserService userService;

    @Autowired
    public ExamPaperAnswerController(ExamPaperAnswerService examPaperAnswerService, SubjectService subjectService, UserService userService) {
        this.examPaperAnswerService = examPaperAnswerService;
        this.subjectService = subjectService;
        this.userService = userService;
    }


    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<Page<ExamPaperAnswerPageResponseVM>> pageJudgeList(@RequestBody ExamPaperAnswerPageRequestVM model) {
        Page<ExamPaperAnswer> pageInfo = examPaperAnswerService.adminPage(model);
        Page<ExamPaperAnswerPageResponseVM> page = pageInfo.map( e -> {
            ExamPaperAnswerPageResponseVM vm = modelMapper.map(e, ExamPaperAnswerPageResponseVM.class);
            Subject subject = subjectService.findById(vm.getSubjectId());
            vm.setDoTime(ExamUtil.secondToVM(e.getDoTime()));
            vm.setSystemScore(ExamUtil.scoreToVM(e.getSystemScore()));
            vm.setUserScore(ExamUtil.scoreToVM(e.getUserScore()));
            vm.setPaperScore(ExamUtil.scoreToVM(e.getPaperScore()));
            vm.setSubjectName(subject.getName());
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            User user = userService.findById(e.getCreateUser());
            vm.setUserName(user.getUserName());
            return vm;
        });
        return RestResponse.ok(page);
    }


}
