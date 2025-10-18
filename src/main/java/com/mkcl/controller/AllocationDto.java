package com.mkcl.controller;

import com.mkcl.model.Allocation;

public record AllocationDto(String postName, String status, String centerName, String slotTime, String examDate) {
    public static AllocationDto from(Allocation a) {
        if (a.getCenterSlot() == null)
            return new AllocationDto(a.getPostName(), a.getStatus(), null, null, null);

        return new AllocationDto(
                a.getPostName(),
                a.getStatus(),
                a.getCenterSlot().getCenter().getCenterName(),
                a.getCenterSlot().getSlot().getSlotTime(),
                a.getCenterSlot().getSlot().getExamDate().toString()
        );
    }
}
