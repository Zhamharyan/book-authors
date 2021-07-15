package Data;

import Models.Author;
import Models.Book;

import java.sql.*;
import java.util.Arrays;
import java.util.stream.IntStream;

public class DAO {

    private Connection connection;
    public DAO(){
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/booksandautorsdb","root","123456");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ResultSet readAllBooks(){
        String sql = "Select * from book";
        ResultSet result = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
    public ResultSet readAllAuthors(){
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from autor");
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet readById(int id){
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from book where id = ?");
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet readByIdAuthor(int id){
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from autor where id = ?");
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    public void createAuthor(Author author){
        String authorFullName = author.getFullName();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into autor(fullName) values (?)");
            preparedStatement.setString(1,authorFullName);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int lastAuthorId = 0;
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("select * from autor order by id desc limit 1");
            ResultSet resultSet = preparedStatement1.executeQuery();
            resultSet.next();
            lastAuthorId = resultSet.getInt("id");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
               if (author.getBookId()!=null && author.getBookId().length != 0) {
                   int[] booksId = author.getBookId();
                   for (int i = 0; i < booksId.length; i++) {

                       try {
                           PreparedStatement preparedStatement1 = connection.prepareStatement("insert into " +
                                   "booksandautors(book_id,autor_id) values(?,?)");
                           preparedStatement1.setInt(1,booksId[i]);
                           preparedStatement1.setInt(2,lastAuthorId);
                           preparedStatement1.execute();
                       } catch (SQLException throwables) {
                           throwables.printStackTrace();
                       }

                   }
               }
    }
    public void createBook(Book book){
        int currentBookId = 0;
        try {
            PreparedStatement setBook = connection.prepareStatement("insert into book(title,genre) values(?,?)");
            setBook.setString(1,book.getTitle());
            setBook.setString(2,book.getBookGenre().toString());
            setBook.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            ResultSet temp = connection.createStatement().executeQuery("select * from book order by id desc limit 1");
            temp.next();
            currentBookId = temp.getInt("id");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int[] authorsId = book.getAuthorsId();
        if(authorsId.length != 0){
            for(int i = 0;i<authorsId.length;i++){
                String sql = "insert into booksandautors(book_id,autor_id) values(?,?);";
                try {
                    PreparedStatement setDepenedencies = connection.prepareStatement(sql);
                    setDepenedencies.setInt(2,authorsId[i]);
                    setDepenedencies.setInt(1,currentBookId);
                    setDepenedencies.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
    public void deleteBookById(int id){
        String sql = "delete from book where id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void deleteAuthorById(int id){
        String sql = "delete from autor where id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public ResultSet searchBook(String text){
        String sql = "select * from book where title like '%"+text+"%'";
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet filterByGenre(String genre){
        String sql = "select * from book where genre = ?";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,genre);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet filterByAuthorName(int authorId){
        String sql = "select * from book inner join booksandautors on book.id = booksandautors.book_id inner join autor on booksandautors.autor_id = autor.id and autor_id = ?;";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,authorId);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

}
