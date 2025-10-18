package com.mkcl.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
	@Id
	private Long registrationNumber;

	private String candidateName;
	private String gender; // "M" or "F"
	private boolean isPwd;
}
