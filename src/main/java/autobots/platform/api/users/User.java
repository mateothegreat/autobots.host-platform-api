package autobots.platform.api.users;

import autobots.platform.api.common.Status;
import autobots.platform.api.organizations.Organization;
import autobots.platform.api.roles.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Range(max = 4294967295L)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    private UUID uuid;
    private UUID token;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private Organization organization;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    @JsonIgnore
    private List<Role> roles;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public String email;
    public String username;

    @JsonIgnore
    private String password;
    public  String firstname;
    public  String lastname;

    @Transient
    public Long organization_id;

    @CreationTimestamp
    private LocalDateTime stampCreated;

    public LocalDateTime stampLastLogin;

    @JsonIgnore
    private String passwordResetToken;

    @JsonIgnore
    private String confirmEmailToken;

    @JsonIgnore
    private Boolean isConfirmed;
    @JsonIgnore
    private Boolean isAdmin;

    @JsonIgnore
    private Boolean enabled = true;

    @JsonIgnore
    private Boolean permissionUsersManage = true;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return null;
//    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
