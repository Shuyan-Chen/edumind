package com.shuyan.edumind.service;

import com.shuyan.edumind.domain.TaskExam;
import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.viewmodel.admin.task.TaskPageRequestVM;
import com.shuyan.edumind.viewmodel.admin.task.TaskRequestVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskExamService {

    TaskExam findById(Integer id);

    Page<TaskExam> findByGradeLevel(Integer gradeLevel, Pageable pageable);

    void deleteById(Integer id);

    void edit(TaskRequestVM model, User user);

    TaskRequestVM taskExamToVM(Integer id);

}
