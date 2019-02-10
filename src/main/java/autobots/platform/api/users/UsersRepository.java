package autobots.platform.api.users;

import autobots.platform.api.organizations.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends PagingAndSortingRepository<User, Long> {

    Page<User> getByOrganization_id(Long organizationId, Pageable pageable);

    List<User> getByOrganization(Organization organization);

    User getByUsername(String username);

    User findByUsername(String username);

    @Transactional
    @Query(value = "SELECT * FROM users u WHERE (u.firstname LIKE :term1 OR u.lastname LIKE :term2 OR u.email LIKE :term3) AND u.status = :status ORDER BY u.id DESC", nativeQuery = true)
    Page<User> _search(@Param("term1") String term1, @Param("term2") String term2, @Param("term3") String term3, @Param("status") int status, Pageable pageable);

    @Query(value = "SELECT COUNT(u.id) FROM users u WHERE u.status = :status", nativeQuery = true)
    Integer _stats(@Param("status") int status);

    Optional<User> getByEmail(String email);

    Optional<User> getByEmailAndPassword(String email, String password);

    Optional<User> getByConfirmEmailToken(String confirmEmailToken);

    Optional<User> getByPasswordResetToken(String passwordResetToken);

    Optional<User> getByOrganizationAndId(Organization organization, Long id);

    @Transactional
    @Modifying
    int deleteByOrganizationAndId(Organization organization, Long id);

}
