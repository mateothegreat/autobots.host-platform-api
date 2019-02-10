package autobots.platform.api.roles.groups;

import autobots.platform.api.common.Status;
import autobots.platform.api.organizations.Organization;
import autobots.platform.api.roles.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "roles_groups")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class RoleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Range(max = 4294967295L)
    @Column(nullable = false, updatable = false, unique = true)
    public Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    @JsonIgnore
    private Organization organization;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    @JsonIgnore
    private List<Role> roles;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.ACTIVE;

    private String name;
    private String description;

    @Transient
    public Long organization_id;

}
