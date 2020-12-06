package org.bausit.admin.repositories;

import org.bausit.admin.models.Function;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionRepository extends CrudRepository<Function, Long> {
}
