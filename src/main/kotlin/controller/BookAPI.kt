package controller

import persistence.Serializer
import models.Book
class BookAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var books = ArrayList<Book>()

    fun add(book: Book): Boolean {
        return books.add(book)
    }

    fun removeBook(indexToDelete: Int): Book?{
        return if (isValidListIndex(indexToDelete, books)){
            books.removeAt(indexToDelete)
        } else null
    }

    fun listAllBooks(): String {
        return if (books.isEmpty()) {
            "No Books Added"
        } else {
            var listOfBooks = " "
            for (i in books.indices)
                listOfBooks += "${i}: ${books[i]} \n"
        }
            listOfBooks
    }

    fun numberOfBooks(): Int {
        return books.size
    }


    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }
}