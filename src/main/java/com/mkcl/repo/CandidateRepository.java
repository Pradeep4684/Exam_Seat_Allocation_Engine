package com.mkcl.repo;

import com.mkcl.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import java.util.*;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {}