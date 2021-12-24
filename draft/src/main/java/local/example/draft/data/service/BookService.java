package local.example.draft.data.service;

import java.util.Optional;

import local.example.draft.data.entity.Book;
import local.example.draft.data.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private BookRepository repository;

    public BookService(@Autowired BookRepository repository) {
        this.repository = repository;
    }

    public Optional<Book> get(Long id) {
        return repository.findById(id);
    }

    public Book update(Book entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Book> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
