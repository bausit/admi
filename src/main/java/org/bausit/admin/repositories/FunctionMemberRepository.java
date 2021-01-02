package org.bausit.admin.repositories;

import org.bausit.admin.models.TeamMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionMemberRepository extends CrudRepository<TeamMember, Long> {
}
