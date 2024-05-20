package dev.chijiokeibekwe.librarymanagementsystem.repository;

import dev.chijiokeibekwe.librarymanagementsystem.entity.Book;
import dev.chijiokeibekwe.librarymanagementsystem.entity.BorrowingRecord;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Patron;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BorrowingRecordStatus;
import dev.chijiokeibekwe.librarymanagementsystem.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BorrowingRecordRepositoryTest {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final TestUtil testUtil = new TestUtil();

    @Test
    void testFindByBookIdAndPatronIdAndStatus() {
        Book book = testUtil.getBook();
        book.setId(null);

        Patron patron = testUtil.getPatron();
        patron.setId(null);

        Book savedBook = testEntityManager.persistAndFlush(book);
        Patron savedPatron = testEntityManager.persistAndFlush(patron);

        BorrowingRecord recordOne = testUtil.getBorrowingRecord();
        recordOne.setId(null);
        recordOne.setStatus(BorrowingRecordStatus.CLOSED);
        recordOne.setBook(savedBook);
        recordOne.setPatron(savedPatron);

        BorrowingRecord recordTwo = testUtil.getBorrowingRecord();
        recordTwo.setId(null);
        recordTwo.setBook(savedBook);
        recordTwo.setPatron(savedPatron);

        testEntityManager.persistAndFlush(recordOne);
        BorrowingRecord savedRecordTwo = testEntityManager.persistAndFlush(recordTwo);

        Optional<BorrowingRecord> result = borrowingRecordRepository.findByBookIdAndPatronIdAndStatus(savedBook.getId(),
                savedPatron.getId(), BorrowingRecordStatus.OPEN);

        assertThat(result.get().getId()).isEqualTo(savedRecordTwo.getId());
    }
}
