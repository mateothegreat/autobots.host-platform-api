package autobots.platform.api.bots;

import autobots.platform.api.bots.environment.BotEnvironment;
import autobots.platform.api.common.Status;
import autobots.platform.api.users.User;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "bots")
public class Bot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Range(max = 4294967295L)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    private UUID uuid;
    private UUID token;

    private Status status;

    @OneToOne
    private User user;

    @OneToMany
    private List<BotEnvironment> environments;

    private String name;
    private String description;
    private String gitUrl;

}
