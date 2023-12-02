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
         > |   2) List books               |
         > |   3) Update a book             |
         > |   4) Delete a book             |
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
    val isAdded = bookAPI.add(Book(bookTitle, bookAuthor, bookGenre))

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

            if (bookAPI.updateBook(indexToUpdate, Book(bookTitle, bookAuthor, bookGenre))){
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

fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exitProcess(0)
}