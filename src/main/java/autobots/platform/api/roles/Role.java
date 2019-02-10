package autobots.platform.api.roles;

import autobots.platform.api.common.Status;
import autobots.platform.api.organizations.Organization;
import autobots.platform.api.roles.groups.RoleGroup;
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
@Table(name = "roles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Role {

    public static final String ROLE_ADMIN = "admin";

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

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.ACTIVE;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    @JsonIgnore
    private List<RoleGroup> roleGroups;

    private String name;
    private String description;

    @Transient
    public Long organization_id;

}
