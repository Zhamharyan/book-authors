package Service;

import Data.DAO;
import Models.Author;
import Models.AuthorRequestModel;
import Models.Book;
import Models.BookRequestModel;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Service {
    private final DAO dao = new DAO();
    public ResultSet readAllBooks(){
        return dao.readAllBooks();
    }
    public ResultSet readAllAuthors(){
        return dao.readAllAuthors();
    }
    public void createBook(BookRequestModel bookRequestModel) throws Exception{
        Book book = new Book();
        String title = bookRequestModel.getTitle();
        String[] authorsId = bookRequestModel.getAuthorsId().split(",");
        if(authorsId.length == 0){
            throw new Exception("The book must have author");
        }
        else{
            int[] authorsIdInt = new int[authorsId.length];
            for(int i = 0;i<authorsIdInt.length;i++){
                int id = 0;
                try{
                    id = Integer.parseInt(authorsId[i]);
                }catch (NumberFormatException e){
                    throw new Exception("The id must be number");
                }
                ResultSet resultSet = dao.readByIdAuthor(id);
                if(!resultSet.next()){
                    throw new Exception("There are no author with such id");
                }
                authorsIdInt[i] = id;
            }
            book.setTitle(title);
            book.setAuthorsId(authorsIdInt);
            book.setBookGenre(bookRequestModel.getBookGenre());
        }
        dao.createBook(book);
    }
    public void createAuthor(AuthorRequestModel authorRequestModel) throws Exception{
        Author author = new Author();
        if(authorRequestModel.getFullName().equals("")){
            throw new Exception("The author name can not be null");
        }
        if(!authorRequestModel.getBookId().equals("")) {
            String[] booksId = authorRequestModel.getBookId().split(",");
            int[] booksIdInt = new int[booksId.length];
            for (int i = 0; i < booksId.length; i++) {
                int id = 0;
                try {
                    id = Integer.parseInt(booksId[i]);
                } catch (NumberFormatException e) {
                    throw new Exception("The book id can not be string\n");
                }
                ResultSet resultSet = dao.readById(id);
                if (!resultSet.next()) {
                    throw new Exception("There is no book with such id\n");
                }
                booksIdInt[i] = id;
            }
            author.setBookId(booksIdInt);
        }
        else{
            author.setBookId(null);
        }

        author.setFullName(authorRequestModel.getFullName());
        dao.createAuthor(author);
    }
    public void deleteBookById(String id) throws Exception{
        int book_id = 0;
        try {
            book_id = Integer.parseInt(id);
        }catch (NumberFormatException e){
            throw new Exception("The book id must be string");
        }
        ResultSet resultSet = dao.readById(book_id);
        if(!resultSet.next()){
            throw new Exception("There is not book with such id");
        }
        dao.deleteBookById(book_id);
    }
    public void deleteAuthorById(String id) throws Exception{
        int author_id = 0;
        try {
            author_id = Integer.parseInt(id);
        }catch (NumberFormatException e){
            throw new Exception("The author id must be string");
        }
        ResultSet resultSet = dao.readByIdAuthor(author_id);
        if(!resultSet.next()){
            throw new Exception("There is no author with such id");
        }
        dao.deleteAuthorById(author_id);
    }
    public ResultSet searchBook(String text){
       return dao.searchBook(text);
    }

    public ResultSet filterBYGenre(String genre){
        return dao.filterByGenre(genre);
    }

    public ResultSet filterByAuthor(String id) throws Exception{
        int authorId = 0;
        try{
            authorId = Integer.parseInt(id);
        }catch (Exception e){
            throw new Exception("The id must be number");
        }
        return dao.filterByAuthorName(authorId);
    }


}
