import controller.BookAPI
import controller.UserAPI
import models.Book
import models.User
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
private val userAPI = UserAPI(JSONSerializer(File("users.json")))

fun main() {
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
        ║ 5) Search Book                      ║
        ║ 6) View\Update Comments             ║
        ║ 7) User Menu                        ║
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
            5  -> searchBook()
            6 -> viewComments()
            7 -> userMenu()
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
    val bookComments = readNextLine("Enter any comments you may have on the book: ")
    val isAdded = bookAPI.add(Book(bookTitle, bookAuthor, bookGenre, bookRating, bookComments))

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
        ║ 0) Return to Main Menu             ║
        ╚════════════════════════════════════╝
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllBooks();
            2 -> listBooksByRating();
            3 -> listBooksByAuthor();
            4 -> listBooksByGenre();
            0 -> return
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No books stored");
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
            val bookComments = readNextLine("Enter comments you may have on the book: ")
            if (bookAPI.updateBook(indexToUpdate, Book(bookTitle, bookAuthor, bookGenre, bookRating, bookComments))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no books for this index number")
        }
    }
}

// VIEW COMMENTS MENU
fun viewComments(){
    if (bookAPI.numberOfBooks() > 0) {
        val option = readNextInt(
            """
        ╔════════════════════════════════════╗
        ║            COMMENTS                ║
        ╠════════════════════════════════════╣
        ╠════════════════════════════════════╣
        ║ 1) View Comments By Title          ║
        ║ 2) Update Comments                 ║
        ║ 0) Return to Main Menu             ║
        ╚════════════════════════════════════╝
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> viewCommentsByTitle();
            2 -> updateBookComments();
            0 -> return
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}

// VIEW COMMENTS BY TITLE
fun viewCommentsByTitle() {
    val searchQuery = readNextLine("Enter the book title to view comments: ")
    val booksList = bookAPI.searchByTitleOrAuthor(searchQuery)
    if (booksList.isNotEmpty()) {
        booksList.forEach { book ->
            println("Title: ${book.bookTitle}")
            println("Comments: ${book.bookComments.ifEmpty { "No comments" }}")
            println("----------")
        }
    } else {
        println("No books found with title containing '$searchQuery'.")
    }
}

// UPDATE COMMENTS (INDEX)
fun updateBookComments() {
    listAllBooks()
    if (bookAPI.numberOfBooks() > 0) {
        val indexToUpdate = readNextInt("Enter the index of the book to update comments: ")
        if (bookAPI.isValidIndex(indexToUpdate)) {
            val newComments = readNextLine("Enter the new comments for the book: ")
            if (bookAPI.updateBookComments(indexToUpdate, newComments)) {
                println("Comments updated successfully.")
            } else {
                println("Update failed. Book not found.")
            }
        } else {
            println("Invalid index number.")
        }
    } else {
        println("No books available to update.")
    }
}

fun userMenu() {
        val option = readNextInt(
            """
            ╔════════════════════════════════════╗
            ║            USER MENU               ║
            ╠════════════════════════════════════╣
            ║ 1) Register User                   ║
            ║ 2) List Users                      ║
            ║ 3) Update User                     ║
            ║ 4) Remove User                     ║
            ║ 5) Search User by Username         ║
            ║ 0) Return to Main Menu             ║
            ╚════════════════════════════════════╝
             > ==>> """.trimMargin(">"))

        when (option) {
            1 -> registerUser()
            2 -> listUsers()
            3 -> updateUser()
            4 -> removeUser()
            5 -> searchUserByUsername()
            0 -> return
            else -> println("Invalid option entered: $option")
        }

}


fun registerUser() {
    val username = readNextLine("Enter username: ")
    val password = readNextLine("Enter password: ")
    val email = readNextLine("Enter email: ")

    val userAdded = userAPI.addUser(User(username, password, email))
    if (userAdded) {
        println("User registered successfully.")
    } else {
        println("User registration failed.")
    }
}

fun listUsers() {
    println(userAPI.listAllUsers())
}

fun updateUser() {
    listUsers()
    if (userAPI.numberOfUsers() > 0) {
        val indexToUpdate = readNextInt("Enter the index of the user to update: ")
        if (userAPI.isValidIndex(indexToUpdate)) {
            val username = readNextLine("Enter new username: ")
            val password = readNextLine("Enter new password: ")
            val email = readNextLine("Enter new email: ")
            if (userAPI.updateUser(indexToUpdate, User(username, password, email))) {
                println("User updated successfully.")
            } else {
                println("Update failed.")
            }
        } else {
            println("Invalid index number.")
        }
    }
}

fun removeUser() {
    listUsers()
    if (userAPI.numberOfUsers() > 0) {
        val indexToDelete = readNextInt("Enter the index of the user to remove: ")
        val userToDelete = userAPI.removeUser(indexToDelete)
        if (userToDelete != null) {
            println("User removed successfully: ${userToDelete.username}")
        } else {
            println("User removal failed.")
        }
    }
}

fun searchUserByUsername() {
    val searchQuery = readNextLine("Enter username to search: ")
    val foundUsers = userAPI.searchByUsername(searchQuery)

    if (foundUsers.isNotEmpty()) {
        println("Found users matching the search:")
        foundUsers.forEachIndexed { index, user ->
            println("${index + 1}. ${user.username} - Email: ${user.email}")
        }
    } else {
        println("No users found matching the search criteria.")
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