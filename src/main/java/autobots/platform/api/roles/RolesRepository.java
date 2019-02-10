package autobots.platform.api.roles;

import autobots.platform.api.roles.groups.RoleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends PagingAndSortingRepository<Role, Long> {

    Page<Role> getByOrganization_id(Long organizationId, Pageable pageable);

    Page<Role> getByRoleGroupsContains(RoleGroup roleGroup, Pageable pageable);

}
