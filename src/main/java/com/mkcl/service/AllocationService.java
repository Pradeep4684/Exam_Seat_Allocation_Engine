package com.mkcl.service;

import com.mkcl.model.*;
import com.mkcl.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class AllocationService {

    private final CandidateRepository candidateRepo;
    private final AppliedPostRepository postRepo;
    private final CenterRepository centerRepo;
    private final SlotRepository slotRepo;
    private final CenterSlotRepository csRepo;
    private final AllocationRepository allocRepo;

    public AllocationService(CandidateRepository candidateRepo, AppliedPostRepository postRepo,
                             CenterRepository centerRepo, SlotRepository slotRepo,
                             CenterSlotRepository csRepo, AllocationRepository allocRepo) {
        this.candidateRepo = candidateRepo;
        this.postRepo = postRepo;
        this.centerRepo = centerRepo;
        this.slotRepo = slotRepo;
        this.csRepo = csRepo;
        this.allocRepo = allocRepo;
    }

    @Transactional
    public void allocateAll() {
        allocRepo.deleteAll();

        List<Candidate> candidates = candidateRepo.findAll();
        List<Slot> slots = slotRepo.findAll();
        List<Center> centers = centerRepo.findAll();

        for (Candidate c : candidates) {
            List<AppliedPost> posts = postRepo.findByRegistrationNumber(c.getRegistrationNumber());
            if (posts.size() == 1)
                allocateSingle(c, posts.get(0), centers, slots);
            else
                allocateMulti(c, posts, centers, slots);
        }
    }

    private void allocateSingle(Candidate c, AppliedPost p, List<Center> centers, List<Slot> slots) {
        for (Slot s : slots) {
            if ("F".equalsIgnoreCase(c.getGender()) && s.getSlotTime().contains("16:00")) continue;
            for (Center center : centers) {
                if (c.isPwd() && !center.isPwdFriendly()) continue;
                var csOpt = csRepo.findByCenterIdAndSlotIdForUpdate(center.getCenterId(), s.getId());
                if (csOpt.isPresent()) {
                    CenterSlot cs = csOpt.get();
                    if (cs.getUsed() < cs.getCapacity()) {
                        cs.setUsed(cs.getUsed() + 1);
                        csRepo.save(cs);
                        allocRepo.save(new Allocation(null, c.getRegistrationNumber(), p.getPostName(), cs, "ALLOCATED"));
                        return;
                    }
                }
            }
        }
        allocRepo.save(new Allocation(null, c.getRegistrationNumber(), p.getPostName(), null, "PENDING"));
    }

    private void allocateMulti(Candidate c, List<AppliedPost> posts, List<Center> centers, List<Slot> slots) {
        int needed = posts.size();

        for (Center center : centers) {
            if (c.isPwd() && !center.isPwdFriendly()) continue;

            List<Slot> allowed = slots.stream()
                    .filter(s -> !("F".equalsIgnoreCase(c.getGender()) && s.getSlotTime().contains("16:00")))
                    .toList();

            if (allowed.size() < needed) continue;

            List<CenterSlot> picked = new ArrayList<>();
            for (Slot s : allowed) {
                var csOpt = csRepo.findByCenterIdAndSlotIdForUpdate(center.getCenterId(), s.getId());
                if (csOpt.isPresent()) {
                    CenterSlot cs = csOpt.get();
                    if (cs.getUsed() < cs.getCapacity()) {
                        picked.add(cs);
                        if (picked.size() == needed) break;
                    }
                }
            }

            if (picked.size() == needed) {
                for (CenterSlot cs : picked) {
                    cs.setUsed(cs.getUsed() + 1);
                    csRepo.save(cs);
                }
                for (int i = 0; i < posts.size(); i++) {
                    allocRepo.save(new Allocation(null, c.getRegistrationNumber(), posts.get(i).getPostName(), picked.get(i), "ALLOCATED"));
                }
                return;
            }
        }

        for (AppliedPost p : posts)
            allocRepo.save(new Allocation(null, c.getRegistrationNumber(), p.getPostName(), null, "PENDING"));
    }

    public List<Allocation> getAllocations(Long reg) {
        return allocRepo.findByRegistrationNumber(reg);
    }
}
