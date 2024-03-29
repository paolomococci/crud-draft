package local.example.hail.security;

import java.util.List;
import java.util.stream.Collectors;

import local.example.hail.data.entity.User;
import local.example.hail.data.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl
        implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(
                    "No user present with username: " + username
            );
        } else {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getHashedPassword(),
                    getAuthorities(user)
            );
        }
    }

    private static List<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName())
        ).collect(Collectors.toList());

    }

}
