import controller.BookAPI
import models.Book
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import utils.ValidateInput.readValidGenre
import utils.ValidateInput.readValidRating
import utils.GenreUtility


import java.lang.System.exit
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}
private val bookAPI = BookAPI(JSONSerializer(File("books.json")))

fun main(args: Array<String>) {
    runMenu()
}

// MENU
fun mainMenu() : Int {
    return readNextInt(""" 
        ╔═════════════════════════════════════╗
        ║           LIBRARY APP               ║
        ╠═════════════════════════════════════╣
        ║               MENU                  ║
        ╠═════════════════════════════════════╣
        ║ 1) Add Book                         ║
        ║ 2) List Books                       ║
        ║ 3) Update Book                      ║
        ║ 4) Delete Book                      ║
        ║ 5) Archive a note                   ║
        ║ 6) Search Book                      ║
        ╠═════════════════════════════════════╣
        ║ 20) Save Books                      ║
        ║ 21) Load Books                      ║
        ╠═════════════════════════════════════╣
        ║ 0) Exit                             ║
        ╚═════════════════════════════════════╝
        Enter number here: """.trimMargin(">"))
}
// RUN MENU
fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addBook()
            2  -> listBooks()
            3  -> updateBook()
            4  -> removeBook()
            7  -> searchBook()
            20  -> save()
            21  -> load()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}
// ADD BOOK
fun addBook(){
    val bookTitle = readNextLine("Enter the books title: ")
    val bookAuthor = readNextLine("Enter book Author: ")
    val bookGenre = readValidGenre("Enter a genre  ${GenreUtility.genres}: ")
    val bookRating = readValidRating("Enter the books rating (Between 1-5): ")
    val isAdded = bookAPI.add(Book(bookTitle, bookAuthor, bookGenre, bookRating))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}
// LIST BOOKS MENU
fun listBooks(){
    if (bookAPI.numberOfBooks() > 0) {
        val option = readNextInt(
            """
        ╔════════════════════════════════════╗
        ║           LIST BOOKS               ║
        ╠════════════════════════════════════╣
        ╠════════════════════════════════════╣
        ║ 1) View All Books                  ║
        ║ 2) List Books By Rating            ║
        ║ 3) List Books By Author            ║
        ║ 4) List Books By Genre             ║
        ╚════════════════════════════════════╝
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllBooks();
            2 -> listBooksByRating();
            3 -> listBooksByAuthor();
            4 -> listBooksByGenre();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}
// LIST BOOKS
fun listAllBooks() {
    println(bookAPI.listAllBooks())
}
// BY RATING
fun listBooksByRating() {
    val rating = readValidRating("Enter the rating of the book: ")
    val booksList = bookAPI.listBooksByRating(rating)
    println(booksList)
}
// BY AUTHOR
fun listBooksByAuthor() {
    val author = ScannerInput.readNextLine("Enter the author: ")
    val booksList = bookAPI.listBooksByAuthor(author)
    println(booksList)
}
// BY GENRE
fun listBooksByGenre() {
    val genre = readValidGenre("Enter the genre ${GenreUtility.genres}: ")
    val booksList = bookAPI.listBooksByGenre(genre)
    println(booksList)
}
// UPDATE BOOK
fun updateBook() {
    listAllBooks()
    if (bookAPI.numberOfBooks() > 0) {
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (bookAPI.isValidIndex(indexToUpdate)) {
            val bookTitle = readNextLine("Enter a title for the book: ")
            val bookAuthor = readNextLine("Enter the books Author: ")
            val bookGenre = readValidGenre("Enter the books Genre ${GenreUtility.genres}: ")
            val bookRating = readValidRating("Enter the books Rating: ")
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

// REMOVE BOOK
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


// SEARCH BOOK
fun searchBook() {
    val searchQuery = readNextLine("Enter book title or author to search: ")
    val foundBooks = bookAPI.searchByTitleOrAuthor(searchQuery)

    if (foundBooks.isNotEmpty()) {
        println("Found books matching the search:")
        foundBooks.forEachIndexed { index, book ->
            println("${index + 1}. ${book.bookTitle} by ${book.bookAuthor} - Genre: ${book.bookGenre}, Rating: ${book.bookRating}")
        }
    } else {
        println("No books found matching the search criteria.")
    }
}
// SAVE
fun save() {
    try {
        bookAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}
// LOAD
fun load() {
    try {
        bookAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}
// EXIT FUNCTION
fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exitProcess(0)
}