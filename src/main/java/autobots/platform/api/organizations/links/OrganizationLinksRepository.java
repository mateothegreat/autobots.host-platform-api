package autobots.platform.api.organizations.links;

import autobots.platform.api.organizations.Organization;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationLinksRepository extends PagingAndSortingRepository<OrganizationLink, Long> {

    int deleteByChildAndParent(Organization child, Organization parent);

}
