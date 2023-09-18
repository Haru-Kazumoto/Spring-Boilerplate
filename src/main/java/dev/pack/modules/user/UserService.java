package dev.pack.modules.user;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    Iterable<UserModel> getAllUser(Pageable pageable);
    UserModel createUser(UserModel body);
    Iterable<UserModel> createBatch(Iterable<UserModel> users);

}
