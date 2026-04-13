package com.pawsstay.auth_service.util;

import com.pawsstay.auth_service.dto.OwnerCreateRequest;
import com.pawsstay.auth_service.dto.OwnerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "owner-service", url = "${app.services.owner-url}")
public interface OwnerClient {
    @GetMapping("/api/v1/owners/email/{email}")
    OwnerResponse getOwnerByEmail(@PathVariable String email);

    @PostMapping("/api/v1/owners")
    OwnerResponse createOwner(@RequestBody OwnerCreateRequest ownerDto);
}
