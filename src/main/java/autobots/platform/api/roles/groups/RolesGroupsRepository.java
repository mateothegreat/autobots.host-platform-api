package autobots.platform.api.roles.groups;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesGroupsRepository extends PagingAndSortingRepository<RoleGroup, Long> {

}
