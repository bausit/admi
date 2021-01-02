package org.bausit.admin.repositories;

import org.bausit.admin.models.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionRepository extends CrudRepository<Team, Long> {
}
