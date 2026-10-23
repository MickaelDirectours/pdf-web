package fr.ngtravel.pdf.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Cdn {
	@Id
	private String code;
	private String publicpath;
}
