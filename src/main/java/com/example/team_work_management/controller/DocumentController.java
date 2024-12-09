package com.example.team_work_management.controller;

import com.example.team_work_management.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/groups")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PreAuthorize("@groupSecurityService.checkUserPermissionForTask(#idTask, authentication.principal.id)")
    @PostMapping("/{idGroup}/work-group/{idWG}/tasks/{idTask}/documents")
    public ResponseEntity<?> add(@RequestParam("file")MultipartFile file, @PathVariable Long idTask) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded or file is empty");
        }
        documentService.save(file, idTask);
        return ResponseEntity.ok("Document upload successfully");
    }

    @PreAuthorize("@groupSecurityService.checkUserPermissionForTask(#idTask, authentication.principal.id)")
    @DeleteMapping("/{idGroup}/work-group/{idWG}/tasks/{idTask}/documents/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @PathVariable Long idTask){
        documentService.delete(id);
        return ResponseEntity.ok("Document delete successfully");
    }

    @PreAuthorize("@groupSecurityService.hasInGroup(#idGroup, authentication.principal.id)")
    @GetMapping("/{idGroup}/work-group/{idWG}/tasks/{idTask}/documents/{id}")
    public ResponseEntity<?> download(@PathVariable Long id, @PathVariable Long idGroup){
        documentService.downloadFile(id);
        return ResponseEntity.ok("Document download successfully");
    }

    @PreAuthorize("@groupSecurityService.hasInGroup(#idGroup, authentication.principal.id)")
    @GetMapping("/{idGroup}/documents")
    public ResponseEntity<?> getAll(@PathVariable Long idGroup){
        return ResponseEntity.ok(documentService.getALlByGroup(idGroup));
    }

}
