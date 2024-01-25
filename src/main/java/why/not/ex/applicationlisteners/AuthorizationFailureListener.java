package why.not.ex.applicationlisteners;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFailureListener implements ApplicationListener<AuthorizationDeniedEvent> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthorizationDeniedEvent event) {
        System.out.printf(
                "%s failed to request \"%s %s\" cause %s %n",
                event.getAuthentication().get().getName(),
                request.getMethod(),
                request.getRequestURI(),
                event.getAuthorizationDecision().toString()
        );
    }
}