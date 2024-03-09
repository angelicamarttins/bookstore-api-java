package com.example.bookstoreapijava.main.book.services;

import com.example.bookstoreapijava.main.book.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.book.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.book.entities.Book;
import com.example.bookstoreapijava.main.book.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.entities.Category;
import com.example.bookstoreapijava.main.category.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class BookstoreService {
  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Book> findAllBooks() {
    return bookRepository.findAll();
  }

  public Book getBook(Long id) {
    return bookRepository.getReferenceById(id);
  }

  public BookCreatedVO insertBook(Book book) throws URISyntaxException {
    Category category = categoryRepository.getReferenceById(book.getCategory().getCategoryId());
    book.setCategory(category);

    Book savedBook = bookRepository.save(book);


    URI uri = new URI("http://localhost:8080/bookstore/" + savedBook.getBookId().toString());

//    BookResponseDTO responseDTO =
//        new BookResponseDTO(savedBook.getTitle(), savedBook.getAuthor(), savedBook.getIsbn(), categoryDTO);

    return new BookCreatedVO(book, uri);
  }

  public Book updateBook(Long id, BookUpdateDTORequest bookUpdateDTO) {
    Book savedBook = bookRepository.getReferenceById(id);

    Category updatedCategory =
        bookUpdateDTO.getCategoryId() == null
            ? savedBook.getCategory()
            : categoryRepository.getReferenceById(bookUpdateDTO.getCategoryId().getAsLong());

    savedBook.setTitle(bookUpdateDTO.getTitle().orElse(savedBook.getTitle()));
    savedBook.setAuthor(bookUpdateDTO.getAuthor().orElse(savedBook.getAuthor()));
    savedBook.setIsbn(bookUpdateDTO.getIsbn().orElse(savedBook.getIsbn()));
    savedBook.setCategory(updatedCategory);

    return bookRepository.save(savedBook);
  }

}
