package autobots.platform.api.bots.environment;

import autobots.platform.api.bots.Bot;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bot_environment_variables")
public class BotEnvironment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Range(max = 4294967295L)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne
    private Bot bot;

    private String name;
    private String value;

}
