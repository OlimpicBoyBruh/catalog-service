package ru.jd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jd.model.dto.device.GetAllDevicesResponse;
import ru.jd.model.dto.device.RegistrationDeviceRequest;
import ru.jd.model.dto.device.RegistrationDeviceResponse;
import ru.jd.model.dto.device.UpdateGroupDeviceRequest;
import ru.jd.model.dto.device.UpdateGroupDeviceResponse;
import ru.jd.service.ManagerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/device")
public class DeviceController {
    private final ManagerService managerService;

    @PostMapping("/register")
    public RegistrationDeviceResponse registerDevice(@RequestBody RegistrationDeviceRequest request) {
        return managerService.registerDevice(request);
    }

    @PutMapping("/update/group")
    public UpdateGroupDeviceResponse updateGroupDevice(@RequestBody UpdateGroupDeviceRequest request) {
        return managerService.updateGroupToDevice(request);
    }

    @GetMapping("/{organizationId}")
    public GetAllDevicesResponse getDevices(@PathVariable("organizationId") Long organizationId) {
        return managerService.getDevices(organizationId);
    }
}
