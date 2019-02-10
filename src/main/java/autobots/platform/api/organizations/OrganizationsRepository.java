package autobots.platform.api.organizations;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationsRepository extends PagingAndSortingRepository<Organization, Long> {

    @Query(value = "" + "SELECT o.id, o.name, o.description, o.status, o.stamp_created, COUNT(c.id) AS total_cameras                                                 " + "                                                               " + "FROM organization_links l                                      " + "                                                               " + "INNER JOIN organizations o ON o.id = l.child_id                " + "                                                               " + "LEFT OUTER JOIN cameras c on o.id = c.organization_id          " + "                                                               " + "WHERE l.parent_id = :parent_id OR l.child_id = :parent_id                                " + "                                                               " + "GROUP BY o.id", nativeQuery = true)
    List<Organization> _getByParentId(@Param("parent_id") Long parent_id);

    @Query(value = "" + "SELECT o.id, o.name, o.description, o.status, o.stamp_created                                                  " + "                                                                                                               " + "FROM organization_links l                                                                                      " + "                                                                                                               " + "INNER JOIN organizations o ON o.id = l.child_id                                                                " + "                                                                                                               " + "WHERE l.child_id = :child_id AND l.parent_id = :parent_id                                                      ", nativeQuery = true)
    Optional<Organization> _getByChildIdAndParentId(@Param("child_id") Long child_id, @Param("parent_id") Long parent_id);

}
