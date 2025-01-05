package com.example.team_work_management.scheduler;

import com.example.team_work_management.entity.Task;
import com.example.team_work_management.service.EmailService;
import com.example.team_work_management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class OverdueTaskScheduler {

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 01 0 * * ?")
    public void markOverdueTasks() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        for(Task task : taskService.getByDelayed(yesterday)){
            String title ="[Pandoras] Notification: Task Delayed - " + task.getName();
            emailService.sendSimpleMessageHtml(task.getAssignee().getEmail(), title, createHtmlToEmail(task));
        }
        taskService.updateTasksAsDelayed(yesterday);
    }
    private String formatDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");
        return date.format(formatter);
    }

    private String createHtmlToEmail(Task task){
        String priorityColor = getPriorityColor(task.getPriorityLevel().getId());
        String taskUrl = "http://localhost:3000/group/" + task.getWorkGroup().getGroup().getId() + "/board/" +
                task.getWorkGroup().getId() + "/tasks/" + task.getId();

        return "<html>\n" +
                "  <body>\n" +
                "    <h2 style=\"color: #4CAF50;\">Task Delay Notification</h2>\n" +
                "    <p>Dear User,</p>\n" +
                "    <p>We would like to inform you that a task you are following has been delayed.</p>\n" +
                "\n" +
                "    <h3 style=\"color: #007bff;\">Task Details:</h3>\n" +
                "    <ul>\n" +
                "      <li><strong>Task Name:</strong> " + task.getName() +"</li>\n" +
                "      <li><strong>Priority:</strong> <span style=\"color: " + priorityColor + ";\">" + task.getPriorityLevel().getName() + "</span></li>\n" +
                "      <li><strong>Start Date:</strong> " + formatDateToString(task.getStartDate()) + "</li>\n" +
                "      <li><strong>Due Date:</strong> " + formatDateToString(task.getEndDate()) + "</li>\n" +
                "    </ul>\n" +
                "\n" +
                "    <p style=\"color: red; font-weight: bold;\">This task is now overdue and requires your attention.</p>\n" +
                "\n" +
                "    <p>You can view and manage the task by clicking the link below:</p>\n" +
                "    <a href=\""+ taskUrl + "\" style=\"color: #007bff;\">Go to Task</a>\n" +
                "\n" +
                "    <br><br>\n" +
                "    <p>Thank you for using Pandoras.</p>\n" +
                "    <footer>\n" +
                "      <p style=\"font-size: 0.8em; color: #777;\">Pandoras - All rights reserved.</p>\n" +
                "    </footer>\n" +
                "  </body>\n" +
                "</html>\n";
    }
    private String getPriorityColor(Long priorityLevel) {
        return switch (priorityLevel.intValue()) {
            case 1 -> "#FF5722";
            case 2 -> "#F0A500";
            case 3 -> "#008000";
            default -> "black";
        };
    }
}
