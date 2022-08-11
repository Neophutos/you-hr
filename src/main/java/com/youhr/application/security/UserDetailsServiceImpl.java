package com.youhr.application.security;

import com.youhr.application.data.entity.User;
import com.youhr.application.data.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @desc UserDetailsServiceImpl ist für die Überprüfung und Authentifikation eines Login-Versuchs verantwortlich.
 *
 * @category Security
 * @author Ben Köppe, Tim Freund, Riccardo Prochnow
 * @version 1.0
 * @since 2022-06-30
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @desc loadUserByUsername fragt die Datenbank nach Verfügbarkeit des eingegebenen Nutzers nach.
     * @param username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Kein Nutzer mit dem Namen " + username + " gefunden.");
        } else {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getHashedPassword(),
                    getAuthorities(user));
        }
    }

    /**
     * @desc getAuthorities überprüft die vergebenen Rollen des eingeloggten Nutzers, um unberechtigten Zugriff auf verbotene Klassen zu verhindern.
     * @param user
     */
    private static List<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

}
