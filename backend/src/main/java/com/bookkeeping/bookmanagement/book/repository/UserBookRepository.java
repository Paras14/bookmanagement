package com.bookkeeping.bookmanagement.book.repository;

import com.bookkeeping.bookmanagement.book.model.UserBook;
import com.bookkeeping.bookmanagement.book.model.UserBookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, UserBookId> {

    List<UserBook> findByUserId(Long userId);
    Optional<UserBook> findByUserIdAndBookIsbn(Long userId, String bookIsbn);
    boolean existsByBookIsbn(String isbn);
    void deleteByBookIsbn(String isbn);


//    @Query("SELECT ub FROM UserBook ub WHERE ub.user.id = :userId AND ub.book.isbn = :bookIsbn")
//    Optional<UserBook> findByUserIdAndBookIsbn(@Param("userId") Long userId,
//                                               @Param("bookIsbn") String bookIsbn);

//    @Query("SELECT COUNT(ub) > 0 FROM UserBook ub WHERE ub.book.isbn = :isbn")
//    boolean existsByBookIsbn(String isbn);

//    @Query("DELETE FROM UserBook ub WHERE ub.book.isbn = :isbn")
//    void deleteByBookIsbn(String isbn);


}
