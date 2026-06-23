package com.talection.talection.devtools;

import com.talection.talection.enums.AuthProvider;
import com.talection.talection.enums.Gender;
import com.talection.talection.enums.Role;
import com.talection.talection.exception.UserAlreadyExistsException;
import com.talection.talection.model.userrelated.User;
import com.talection.talection.service.userrelated.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class AdminUserInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserService userService;
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private final Logger logger = LoggerFactory.getLogger(AdminUserInitializer.class);


    public AdminUserInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        initiateAdminUser();
    }

    private void initiateAdminUser() {
        String adminEmail = dotenv.get("ADMIN_USERNAME");
        String adminPassword = dotenv.get("ADMIN_PASSWORD");

        if (adminEmail != null && !adminEmail.isEmpty() &&
                adminPassword != null && !adminPassword.isEmpty()) {

            User user = new User();
            user.setGender(Gender.UNIDENTIFIED);
            user.setRole(Role.ADMIN);
            user.setFirstName("Admin");
            user.setLastName("User");
            user.setAuthProvider(AuthProvider.LOCAL);
            user.setEmail(adminEmail);

            try {
                userService.addUser(user, adminPassword);
                logger.info("Admin user initialized with email: {}", adminEmail);
            } catch (UserAlreadyExistsException e) {
                logger.info("Admin user already exists, skipping initialization");
            }
        } else {
            logger.error("Admin password or email is not set in the environment variables, skipping initialization");
        }
    }

}
