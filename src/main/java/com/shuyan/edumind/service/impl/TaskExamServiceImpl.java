package com.shuyan.edumind.service.impl;

import com.shuyan.edumind.domain.ExamPaper;
import com.shuyan.edumind.domain.TaskExam;
import com.shuyan.edumind.domain.TextContent;
import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.domain.task.TaskItemObject;
import com.shuyan.edumind.repository.ExamPaperRepository;
import com.shuyan.edumind.repository.TaskExamRepository;
import com.shuyan.edumind.service.TaskExamService;
import com.shuyan.edumind.service.TextContentService;
import com.shuyan.edumind.service.enums.ActionEnum;
import com.shuyan.edumind.utility.DateTimeUtil;
import com.shuyan.edumind.utility.JsonUtil;
import com.shuyan.edumind.utility.ModelMapperSingle;
import com.shuyan.edumind.viewmodel.admin.exam.ExamResponseVM;
import com.shuyan.edumind.viewmodel.admin.task.TaskPageRequestVM;
import com.shuyan.edumind.viewmodel.admin.task.TaskRequestVM;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskExamServiceImpl implements TaskExamService {

    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();
    private final TaskExamRepository taskExamRepository;
    private final TextContentService textContentService;
    private final ExamPaperRepository examPaperRepository;

    public TaskExamServiceImpl(TaskExamRepository taskExamRepository, TextContentService textContentService, ExamPaperRepository examPaperRepository) {
        this.taskExamRepository = taskExamRepository;
        this.textContentService = textContentService;
        this.examPaperRepository = examPaperRepository;
    }

    @Override
    public TaskExam findById(Integer id) {
        return taskExamRepository.findById(id).get();
    }

    @Override
    public Page<TaskExam> findByGradeLevel(Integer gradeLevel, Pageable pageable) {
        return taskExamRepository.findByGradeLevel(gradeLevel, pageable);
    }

    @Override
    public void deleteById(Integer id) {
        taskExamRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void edit(TaskRequestVM model, User user) {
        ActionEnum actionEnum = (model.getId() == null) ? ActionEnum.ADD : ActionEnum.UPDATE;
        TaskExam taskExam = null;
        if (actionEnum == ActionEnum.ADD) {
            Date now = new Date();
            taskExam = modelMapper.map(model, TaskExam.class);
            taskExam.setCreateUser(user.getId());
            taskExam.setCreateUserName(user.getUserName());
            taskExam.setCreateTime(now);
            taskExam.setDeleted(false);

            TextContent textContent = textContentService.jsonConvertInsert(model.getPaperItems(), now, p -> {
                TaskItemObject taskItemObject = new TaskItemObject();
                taskItemObject.setExamPaperId(p.getId());
                taskItemObject.setExamPaperName(p.getName());
                return taskItemObject;
            });
            textContentService.save(textContent);
            taskExam.setFrameTextContentId(textContent.getId());
            taskExamRepository.save(taskExam);
        } else {
            taskExam = taskExamRepository.findById(model.getId()).get();
            modelMapper.map(model, taskExam);

            TextContent textContent = textContentService.findById(taskExam.getFrameTextContentId());
            //清空试卷任务的试卷Id，后面会统一设置
            List<Integer> paperIds = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemObject.class)
                    .stream()
                    .map(d -> d.getExamPaperId())
                    .collect(Collectors.toList());
            examPaperRepository.deleteAllById(paperIds);

            //更新任务结构
            textContentService.jsonConvertUpdate(textContent, model.getPaperItems(), p -> {
                TaskItemObject taskItemObject = new TaskItemObject();
                taskItemObject.setExamPaperId(p.getId());
                taskItemObject.setExamPaperName(p.getName());
                return taskItemObject;
            });
            textContentService.save(textContent);
            taskExamRepository.save(taskExam);
        }

        List<Integer> paperIds = model.getPaperItems().stream().map(d -> d.getId()).collect(Collectors.toList());
        examPaperRepository.updateTaskPaper(taskExam.getId(), paperIds);
        model.setId(taskExam.getId());
    }

    @Override
    public TaskRequestVM taskExamToVM(Integer id) {
        TaskExam taskExam = taskExamRepository.findById(id).get();
        TaskRequestVM vm = modelMapper.map(taskExam, TaskRequestVM.class);
        TextContent textContent = textContentService.findById(taskExam.getFrameTextContentId());
        List<ExamResponseVM> examResponseVMS = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemObject.class).stream().map(tk -> {
            ExamPaper examPaper = examPaperRepository.findById(tk.getExamPaperId()).get();
            ExamResponseVM examResponseVM = modelMapper.map(examPaper, ExamResponseVM.class);
            examResponseVM.setCreateTime(DateTimeUtil.dateFormat(examPaper.getCreateTime()));
            return examResponseVM;
        }).collect(Collectors.toList());
        vm.setPaperItems(examResponseVMS);
        return vm;
    }

}
