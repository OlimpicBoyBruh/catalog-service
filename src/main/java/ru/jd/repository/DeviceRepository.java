package ru.jd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jd.model.entity.Device;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findDevicesByOrganizationId(Long organisationId);
}
