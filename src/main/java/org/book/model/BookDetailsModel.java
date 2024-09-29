package org.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "book_details")
public class BookDetailsModel extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "creator")
    private String creator;
}
