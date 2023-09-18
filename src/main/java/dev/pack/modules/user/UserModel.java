package dev.pack.modules.user;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.pack.enums.*;
import dev.pack.model.TimeStamps;
import dev.pack.utils.CustomDateSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Table(name = "user_tbl")
@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel extends TimeStamps implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Roles role;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "join_at")
    private Date joinAt;

    @PrePersist
    protected void onUserJoin(){
        this.joinAt = new Date();
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(Roles.SUPERADMIN.name()));
        authorities.add(new SimpleGrantedAuthority(Roles.ADMIN_TKJ.name()));
        authorities.add(new SimpleGrantedAuthority(Roles.ADMIN_TKR.name()));
        authorities.add(new SimpleGrantedAuthority(Roles.ADMIN_TKJ.name()));
        authorities.add(new SimpleGrantedAuthority(Roles.ADMIN_TKJ.name()));

        return authorities;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return this.password;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
