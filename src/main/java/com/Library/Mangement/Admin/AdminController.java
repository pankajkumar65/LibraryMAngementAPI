package com.Library.Mangement.Admin;

import com.Library.Mangement.user.Role;
import com.Library.Mangement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/handle-author/{authorId}")
    public ResponseEntity<String> handleAuthorRequest(
            @PathVariable Long authorId,
            @RequestParam String action) {

        // Get the authenticated admin's ID
        Long adminId = getAuthenticatedAdminId();
        if (adminId == null) {
            return ResponseEntity.status(403).body("Access Denied: Only admins can approve or reject authors.");
        }

        // Decide whether to approve or reject based on action parameter
        String result;
        if (action.equalsIgnoreCase("approve")) {
            result = adminService.approveAuthor(authorId);
        } else if (action.equalsIgnoreCase("reject")) {
            result = adminService.rejectAuthor(authorId);
        } else {
            return ResponseEntity.badRequest().body("Invalid action. Please specify 'approve' or 'reject'.");
        }

        return ResponseEntity.ok(result);
    }

    // Helper method to retrieve authenticated admin's ID
    private Long getAuthenticatedAdminId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            User admin = (User) auth.getPrincipal();
            if (admin.getRole().equals(Role.ADMIN)) { // Check if the user is an admin
                return admin.getId();
            }
        }
        return null; // Return null if not an admin
    }
}
