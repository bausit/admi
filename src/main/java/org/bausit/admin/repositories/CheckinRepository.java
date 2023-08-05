package org.bausit.admin.repositories;

import org.bausit.admin.models.Checkin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckinRepository extends CrudRepository<Checkin, Long> {
}
