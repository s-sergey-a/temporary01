package why.not.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("rest/admin/")
public class AdminController {

    @Autowired
    private UserDetailsService userDet;

    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;

    @GetMapping("users")
    public Object getCurrentUser() {
        Field usersMapField = ReflectionUtils.findField(InMemoryUserDetailsManager.class, "users");
        ReflectionUtils.makeAccessible(usersMapField);
        HashMap<String, UserDetails> map = (HashMap<String, UserDetails>) ReflectionUtils.getField(usersMapField, userDet);
        return map.values();
    }

    @PutMapping("users/{login}/availability")
    public void toggleAvailability(@PathVariable String login, @RequestBody Boolean state) {
        InMemoryUserDetailsManager detMan = (InMemoryUserDetailsManager) this.userDet;
        UserDetails user = detMan.loadUserByUsername(login);
        detMan.updateUser(
                User.withUserDetails(user)
                .disabled(!state)
                .build()
        );
    }

    @GetMapping("sessions")
    public Object getSessions() {
        return sessionRegistry.getAllPrincipals().stream()
                .flatMap(principal -> sessionRegistry.getAllSessions(principal, true).stream())
                .toList();
    }

    @DeleteMapping("sessions/{sessionId}")
    public void deleteSession(@PathVariable String sessionId) {
        SessionInformation session = sessionRegistry.getSessionInformation(sessionId);
        if (session.isExpired()) {
            sessionRegistry.removeSessionInformation(sessionId);
        } else {
            session.expireNow();
        }
    }
}