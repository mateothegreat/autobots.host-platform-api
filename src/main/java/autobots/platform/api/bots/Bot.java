package autobots.platform.api.bots;

import autobots.platform.api.users.User;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
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

    private BotStatus status;

    @OneToOne
    private User user;

    private String name;
    private String description;
    private String gitUrl;

}
