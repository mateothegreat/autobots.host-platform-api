package autobots.platform.api.organizations.links;

import autobots.platform.api.organizations.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationLinksService {

    private OrganizationLinksRepository organizationLinksRepository;

    @Autowired
    public OrganizationLinksService(final OrganizationLinksRepository organizationLinksRepository) {

        this.organizationLinksRepository = organizationLinksRepository;

    }

    public OrganizationLink createLink(Organization parent, Organization child) {

        OrganizationLink organizationLink = new OrganizationLink();

        organizationLink.setParent(parent);
        organizationLink.setChild(child);

        return organizationLinksRepository.save(organizationLink);

    }

    public int deleteByChildAndParent(Organization child, Organization parent) {

        return organizationLinksRepository.deleteByChildAndParent(child, parent);

    }

}
