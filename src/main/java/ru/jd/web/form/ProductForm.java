package ru.jd.web.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ProductForm {
    private Long templateId;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String line;
    private List<DetailRow> details = new ArrayList<>();
    private MultipartFile image;

    public Map<String, String> toDetailsMap() {
        Map<String, String> mappedDetails = new LinkedHashMap<>();
        if (details == null) {
            return mappedDetails;
        }
        for (DetailRow row : details) {
            if (row == null) {
                continue;
            }
            if (StringUtils.hasText(row.getKey()) && StringUtils.hasText(row.getValue())) {
                mappedDetails.put(row.getKey().trim(), row.getValue().trim());
            }
        }
        return mappedDetails;
    }

    public void ensureDetailRows(int minRows) {
        if (details == null) {
            details = new ArrayList<>();
        }
        while (details.size() < minRows) {
            details.add(new DetailRow());
        }
    }

    @Getter
    @Setter
    public static class DetailRow {
        private String key;
        private String value;
    }
}

