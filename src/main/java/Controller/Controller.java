package Controller;

import Models.AuthorRequestModel;
import Models.BookGenre;
import Models.BookRequestModel;
import Service.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    private final Service service = new Service();
    private final String[] genreList = new String[]{"Horror","HISTORICAL","FANTASY","CLASSICS"};
    public void start(){
        while(true) {
            String response = suggest();
            if (response.equals("1")) {
                AuthorRequestModel authorRequestModel = createAuthor();
                try {
                    service.createAuthor(authorRequestModel);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (response.equals("2")) {
                try {
                    BookRequestModel bookRequestModel = createBook();
                    service.createBook(bookRequestModel);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            } else if (response.equals("3")) {
                printAllAuthors();
            } else if (response.equals("4")) {
                printAllBooks();
            } else if (response.equals("5")) {
                String id = deleteBookById();
                try {
                    service.deleteBookById(id);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            } else if (response.equals("6")) {
                String id = deleteAuthorById();
                try {
                    service.deleteAuthorById(id);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            } else if (response.equals("7")) {
                String search = searchBook();
                ResultSet resultSet = service.searchBook(search);
                printSearchedBooks(resultSet);
            } else if (response.equals("8")) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Press 1 to filter by Genre");
                System.out.println("Press 2 to filter by AuthorName");
                String filterBy = scanner.nextLine();
                if(filterBy.equals("1")){
                    try {
                        ResultSet resultSet = filterBooksByGenre();
                        printResultSet(resultSet);

                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                } else if (filterBy.equals("2")) {
                    try {
                        ResultSet resultSet = filterBooksByAuthor();
                        printResultSet(resultSet,true);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }

            }
            else {
                System.out.println("Press valid key");
            }
        }
    }
    private void printResultSet(ResultSet resultSet,boolean flag){
        try{
            while(resultSet.next()){

                System.out.println(resultSet.getInt("id")+
                        ": "+resultSet.getString("title")+" - "+
                        resultSet.getString("fullName"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void printResultSet(ResultSet resultSet){
       try{
           while(resultSet.next()){

               System.out.println(resultSet.getInt("id")+
                      ": "+resultSet.getString("title")+" - "+
                       resultSet.getString("genre"));
           }
       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
    }
    private ResultSet filterBooksByGenre() throws Exception{
        Scanner scanner = new Scanner(System.in);
        ResultSet resultSet = null;
        System.out.println("1: HORROR");
        System.out.println("2: HISTORICAL");
        System.out.println("3: FANTASY");
        System.out.println("4: CLASSICS");
        String genre = scanner.nextLine();
        if(!(genre.equals("1") || genre.equals("2")
                || genre.equals("3") || genre.equals("4"))){
            throw new Exception("There is no genre with such id");

        }
        resultSet = service.filterBYGenre(genreList[Integer.parseInt(genre)-1]);
        return resultSet;
    }
    private ResultSet filterBooksByAuthor() throws Exception{
        ResultSet resultSet = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the author id,with whom you want to filter");
        printAllAuthors();
        String authorId = scanner.nextLine();
        resultSet = service.filterByAuthor(authorId);
        return resultSet;
    }
    private void printSearchedBooks(ResultSet resultSet){
        try{
            while(resultSet.next()){
                System.out.println(resultSet.getInt("id")+": "+resultSet.getString("title"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private String searchBook(){
        System.out.println("Enter the text  wich you want to search");
        Scanner scanner = new Scanner(System.in);
        return  scanner.nextLine();
    }
    private String deleteBookById(){
        Scanner scanner = new Scanner(System.in);
        printAllBooks();
        String response = scanner.nextLine();
        return response;
    }
    private String deleteAuthorById(){
        Scanner scanner = new Scanner(System.in);
        printAllAuthors();
        String response = scanner.nextLine();
        return response;
    }
    private void printAllBooks(){
        ResultSet allBooks = service.readAllBooks();
        System.out.println("Select which book has written this author,if there is more seperate it with coma" +
                "else input enter");
        while(true){
            try {
                if (!allBooks.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {

                System.out.println(allBooks.getInt("id")+": "+allBooks.getString("title")+" "+
                        allBooks.getString("genre"));
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    private void printAllAuthors(){
        ResultSet resultSet = service.readAllAuthors();
        while(true){
            try {
                if(!resultSet.next()){
                    break;
                }
                System.out.println(resultSet.getString("id")
                        +": "+resultSet.getString("fullName"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    private BookRequestModel createBook() throws Exception{
        Scanner scanner = new Scanner(System.in);
        BookRequestModel bookRequestModel = new BookRequestModel();
        System.out.println("Enter the book title");
        String title = scanner.nextLine();
        System.out.println("Input the number which author has written this book,if there are mote separate them by coma");
        printAllAuthors();
        String authorId = scanner.nextLine();
        bookRequestModel.setAuthorsId(authorId);
        bookRequestModel.setTitle(title);
        System.out.println("Press 1 : Classics");
        System.out.println("Press 2 : Historical");
        System.out.println("Press 3 : Horror");
        System.out.println("Press 4 : Fantasy");
        String genre = scanner.nextLine();
        if(genre.equals("1")){
            bookRequestModel.setBookGenre(BookGenre.CLASSICS);
        }
        else if(genre.equals("2")){
            bookRequestModel.setBookGenre(BookGenre.HISTORICAL);
        }
        else if(genre.equals("3")){
            bookRequestModel.setBookGenre(BookGenre.HORROR);
        }
        else if(genre.equals("4")){
            bookRequestModel.setBookGenre(BookGenre.FANTASY);
        }
        else{
            throw new Exception("Invalid book genre");
        }
        return bookRequestModel;
    }
    private AuthorRequestModel createAuthor(){
        Scanner scanner = new Scanner(System.in);
        AuthorRequestModel authorRequestModel = new AuthorRequestModel();
        System.out.println("Input author full name");
        String authorFullName = scanner.nextLine();
        printAllBooks();
        String bookId = scanner.nextLine();
        authorRequestModel.setBookId(bookId);
        authorRequestModel.setFullName(authorFullName);
        return authorRequestModel;
    }
    private String suggest(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press 1 to create autor");
        System.out.println("Press 2 to create book");
        System.out.println("Press 3 to read autors");
        System.out.println("Press 4 to read books");
        System.out.println("Press 5 to delete book");
        System.out.println("Press 6 to delete autor");
        System.out.println("Press 7 to search book");
        System.out.println("Press 8 to filter books");
        String response = scanner.nextLine();
        return response;
    }
}
