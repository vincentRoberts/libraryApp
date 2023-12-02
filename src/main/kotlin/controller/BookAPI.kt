package controller

import models.Book
import persistence.Serializer

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


    fun listAllBooks(): String {
        return if (books.isEmpty()) {
            "No notes stored"
        } else {
            var listOfBooks = ""
            for (i in books.indices) {
                listOfBooks += "${i}: ${books[i]} \n"
            }
            listOfBooks
        }
    }

    fun rateBook(indexToUpdate: Int, rating: Int): Boolean {
        val foundBook = findBook(indexToUpdate)

        if (foundBook != null && isValidListIndex(indexToUpdate, books)) {
            foundBook.bookRating = foundBook.bookRating
            return true
        }

        return false
    }

    fun rankBooksByRating(): List<Book> {
        return books.sortedByDescending { it.bookRating }
    }

    fun numberOfBooks(): Int {
        return books.size
    }

    fun findBook(index: Int): Book? {
        return if (isValidListIndex(index, books)) {
            books[index]
        } else null
    }

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

}