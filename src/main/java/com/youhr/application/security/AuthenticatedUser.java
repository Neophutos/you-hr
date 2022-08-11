package com.youhr.application.security;

import com.youhr.application.data.entity.User;
import com.youhr.application.data.repository.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

/**
 * @desc AuthenticatedUser stellt die Authentifizierung eines Nutzers während der Nutzung des Systems sicher.
 *
 * @category Security
 * @author Ben Köppe, Tim Freund
 * @version 1.0
 * @since 2022-08-05
 */
@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticatedUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Optional<Authentication> getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
    }

    public Optional<User> get() {
        return getAuthentication().map(authentication -> userRepository.findByUsername(authentication.getName()));
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(SecurityConfiguration.LOGOUT_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }

}
