package ru.jd.model.dto.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class AddProductTemplateRequest {
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Long templateId;
    private Map<String, String> details;
    private String line;
    private MultipartFile image;
}
