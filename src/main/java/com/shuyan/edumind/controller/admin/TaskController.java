package com.shuyan.edumind.controller.admin;


import com.shuyan.edumind.base.BaseApiController;
import com.shuyan.edumind.base.RestResponse;
import com.shuyan.edumind.domain.TaskExam;
import com.shuyan.edumind.service.TaskExamService;
import com.shuyan.edumind.utility.DateTimeUtil;
import com.shuyan.edumind.viewmodel.admin.task.TaskPageRequestVM;
import com.shuyan.edumind.viewmodel.admin.task.TaskPageResponseVM;
import com.shuyan.edumind.viewmodel.admin.task.TaskRequestVM;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


@RestController("AdminTaskController")
@RequestMapping(value = "/api/admin/task")
public class TaskController extends BaseApiController {

    private final TaskExamService taskExamService;

    @Autowired
    public TaskController(TaskExamService taskExamService) {
        this.taskExamService = taskExamService;
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<Page<TaskPageResponseVM>> pageList(@RequestBody TaskPageRequestVM model) {
        PageRequest pageRequest = PageRequest.of(model.getPageIndex(), model.getPageSize());
        Page<TaskExam> pageInfo = taskExamService.findByGradeLevel(model.getGradeLevel(), pageRequest);
        Page<TaskPageResponseVM> page = pageInfo.map( m -> {
            TaskPageResponseVM vm = modelMapper.map(m, TaskPageResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(m.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<TaskRequestVM> select(@PathVariable Integer id) {
        TaskRequestVM vm = taskExamService.taskExamToVM(id);
        return RestResponse.ok(vm);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResponse edit(@RequestBody @Valid TaskRequestVM model) {
        taskExamService.edit(model, getCurrentUser());
        TaskRequestVM vm = taskExamService.taskExamToVM(model.getId());
        return RestResponse.ok(vm);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id) {
        taskExamService.deleteById(id);
        return RestResponse.ok();
    }
}
