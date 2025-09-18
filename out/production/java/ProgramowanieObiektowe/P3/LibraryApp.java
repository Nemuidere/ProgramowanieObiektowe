package ProgramowanieObiektowe.P3;

import java.io.*;
import java.util.*;
import java.util.function.*;

// Final - stała i klasa niemodyfikowalna
final class DocumentStatus {
    public static final String AVAILABLE = "DOSTĘPNY";
    public static final String BORROWED = "WYPOŻYCZONY";
}

// Dziedziczenie, enkapsulacja, Serializable, final method
abstract class Document implements Serializable {
    private String title;
    private String author;
    protected int year;
    public final double calculateLateFee(int daysLate) { // final method
        return daysLate * 0.50;
    }
    public Document(String title, String author, int year) { // konstruktor z parametrami
        this.title = title;
        this.author = author;
        this.year = year;
    }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public abstract void display(); // polimorfizm (abstrakcja)
    // Klonowanie (shallow)
    public Document shallowClone() throws CloneNotSupportedException {
        return (Document) super.clone();
    }
}

// Interfejs, polimorfizm (runtime)
interface Borrowable {
    void borrow(User user);
    void returnItem();
    default boolean isAvailable() { return true; }
}

// Implementacja buildera
class Book extends Document implements Borrowable {
    private String isbn;
    private String category;
    // Builder pattern
    public static class Builder {
        String title, author, isbn, category;
        int year;
        public Builder title(String t) { this.title = t; return this; }
        public Builder author(String a) { this.author = a; return this; }
        public Builder isbn(String i) { this.isbn = i; return this; }
        public Builder category(String c) { this.category = c; return this; }
        public Builder year(int y) { this.year = y; return this; }
        public Book build() { return new Book(title, author, year, isbn, category); }
    }
    // Konstruktor wywoływany przez buildera (this + super)
    private Book(String title, String author, int year, String isbn, String category) {
        super(title, author, year); this.isbn = isbn; this.category = category;
    }
    @Override public void display() { System.out.println("Book: " + getTitle()); }
    @Override public void borrow(User user) { System.out.println(user.getName()+" borrows "+getTitle()); }
    @Override public void returnItem() { System.out.println(getTitle()+" returned."); }
    public String getCategory() { return category; }
}

// Klasa abstrakcyjna, dziedziczenie, agregacja
abstract class User {
    protected String name;
    protected List<Document> borrowed = new LinkedList<>();
    public User(String name) { this.name = name; }
    public String getName() { return name; }
    public abstract void borrow(Document doc);
}
class Student extends User {
    public Student(String name) { super(name); }
    @Override public void borrow(Document doc) { borrowed.add(doc); }
}

// Singleton - tylko jedna instancja LibraryManager
class LibraryManager {
    private static volatile LibraryManager instance;
    private final List<Document> docs = new ArrayList<>();
    private LibraryManager() {}
    public static LibraryManager getInstance() {
        if(instance==null) synchronized(LibraryManager.class) {
            if(instance==null) instance=new LibraryManager();
        }
        return instance;
    }
    public void addDocument(Document doc) { docs.add(doc); }
    public List<Document> getDocs() { return docs; }
    public List<Document> search(Predicate<Document> filter) { // lambda, Predicate
        List<Document> result = new ArrayList<>();
        for(Document d:docs) if(filter.test(d)) result.add(d);
        return result;
    }
}

// Decorator - dynamiczne rozszerzenie
class PremiumDocDecorator extends Document {
    private Document base;
    public PremiumDocDecorator(Document base) { super(base.getTitle(), base.getAuthor(), base.getYear()); this.base=base; }
    public void display() { base.display(); System.out.println("[Premium feature!]"); }
}

// Wyjątki - checked i unchecked
class BookNotFoundException extends Exception {
    public BookNotFoundException(String msg) { super(msg); }
}
class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException(String msg) { super(msg); }
}

// Kolekcje, Multimap (Map<K, List<V>>)
class MultiMap<K,V> {
    private Map<K,List<V>> map = new HashMap<>();
    public void put(K key, V val) { map.computeIfAbsent(key,k->new ArrayList<>()).add(val);}
    public List<V> get(K key) { return map.getOrDefault(key,new ArrayList<>());}
}

// Testy jednostkowe w main (TDD approach, parametryzacja)
public class LibraryApp {
    public static void main(String[] args) throws Exception {
        // Budowanie książki (Builder)
        Book b1 = new Book.Builder().title("Java Guide").author("Oracle").category("Programming").year(2020).isbn("123").build();

        // Dziedziczenie/User/Student & Borrow
        User u1 = new Student("Janek"); u1.borrow(b1);

        // Singleton
        LibraryManager manager = LibraryManager.getInstance();
        manager.addDocument(b1);
        manager.addDocument(new Book.Builder().title("Clean Code").author("Martin").category("Programming").year(2008).isbn("456").build());

        // Strumienie, lambda, kolektory
        List<Document> programmingBooks = manager.search(d -> (d instanceof Book) && "Programming".equals(((Book)d).getCategory()));
        System.out.println("Programming Books: " + programmingBooks.size());

        // Multimap
        MultiMap<String, Book> booksByCat = new MultiMap<>();
        booksByCat.put("Programming",b1);

        // Optional
        Optional<Document> found = programmingBooks.stream().findFirst();
        found.ifPresent(d -> System.out.println("Found: "+d.getTitle()));

        // Try-with-resources, serializacja
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("books.dat"))) {
            oos.writeObject(programmingBooks);
        }

        // Wyjątki, test parametryczny (symulacja)
        try {
            Document doc = programmingBooks.stream().filter(b->"NotExist".equals(b.getTitle())).findFirst()
                    .orElseThrow(() -> new BookNotFoundException("Not found!"));
        } catch(BookNotFoundException e){System.out.println("Exception: "+e.getMessage());}

        // Decorator
        PremiumDocDecorator prembk = new PremiumDocDecorator(b1); prembk.display();

        // Wątki (Thread, Runnable)
        Runnable indexer = () -> { for(Document d:manager.getDocs())
            System.out.println("Indexing: "+d.getTitle());};
        Thread t = new Thread(indexer); t.start(); t.join();

        // Test assertions (mini TDD) - nie korzysta z JUnit, tylko System.out
        assert b1.getTitle().equals("Java Guide") : "Book title mismatch";
        assert u1 instanceof Student : "User type mismatch";
        assert programmingBooks.size() == 2 : "Programming books count mismatch";
        assert booksByCat.get("Programming").size() == 1 : "Multimap count mismatch";

        // Wyrażenie funkcyjne własne
        BookValidator validator = b -> b.getTitle()!=null && b.getAuthor()!=null;
        System.out.println("Is valid? "+validator.isValid(b1));
    }
}

// Interfejs funkcyjny
@FunctionalInterface
interface BookValidator { boolean isValid(Book b); }

//why?
