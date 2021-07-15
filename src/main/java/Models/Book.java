package Models;

public class Book {
    private int[] authorsId;
    private String title;
    private BookGenre bookGenre;

    public BookGenre getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(BookGenre bookGenre) {
        this.bookGenre = bookGenre;
    }

    public int[] getAuthorsId() {
        return authorsId;
    }

    public void setAuthorsId(int[] authorsId) {
        this.authorsId = authorsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
