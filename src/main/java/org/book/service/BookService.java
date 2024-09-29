package org.book.service;

import org.book.repository.BookDetailsRepository;
import org.book.model.BookDetailsModel;
import org.book.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class BookService {

    @Autowired
    BookDetailsRepository bookDetailsDao;

    public ResponseEntity<ResponseUtil> bookAdd(BookDetailsModel request) {
        ResponseUtil response = new ResponseUtil();
        try {
            bookDetailsDao.save(request);
            response.setStatus(200);
            response.setMessage(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseUtil> bookFind(BookDetailsModel request) {
        ResponseUtil response = new ResponseUtil();
        try {
            List<BookDetailsModel> result = new ArrayList<>();
            if (request.getId() != null) {
                result.add(bookDetailsDao.findById(request.getId()).orElse(new BookDetailsModel()));
            } else if (request.getBookName() != null && !request.getBookName().equals("")) {
                result = bookDetailsDao.findByBookNameContainingIgnoreCase(request.getBookName()).stream().toList();
            } else if (request.getReleaseDate() != null && !request.getReleaseDate().equals("")) {
                result = bookDetailsDao.findByReleaseDateContainingIgnoreCase(request.getReleaseDate());
            } else if (request.getCreator() != null && !request.getCreator().equals("")) {
                result = bookDetailsDao.findByCreatorContainingIgnoreCase(request.getCreator());
            }
            response.setStatus(200);
            response.setMessage(result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseUtil> bookFindAll() {
        ResponseUtil response = new ResponseUtil();
        try {
            List<BookDetailsModel> result = bookDetailsDao.findAll().stream().toList();
            response.setStatus(200);
            response.setMessage(result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseUtil> bookUpdate(BookDetailsModel request) {
        ResponseUtil response = new ResponseUtil();
        try {
            if (request.getId() == null) {
                response.setStatus(400);
                response.setMessage("Wrong input variables!");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            bookDetailsDao.save(request);
            response.setStatus(200);
            response.setMessage(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
