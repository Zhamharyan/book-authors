package Models;

public class Author {
    private String fullName;
    private int[] bookId;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int[] getBookId() {
        return bookId;
    }

    public void setBookId(int[] bookId) {
        this.bookId = bookId;
    }
}
