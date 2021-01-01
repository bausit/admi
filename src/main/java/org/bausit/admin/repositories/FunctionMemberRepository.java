package org.bausit.admin.repositories;

import org.bausit.admin.models.FunctionMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionMemberRepository extends CrudRepository<FunctionMember, Long> {
}
