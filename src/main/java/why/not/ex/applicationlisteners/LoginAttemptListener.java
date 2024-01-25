package why.not.ex.applicationlisteners;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LoginAttemptListener implements ApplicationListener<AbstractAuthenticationEvent> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        if (event instanceof AuthenticationSuccessEvent successEvent) {
            System.out.printf(
                    "%s successful logged in (roles: %s)%n",
                    successEvent.getAuthentication().getName(),
                    successEvent.getAuthentication().getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(", "))
            );
        } else if (event instanceof AbstractAuthenticationFailureEvent failureEvent) {
            System.out.printf(
                    "%s failed to log in: %s%n",
                    failureEvent.getAuthentication().getName(),
                    failureEvent.getException().getMessage()
            );
        }
    }
}