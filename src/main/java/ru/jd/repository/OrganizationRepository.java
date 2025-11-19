package ru.jd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jd.model.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
