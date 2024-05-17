package dev.chijiokeibekwe.librarymanagementsystem.repository;

import dev.chijiokeibekwe.librarymanagementsystem.entity.BorrowingRecord;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BorrowingRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    Optional<BorrowingRecord> findByBookIdAndPatronIdAndStatus(Long bookId, Long patronId, BorrowingRecordStatus status);
}