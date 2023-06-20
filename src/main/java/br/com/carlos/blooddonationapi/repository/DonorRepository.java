package br.com.carlos.blooddonationapi.repository;

import br.com.carlos.blooddonationapi.entities.Donor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface DonorRepository extends JpaRepository<Donor, Integer> {
    Page<Donor> findAll(Pageable pageable);

    Optional<Donor> findByNome(String name);
}
