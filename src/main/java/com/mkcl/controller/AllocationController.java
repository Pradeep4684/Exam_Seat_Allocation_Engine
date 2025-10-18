package com.mkcl.controller;

import com.mkcl.model.Allocation;
import com.mkcl.service.AllocationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AllocationController {
    private final AllocationService service;

    public AllocationController(AllocationService service) {
        this.service = service;
    }

    @PostMapping("/allocate")
    public String allocateAll() {
        service.allocateAll();
        return "Allocation Completed";
    }

    @GetMapping("/allocation/{reg}")
    public List<AllocationDto> get(@PathVariable Long reg) {
        return service.getAllocations(reg).stream().map(AllocationDto::from).toList();
    }
}
