package dev.chijiokeibekwe.librarymanagementsystem.repository;

import dev.chijiokeibekwe.librarymanagementsystem.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {
}