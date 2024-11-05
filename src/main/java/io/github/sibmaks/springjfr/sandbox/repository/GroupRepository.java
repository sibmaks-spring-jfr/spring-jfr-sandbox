package io.github.sibmaks.springjfr.sandbox.repository;

import io.github.sibmaks.springjfr.sandbox.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Group JPA repository
 *
 * @author sibmaks
 * @since 0.0.1
 */
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

}
