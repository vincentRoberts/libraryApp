package controller

import models.Book
import persistence.Serializer
import utils.Utilities.isValidListIndex

class BookAPI(serializerType: Serializer){

    private var serializer: Serializer = serializerType

    private var books = ArrayList<Book>()

    fun add(book: Book): Boolean {
        return books.add(book)
    }

    fun removeBook(indexToDelete: Int): Book? {
        return if (isValidListIndex(indexToDelete, books)) {
            books.removeAt(indexToDelete)
        } else null
    }

    fun updateBook(indexToUpdate: Int, book: Book?): Boolean {
        val foundBook = findBook(indexToUpdate)

        if ((foundBook != null) && (book != null)) {
            foundBook.bookTitle = book.bookTitle
            foundBook.bookAuthor = book.bookAuthor
            foundBook.bookGenre = book.bookGenre
            return true
        }

        return false
    }


    fun listAllBooks(): String =
        if (books.isEmpty())  "No books stored"
        else formatListString(books)

    fun rateBook(indexToUpdate: Int, rating: Int): Boolean {
        val foundBook = findBook(indexToUpdate)

        if (foundBook != null && isValidListIndex(indexToUpdate, books)) {
            foundBook.bookRating = foundBook.bookRating
            return true
        }

        return false
    }



    fun numberOfBooks(): Int {
        return books.size
    }

    fun findBook(index: Int): Book? {
        return if (isValidListIndex(index, books)) {
            books[index]
        } else null
    }

    fun searchByTitleOrAuthor(query: String): List<Book> {
        return books.filter { it.bookTitle.contains(query, ignoreCase = true) || it.bookAuthor.contains(query, ignoreCase = true) }
    }

    fun listBooksByRating(rating: Int): String =
        if (books.isEmpty()) "No books stored"
        else {
            val listOfBooks = formatListString(books.filter{ book -> book.bookRating == rating})
            if (listOfBooks.equals("")) "No books with rating: $rating"
            else "${numberOfBooksByRating(rating)} books with rating '$rating': $listOfBooks"
        }

    fun listBooksByAuthor(author: String): String =
        if (books.isEmpty()) "No books stored"
        else {
            val listOfBooks = formatListString(books.filter{ book -> book.bookAuthor == author})
            if (listOfBooks.equals("")) "No books for : $author"
            else "${numberOfBooksByAuthor(author)} books authored by '$author': $listOfBooks"
        }

    fun listBooksByGenre(genre: String): String =
        if (books.isEmpty()) "No books stored"
        else {
            val listOfBooks = formatListString(books.filter{ book -> book.bookGenre == genre})
            if (listOfBooks.equals("")) "No books for : $genre"
            else "${numberOfBooksByGenre(genre)} books with genre '$genre': $listOfBooks"
        }

    fun numberOfBooksByRating(rating: Int): Int = books.count { p: Book -> p.bookRating == rating }

    fun numberOfBooksByAuthor(author: String): Int = books.count { p: Book -> p.bookAuthor == author }

    fun numberOfBooksByGenre(genre: String): Int = books.count { p: Book -> p.bookGenre == genre }

    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, books);
    }

    @Throws(Exception::class)
    fun load() {
        books = serializer.read() as ArrayList<Book>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(books)
    }

    private fun formatListString(booksToFormat : List<Book>) : String =
        booksToFormat
            .joinToString (separator = "\n") { book ->
                books.indexOf(book).toString() + ": " + book.toString() }

}

