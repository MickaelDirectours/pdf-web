package fr.ngtravel.pdf.adapters.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.ngtravel.pdf.model.Cdn;

public interface CdnRepository extends JpaRepository<Cdn, String> {

}
