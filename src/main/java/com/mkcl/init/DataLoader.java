package com.mkcl.init;

import com.mkcl.model.*;
import com.mkcl.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CandidateRepository candidateRepo;
    private final AppliedPostRepository postRepo;
    private final CenterRepository centerRepo;
    private final SlotRepository slotRepo;
    private final CenterSlotRepository csRepo;

    public DataLoader(CandidateRepository candidateRepo, AppliedPostRepository postRepo,
                      CenterRepository centerRepo, SlotRepository slotRepo, CenterSlotRepository csRepo) {
        this.candidateRepo = candidateRepo;
        this.postRepo = postRepo;
        this.centerRepo = centerRepo;
        this.slotRepo = slotRepo;
        this.csRepo = csRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        candidateRepo.saveAll(List.of(
                new Candidate(101L, "Abhay", "M", false),
                new Candidate(102L, "Sonam", "F", false),
                new Candidate(103L, "Ritika", "F", false),
                new Candidate(104L, "Akshay", "M", true)
        ));

        postRepo.saveAll(List.of(
                new AppliedPost(null, 101L, "Tech Assistant"),
                new AppliedPost(null, 101L, "Clerk"),
                new AppliedPost(null, 102L, "Clerk"),
                new AppliedPost(null, 103L, "Data Entry Operator"),
                new AppliedPost(null, 104L, "Tech Assistant")
        ));

        Center c1 = centerRepo.save(new Center(null, "MIT College Pune", 30, true));
        Center c2 = centerRepo.save(new Center(null, "COEP Pune", 25, false));
        Center c3 = centerRepo.save(new Center(null, "Government Engg College Pune", 20, true));

        Slot s1 = slotRepo.save(new Slot(null, LocalDate.of(2025, 9, 15), "09:00-10:30"));
        Slot s2 = slotRepo.save(new Slot(null, LocalDate.of(2025, 9, 15), "12:30-14:00"));
        Slot s3 = slotRepo.save(new Slot(null, LocalDate.of(2025, 9, 15), "16:00-17:30"));

        for (Center c : List.of(c1, c2, c3)) {
            csRepo.save(new CenterSlot(null, c, s1, c.getCapacity(), 0));
            csRepo.save(new CenterSlot(null, c, s2, c.getCapacity(), 0));
            csRepo.save(new CenterSlot(null, c, s3, c.getCapacity(), 0));
        }
    }
}
