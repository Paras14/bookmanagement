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
    Optional<UserBook> findByUserIdAndBookId(Long userId, Long bookId);
    boolean existsByBookId(Long bookId);
    void deleteByBookId(Long bookId);


//    @Query("SELECT ub FROM UserBook ub WHERE ub.user.id = :userId AND ub.book.id = :bookId")
//    Optional<UserBook> findByUserIdAndBookId(@Param("userId") Long userId,
//                                             @Param("bookId") Long bookId);

//    @Query("SELECT COUNT(ub) > 0 FROM UserBook ub WHERE ub.book.id = :bookId")
//    boolean existsByBookId(Long bookId);

//    @Query("DELETE FROM UserBook ub WHERE ub.book.id = :bookId")
//    void deleteByBookId(Long bookId);


}
