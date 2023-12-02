import controller.BookAPI
import models.Book
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}
//private val bookAPI = BookAPI(XMLSerializer(File("books.xml")))
private val bookAPI = BookAPI(JSONSerializer(File("books.json")))

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a book                |
         > |   2) List books                |
         > |   3) Update a book             |
         > |   4) Delete a book             |
         > |   5) Rate a book               |
         > |   6) Rank Books By Rating      |
         > ----------------------------------
         > |   20) Save notes               |
         > |   21) Load notes               |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addBook()
            2  -> listBooks()
            3  -> updateBook()
            4  -> removeBook()
            5  -> rateBook()
            6  -> rankBooksByRating()
            //20  -> save()
            //21  -> load()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun addBook(){
    val bookTitle = readNextLine("Enter the books title: ")
    val bookAuthor = readNextLine("Enter book Author ")
    val bookGenre = readNextLine("Enter the books genre: ")
    val bookRating = readNextInt("Enter the books rating (Between 1-5): ")
    val isAdded = bookAPI.add(Book(bookTitle, bookAuthor, bookGenre, bookRating))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listBooks(){
    if (bookAPI.numberOfBooks() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL books          |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllBooks();
            else -> println("Invalid option entered: $option");
        }
    } else {
        println("Option Invalid - No books stored");
    }
}

fun listAllBooks() {
    println(bookAPI.listAllBooks())
}

fun updateBook() {
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (bookAPI.isValidIndex(indexToUpdate)) {
            val bookTitle = readNextLine("Enter a title for the book: ")
            val bookAuthor = readNextLine("Enter the books Author: ")
            val bookGenre = readNextLine("Enter the books Genre: ")
            val bookRating = readNextInt("Enter the books Rating: ")
            if (bookAPI.updateBook(indexToUpdate, Book(bookTitle, bookAuthor, bookGenre, bookRating))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no books for this index number")
        }
    }
}

fun removeBook(){
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        val indexToDelete = readNextInt("Enter the index of the book to remove: ")
        val bookToDelete = bookAPI.removeBook(indexToDelete)
        if (bookToDelete != null) {
            println("Delete Successful! Deleted book: ${bookToDelete.bookTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun rateBook() {
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        val indexToRate = readNextInt("Enter the index of the book to rate: ")
        if (bookAPI.isValidIndex(indexToRate)) {
            val rating = readNextInt("Enter your rating (1-5) for the book: ")
            if (rating in 1..5) {
                val isRated = bookAPI.rateBook(indexToRate, rating)
                if (isRated) {
                    println("Rating added successfully!")
                } else {
                    println("Failed to add rating. Please try again.")
                }
            } else {
                println("Invalid rating. Please enter a value between 1 and 5.")
            }
        } else {
            println("Invalid book index.")
        }
    }
}

fun rankBooksByRating() {
    val rankedBooks = bookAPI.rankBooksByRating()

    if (rankedBooks.isNotEmpty()) {
        println("Books ranked by rating:")
        rankedBooks.forEachIndexed { index, book ->
            println("${index + 1}. ${book.bookTitle} - Rating: ${book.bookRating}")
        }
    } else {
        println("No books to rank.")
    }
}

fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exitProcess(0)
}