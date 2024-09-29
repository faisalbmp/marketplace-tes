package org.book.repository;

import org.book.model.BookDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDetailsRepository extends JpaRepository<BookDetailsModel, Long> {

    List<BookDetailsModel> findByBookNameContainingIgnoreCaseOrCreatorContainingIgnoreCaseOrReleaseDateContainingIgnoreCase(String bookName, String creator, String releaseDate);

    List<BookDetailsModel> findByBookNameContainingIgnoreCase(String bookName);

    List<BookDetailsModel> findByReleaseDateContainingIgnoreCase(String releaseDate);

    List<BookDetailsModel> findByCreatorContainingIgnoreCase(String creator);
}
