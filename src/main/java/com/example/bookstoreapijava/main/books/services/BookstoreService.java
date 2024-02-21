package com.example.bookstoreapijava.main.books.services;

import com.example.bookstoreapijava.main.books.data.dto.BookResponseDTO;
import com.example.bookstoreapijava.main.books.data.dto.BookUpdateDTORequest;
import com.example.bookstoreapijava.main.books.data.vo.BookCreatedVO;
import com.example.bookstoreapijava.main.books.entities.Book;
import com.example.bookstoreapijava.main.books.repositories.BookRepository;
import com.example.bookstoreapijava.main.category.data.dto.CategoryResponseDTO;
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
    Book savedBook = bookRepository.save(book);
    Category category = categoryRepository.getReferenceById(savedBook.getCategory().getCategoryId());
    CategoryResponseDTO categoryDTO = new CategoryResponseDTO(category.getCategoryId(), category.getCategoryName());
    URI uri = new URI("http://localhost:8080/bookstore/" + savedBook.getBookId().toString());
    BookResponseDTO responseDTO =
        new BookResponseDTO(savedBook.getTitle(), savedBook.getAuthor(), savedBook.getIsbn(), categoryDTO);

    BookCreatedVO bookCreatedVO = new BookCreatedVO(responseDTO, uri);

    return bookCreatedVO;
  }

  public Book updateBook(Long id, BookUpdateDTORequest bookUpdateDTO) {
    Book savedBook = bookRepository.getReferenceById(id);

    Category updatedCategory =
        bookUpdateDTO.getCategoryId() == null
            ? savedBook.getCategory()
            : categoryRepository.getReferenceById(bookUpdateDTO.getCategoryId().getAsLong());

    String author = bookUpdateDTO.getAuthor() == null
        ? savedBook.getAuthor()
        : bookUpdateDTO.getAuthor().get();

    String title = bookUpdateDTO.getTitle() == null
        ? savedBook.getTitle()
        : bookUpdateDTO.getTitle().get();

    String isbn = bookUpdateDTO.getIsbn() == null
        ? savedBook.getIsbn()
        : bookUpdateDTO.getIsbn().get();

    bookRepository.updateBookById(title, author, isbn, updatedCategory, id);

    return bookRepository.getReferenceById(id);
  }

}
