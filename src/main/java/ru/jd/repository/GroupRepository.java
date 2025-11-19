package ru.jd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jd.model.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
