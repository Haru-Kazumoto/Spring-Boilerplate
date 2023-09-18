package dev.pack.modules.user;

import dev.pack.config.AppConfig;
import dev.pack.exception.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final PagingRepository pagingRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder password;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }

    @Override
    public Iterable<UserModel> getAllUser(Pageable pageable) {
        return this.pagingRepository.findAll(pageable);
    }

    @Override
    @Transactional(rollbackOn = BadRequestException.class)
    public UserModel createUser(UserModel body) {
        //Validating
        this.userRepository.findByUsername(body.getUsername())
                .ifPresent(username -> {
                    throw new DuplicateDataException("Username already exists.");
                });
        this.userRepository.findByEmail(body.getEmail())
                .ifPresent(email -> {
                    throw new DuplicateDataException("Email already exists.");
                });

        //Check is user with the same major has already 2 user
        long usersWithSameRoleCount = this.userRepository.countByRole(body.getRole());
        if(usersWithSameRoleCount >= 2){
            throw new BadRequestException(String.format("User with role %s has reached the specified maximum limit!", body.getRole()));
        }

        //Hash password
        String hashedPassword = this.password.encode(body.getPassword());
        body.setPassword(hashedPassword);

        return this.userRepository.save(body);
    }

    @Override
    public Iterable<UserModel> createBatch(Iterable<UserModel> users) {
        for (UserModel datas : users){
            //Validating
            this.userRepository.findByUsername(datas.getUsername())
                    .ifPresent(username -> {
                        throw new DuplicateDataException("Username already exists.");
                    });
            this.userRepository.findByEmail(datas.getEmail())
                    .ifPresent(email -> {
                        throw new DuplicateDataException("Email already exists.");
                    });

            //Check is user with the same major has already 2 user
            long usersWithSameRoleCount = this.userRepository.countByRole(datas.getRole());
            if(usersWithSameRoleCount >= 2){
                throw new BadRequestException(String.format("User with role %s has reached the specified maximum limit!", datas.getRole()));
            }

            //Hash password
            String hashedPassword = this.password.encode(datas.getPassword());
            datas.setPassword(hashedPassword);
        }

        return userRepository.saveAll(users);
    }
}
