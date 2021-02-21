package org.bausit.admin.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.models.Permission;
import org.bausit.admin.repositories.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Optional<Permission> findById(long permissionId) {
        return permissionRepository.findById(permissionId);
    }
}
