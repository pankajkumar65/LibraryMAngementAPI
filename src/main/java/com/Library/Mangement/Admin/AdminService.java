package com.Library.Mangement.Admin;

import com.Library.Mangement.Email.EmailService;
import com.Library.Mangement.user.Role;
import com.Library.Mangement.user.User;
import com.Library.Mangement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public Optional<User> assignApprovalToAdmin() {
        List<User> admins = userRepository.findByRole(Role.ADMIN);  // Get all admins
        if (admins.isEmpty()) {
            return Optional.empty();  // No admins found
        }
        // Select a random admin
        Random random = new Random();
        User selectedAdmin = admins.get(random.nextInt(admins.size()));
        return Optional.of(selectedAdmin);
    }

    // Approve pending author request
    public String approveAuthor(Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        if (author.getRole() != Role.PENDING_AUTHOR) {
            return "This user is not awaiting approval as an author.";
        }

        author.setRole(Role.AUTHOR);  // Approve author by changing role
        userRepository.save(author);

        // Notify the author via email
        emailService.sendEmail(author.getEmail(), "Author Account Approved", "Your account has been approved as an AUTHOR!");

        return "Author account approved successfully!";
    }

    // Reject pending author request
    public String rejectAuthor(Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        if (author.getRole() != Role.PENDING_AUTHOR) {
            return "This user is not awaiting approval as an author.";
        }

        userRepository.delete(author);  // Reject and delete the account

        // Notify the author via email
        emailService.sendEmail(author.getEmail(), "Author Account Rejected", "Your request to become an AUTHOR has been rejected.");

        return "Author account rejected successfully!";
    }
}
