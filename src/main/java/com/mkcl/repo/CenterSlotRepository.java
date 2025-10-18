package com.mkcl.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mkcl.model.CenterSlot;

import jakarta.persistence.LockModeType;

@Repository
public interface CenterSlotRepository extends JpaRepository<CenterSlot, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select cs from CenterSlot cs where cs.center.centerId = :centerId and cs.slot.id = :slotId")
    Optional<CenterSlot> findByCenterIdAndSlotIdForUpdate(@Param("centerId") Long centerId, @Param("slotId") Long slotId);
}
