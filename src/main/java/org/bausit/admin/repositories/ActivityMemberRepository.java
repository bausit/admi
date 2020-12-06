package org.bausit.admin.repositories;

import org.bausit.admin.models.ActivityMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityMemberRepository extends CrudRepository<ActivityMember, Long> {
}
