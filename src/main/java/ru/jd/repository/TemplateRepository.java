package ru.jd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jd.model.entity.Template;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
}
