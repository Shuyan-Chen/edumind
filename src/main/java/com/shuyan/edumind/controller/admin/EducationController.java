package com.shuyan.edumind.controller.admin;

import com.shuyan.edumind.base.BaseApiController;
import com.shuyan.edumind.base.RestResponse;
import com.shuyan.edumind.domain.Subject;
import com.shuyan.edumind.service.SubjectService;
import com.shuyan.edumind.viewmodel.admin.education.SubjectEditRequestVM;
import com.shuyan.edumind.viewmodel.admin.education.SubjectPageRequestVM;
import com.shuyan.edumind.viewmodel.admin.education.SubjectResponseVM;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("AdminEducationController")
@RequestMapping(value = "/api/admin/education")
public class EducationController extends BaseApiController {

    private final SubjectService subjectService;

    @Autowired
    public EducationController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @RequestMapping(value = "/subject/list", method = RequestMethod.POST)
    public RestResponse<List<Subject>> list() {
        List<Subject> subjects = subjectService.findAll();
        return RestResponse.ok(subjects);
    }

    @RequestMapping(value = "/subject/page", method = RequestMethod.POST)
    public RestResponse<Page<SubjectResponseVM>> pageList(@RequestBody SubjectPageRequestVM model) {
        Page<Subject> pageInfo = subjectService.page(model);
        Page<SubjectResponseVM> page = pageInfo.map( e -> modelMapper.map(e, SubjectResponseVM.class));
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/subject/edit", method = RequestMethod.POST)
    public RestResponse edit() {
        return edit(null);
    }

    @RequestMapping(value = "/subject/edit", method = RequestMethod.POST)
    public RestResponse edit(@RequestBody @Valid SubjectEditRequestVM model) {
        Subject subject = modelMapper.map(model, Subject.class);
        if (model.getId() == null) {
            subject.setDeleted(false);
            subjectService.insertByFilter(subject);
        } else {
            subjectService.updateByIdFilter(subject);
        }
        return RestResponse.ok();
    }

    @RequestMapping(value = "/subject/select/{id}", method = RequestMethod.POST)
    public RestResponse<SubjectEditRequestVM> select(@PathVariable Integer id) {
        Subject subject = subjectService.findById(id);
        SubjectEditRequestVM vm = modelMapper.map(subject, SubjectEditRequestVM.class);
        return RestResponse.ok(vm);
    }

    @RequestMapping(value = "/subject/delete/{id}", method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id) {
        Subject subject = subjectService.findById(id);
        subject.setDeleted(true);
        subjectService.updateByIdFilter(subject);
        return RestResponse.ok();
    }
}
