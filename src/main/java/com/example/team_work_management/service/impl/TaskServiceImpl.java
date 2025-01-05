package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.*;
import com.example.team_work_management.exception.error.TaskNotFoundException;
import com.example.team_work_management.repository.ITaskRepository;
import com.example.team_work_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class TaskServiceImpl implements TaskService {

    private final String content_assignment = "đã giao cho bạn một nhiệm vụ";
    private final String content_edit = "đã chỉnh sửa nhiệm vụ";
    private final String content_delete = "đã thu hồi nhiệm vụ";
    private final String content_update = "đã cập nhật trạng thái nhiệm vụ";
    private final String content_change_person = "đã giao nhiệm vụ cho thành viên khác";

    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PriorityLevelService priorityLevelService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkGroupService workGroupService;

    @Autowired
    private TaskInterestService taskInterestService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Override
    public Task getById(Long id) {
        return taskRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    @Override
    @Transactional
    public boolean add(Task task) {
        task.setDeleted(false);
        task.setDelay(task.getEndDate().isBefore(LocalDate.now()));
        task.setPriorityLevel(priorityLevelService.getById(task.getPriorityLevel().getId()));
        task.setStatus(statusService.getById(2L));
        task.setWorkGroup(workGroupService.getById(task.getWorkGroup().getId()));

        if(task.getParentTask() != null){
            task.setParentTask(taskService.getById(task.getParentTask().getId()));
        }

        User assignee = authService.getDetail(task.getAssignee().getId());
        task.setAssignee(assignee);

        //Save task
        Task saveTask = taskRepository.save(task);

        //Create Task Interest
        User manager = authService.getCurrentAuthenticatedUser();
        taskInterestService.save(TaskInterest.builder().task(saveTask).user(manager).build());

        if(!manager.getId().equals(assignee.getId())){
            taskInterestService.save(TaskInterest.builder().task(saveTask).user(assignee).build());
            notificationService.send(saveTask, manager, assignee, content_assignment);
            sendTaskNotification(saveTask, saveTask.getAssignee().getEmail(),"The task has been assigned to you.");
        }

        return true;
    }

    @Transactional
    @Override
    public boolean edit(Task task, Long id) {
        Task taskEdit = getById(id);
        taskEdit.setName(task.getName());
        taskEdit.setDes(task.getDes());
        taskEdit.setPriorityLevel(priorityLevelService.getById(task.getPriorityLevel().getId()));
        taskEdit.setStartDate(task.getStartDate());
        taskEdit.setEndDate(task.getEndDate());

        if(!task.getEndDate().isBefore(LocalDate.now())){
            taskEdit.setDelay(false);
        }else if(!taskEdit.getStatus().getName().equalsIgnoreCase("Completed")){
            taskEdit.setDelay(true);
        }else if(!task.getEndDate().isBefore(taskEdit.getCompletedDate())){
            taskEdit.setDelay(false);
        }

        if(!task.getAssignee().getId().equals(taskEdit.getAssignee().getId())){
            processEdit(taskEdit, authService.getDetail(task.getAssignee().getId()));
        }else{
            processEdit(taskEdit);
        }
        return true;
    }

    @Override
    public boolean delete(Long id) {
        Task deleteTask = getById(id);
        deleteTask.setDeleted(true);
        taskRepository.save(deleteTask);

        User manager = authService.getCurrentAuthenticatedUser();
        notificationService.sendGroup(deleteTask, manager, taskInterestService.getByTaskAndExcludeUser(TaskInterest.builder().task(deleteTask).user(manager).build()), content_delete);
        sendTaskNotification(deleteTask, deleteTask.getAssignee().getEmail(),"The task has been revoked.");
        return true;
    }

    @Override
    public boolean isTaskAssignedToUser(Long id, Long assigneeId) {
        return taskRepository.existsByIdAndAssigneeId(id, assigneeId);
    }

    @Override
    public boolean updateStatus(Long id, Long statusId) {
        Task task = taskRepository.getById(id);
        Status status_currently = task.getStatus();
        if(!statusId.equals(status_currently.getId())){
            Status status_update = statusService.getById(statusId);
            task.setStatus(status_update);
            if(status_update.getName().equalsIgnoreCase("Completed")){
                task.setCompletedDate(LocalDate.now());
            }
            taskRepository.save(task);
            User assignee = authService.getCurrentAuthenticatedUser();
            notificationService.sendGroup(task, assignee, taskInterestService.getByTaskAndExcludeUser(TaskInterest.builder().task(task).user(assignee).build()), content_update);
        }

        return true;
    }

    @Override
    public List<Task> getByAssignee(Long assigneeId) {
        return taskRepository.findByIsDeletedFalseAndAssigneeIdAndGroupNotClosed(assigneeId);
    }

    @Override
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllTasksByUserId(userId);
    }

    @Override
    public List<Task> getAllByGroupId(Long groupId) {
        return taskRepository.findAllTasksByGroupId(groupId);
    }

    @Override
    public boolean hasUserTasksInGroup(Long userId, Long groupId) {
        Long taskCount = taskRepository.countTasksByUserInGroup(userId, groupId);
        return taskCount > 0;
    }

    @Override
    public List<Task> getByStatusAndGroup(Long idGroup, Long idStatus) {
        return taskRepository.findByStatusAndGroup(idGroup, idStatus);
    }

    @Override
    public List<Task> getByUserAndGroup(Long idUser, Long idGroup) {
        return taskRepository.findByUserAndGroup(idUser, idGroup);
    }

    @Override
    public List<Task> getByEndDate(LocalDate endDate) {
        return taskRepository.findTasksByEndDate(endDate);
    }

    @Override
    public void updateTasksAsDelayed(LocalDate yesterday) {
        taskRepository.updateIsDelayForTasks(yesterday);
    }

    @Override
    public List<Task> getByDelayed(LocalDate yesterday) {
        return taskRepository.findTaskDelayed(yesterday);
    }

    private void processEdit(Task task){
        Task saveTask = taskRepository.save(task);
        User manager = authService.getCurrentAuthenticatedUser();
        notificationService.sendGroup(saveTask, manager, taskInterestService.getByTaskAndExcludeUser(TaskInterest.builder().task(saveTask).user(manager).build()), content_edit);
    }

    private void processEdit(Task task, User substitute){
        User assignee_currently = task.getAssignee();
        task.setAssignee(substitute);
        Task saveTask = taskRepository.save(task);

        User manager = authService.getCurrentAuthenticatedUser();
        if(!assignee_currently.getId().equals(manager.getId())){
            taskInterestService.delete(TaskInterest.builder().task(saveTask).user(assignee_currently).build());
            notificationService.send(task, manager, assignee_currently, content_change_person);
            sendTaskNotification(saveTask, assignee_currently.getEmail(),"The task has been assigned to another member.");
        }
        notificationService.sendGroup(saveTask, manager, taskInterestService.getByTaskAndExcludeUser(TaskInterest.builder().task(saveTask).user(manager).build()), content_edit);
        if(!substitute.getId().equals(manager.getId())){
            notificationService.send(task, manager, substitute, content_assignment);
            taskInterestService.save(TaskInterest.builder().task(saveTask).user(substitute).build());
            sendTaskNotification(saveTask, substitute.getEmail(),"The task has been assigned to you.");
        }

    }

    private String formatDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");
        return date.format(formatter);
    }

    private String getPriorityColor(Long priorityLevel) {
        return switch (priorityLevel.intValue()) {
            case 1 -> "#FF5722";
            case 2 -> "#F0A500";
            case 3 -> "#008000";
            default -> "black";
        };
    }

    private String createHtmlToEmail(Task task, String message) {
        String priorityColor = getPriorityColor(task.getPriorityLevel().getId());
        String taskUrl = "http://localhost:3000/group/" + task.getWorkGroup().getGroup().getId() + "/board/" +
                task.getWorkGroup().getId() + "/tasks/" + task.getId();

        String htmlUrl = "";
        if (!message.equalsIgnoreCase("The task has been revoked.")) {
            htmlUrl = "    <p>You can view and manage the task by clicking the link below:</p>\n" +
                    "    <a href=\"" + taskUrl + "\" style=\"color: #007bff;\">Go to Task</a>\n" +
                    "\n";
        }

        // Xây dựng nội dung email
        return "<html>\n" +
                "  <body>\n" +
                "    <h2 style=\"color: #4CAF50;\">Task Notification</h2>\n" +
                "    <p>Dear User,</p>\n" +
                "    <p>" + message + "</p>\n" +
                "\n" +
                "    <h3 style=\"color: #007bff;\">Task Details:</h3>\n" +
                "    <ul>\n" +
                "      <li><strong>Task Name:</strong> " + task.getName() + "</li>\n" +
                "      <li><strong>Priority:</strong> <span style=\"color: " + priorityColor + ";\">" + task.getPriorityLevel().getName() + "</span></li>\n" +
                "      <li><strong>Start Date:</strong> " + formatDateToString(task.getStartDate()) + "</li>\n" +
                "      <li><strong>Due Date:</strong> " + formatDateToString(task.getEndDate()) + "</li>\n" +
                "    </ul>\n" +
                "\n" +
                htmlUrl +
                "    <br><br>\n" +
                "    <p>Thank you for using Pandoras.</p>\n" +
                "    <footer>\n" +
                "      <p style=\"font-size: 0.8em; color: #777;\">Pandoras - All rights reserved.</p>\n" +
                "    </footer>\n" +
                "  </body>\n" +
                "</html>\n";
    }


    private void sendTaskNotification(Task task, String email,String message){
        String title = "[Pandoras] Notification: Task - " + task.getName();
        String emailContent = createHtmlToEmail(task, message);
        emailService.sendSimpleMessageHtml(email, title, emailContent);
    }

}
