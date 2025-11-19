package ru.jd.mapper;

import lombok.NoArgsConstructor;
import ru.jd.model.dto.device.DeviceDto;
import ru.jd.model.dto.device.RegistrationDeviceRequest;
import ru.jd.model.entity.Device;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DeviceMapper {
    public static Device toEntity(RegistrationDeviceRequest request) {
        Device device = new Device();

        device.setDeviceCode(request.getDeviceCode());
        device.setName(request.getName());
        device.setLocation(request.getLocation());

        return device;
    }

    public static List<DeviceDto> toDto(List<Device> devices) {
        return devices.stream().map(d -> new DeviceDto(d.getId(),d.getName(),d.getDeviceCode(),
                d.getLocation(),d.getIsActive(), d.getCreatedAt())).toList();
    }
}
