package ru.jd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jd.mapper.TemplateMapper;
import ru.jd.model.dto.template.CreateTemplateRequest;
import ru.jd.model.entity.Template;
import ru.jd.repository.TemplateRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;

    public Template saveTemplate(CreateTemplateRequest createTemplateRequest) {
        return templateRepository.save(TemplateMapper.toEntity(createTemplateRequest));

    }

    public List<Template> getAllTemplate() {
        return templateRepository.findAll();

    }

    public Template getReferenceById(Long templateId) {
        return templateRepository.getReferenceById(templateId);
    }

    public Template getById(Long templateId) {
        return templateRepository.findById(templateId).orElse(null);
    }
}
