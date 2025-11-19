package ru.jd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jd.model.entity.Device;
import ru.jd.repository.DeviceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public Device saveDevice(Device device) {
        return deviceRepository.save(device);
    }
    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    public List<Device> getAllDevicesForOrganization(Long organizationId) {
        return deviceRepository.findDevicesByOrganizationId(organizationId);
    }
}
