package controllers

import controller.BookAPI
import models.Book
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import persistence.XMLSerializer
import java.io.File

class BookAPITest {

    private var book1: Book? = null
    private var book2: Book? = null
    private var book3: Book? = null
    private var book4: Book? = null
    private var book5: Book? = null
    private var book6: Book? = null
    private var book7: Book? = null
    private var book8: Book? = null
    private var populatedBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))
    private var emptyBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))

    @BeforeEach
    fun setup() {
        book1 = Book("Hamlet", "Shakespeare", "Play", 5, "Very Good")
        book2 = Book("War and Peace", "Leo Tolstoy", "Novel", 5, "Very Good")
        book3 = Book("Ulysses", "James Joyce", "Novel", 4, "Good")
        book4 = Book("The Prelude", "William Wordsworth", "Poetry", 3, "OK")
        book5 = Book("The Peloponnesian Wars", "Thucydides", "History", 3, "Good")
        book6 = Book("The Nichomachean Ethics", "Aristotle", "Philosophy", 2, "Interesting")
        book7 = Book("A Sportsman's Notebook", "Ivan Turgenev", "Short Story", 1, "Good")
        book8 = Book("The Origin Of Species", "Darwin", "Science", 4, "Very Good")

        populatedBooks!!.add(book1!!)
        populatedBooks!!.add(book2!!)
        populatedBooks!!.add(book3!!)
        populatedBooks!!.add(book4!!)
        populatedBooks!!.add(book5!!)
        populatedBooks!!.add(book6!!)
        populatedBooks!!.add(book7!!)
        populatedBooks!!.add(book8!!)
    }

    @AfterEach
    fun tearDown() {
        book1 = null
        book2 = null
        book3 = null
        book4 = null
        book5 = null
        book6 = null
        book7 = null
        book8 = null
        populatedBooks = null
        emptyBooks = null
    }

    @Nested
    inner class AddBooks {
        @Test
        fun `adding a Book to a populated list adds to ArrayList`() {
            val newBook = Book("Jane Eyre", "Charlotte Bronte", "Novel", 4, "Very Good")
            assertEquals(8, populatedBooks!!.numberOfBooks())
            assertTrue(populatedBooks!!.add(newBook))
            assertEquals(9, populatedBooks!!.numberOfBooks())
            assertEquals(newBook, populatedBooks!!.findBook(populatedBooks!!.numberOfBooks() - 1))
        }

        @Test
        fun `adding a Book to an empty list adds to ArrayList`() {
            val newBook = Book("Jane Eyre", "Charlotte Bronte", "Novel", 4, "Very Good")
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.add(newBook))
            assertEquals(1, emptyBooks!!.numberOfBooks())
            assertEquals(newBook, emptyBooks!!.findBook(emptyBooks!!.numberOfBooks() - 1))
        }
    }

    @Nested
    inner class ListBooks {

        @Test
        fun `listAllBooks returns No Books Stored message when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listAllBooks().lowercase().contains("no books"))
        }

        @Test
        fun `listAllBooks returns books when ArrayList has books stored`() {
            assertEquals(8, populatedBooks!!.numberOfBooks())
            val booksString = populatedBooks!!.listAllBooks().lowercase()
            assertTrue(booksString.contains("hamlet"))
            assertTrue(booksString.contains("war and peace"))
            assertTrue(booksString.contains("ulysses"))
            assertTrue(booksString.contains("the prelude"))
            assertTrue(booksString.contains("the peloponnesian wars"))
            assertTrue(booksString.contains("the nichomachean ethics"))
            assertTrue(booksString.contains("a sportsman's notebook"))
            assertTrue(booksString.contains("the origin of species"))
        }

        @Test
        fun `listBooksByRating returns No Books when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listBooksByRating(1).lowercase().contains("no books"))
        }


        @Test
        fun `listBooksByRating returns all books that match that rating when books of that rating exist`() {
            assertEquals(8, populatedBooks!!.numberOfBooks())

            val rating1String = populatedBooks!!.listBooksByRating(1).lowercase()
            assertTrue(rating1String.contains("a sportsman's notebook"))

            val rating4String = populatedBooks!!.listBooksByRating(4).lowercase()
            assertTrue(rating4String.contains("ulysses"))
            assertTrue(rating4String.contains("the origin of species"))
        }
    }

    @Nested
    inner class DeleteBooks {

        @Test
        fun `deleting a Book that does not exist returns null`() {
            assertNull(emptyBooks!!.removeBook(0))
            assertNull(populatedBooks!!.removeBook(-1))
            assertNull(populatedBooks!!.removeBook(8))
        }

        @Test
        fun `deleting a Book that exists deletes and returns deleted object`() {
            assertEquals(8, populatedBooks!!.numberOfBooks())

            // Remove and verify the last book
            assertEquals(book8, populatedBooks!!.removeBook(7))
            assertEquals(7, populatedBooks!!.numberOfBooks())

            // Remove and verify the first book
            assertEquals(book1, populatedBooks!!.removeBook(0))
            assertEquals(6, populatedBooks!!.numberOfBooks())

            // Remove and verify a middle book
            assertEquals(book4, populatedBooks!!.removeBook(2))
            assertEquals(5, populatedBooks!!.numberOfBooks())
        }
    }

    @Nested
    inner class UpdateBooks {

        @Test
        fun `updating a Book that does not exist returns false`() {
            assertFalse(populatedBooks!!.updateBook(8, Book("Updating Book", "Author", "Category", 2, "Good")))
            assertFalse(populatedBooks!!.updateBook(-1, Book("Updating Book", "Author", "Category", 2, "Good")))
            assertFalse(emptyBooks!!.updateBook(0, Book("Updating Book", "Author", "Category", 2, "Good")))
        }


    }

    @Nested
    inner class FindBooks {

        @Test
        fun `finding a Book by index returns the correct Book`() {
            assertEquals(book1, populatedBooks!!.findBook(0))
            assertEquals(book5, populatedBooks!!.findBook(4))
            assertEquals(book8, populatedBooks!!.findBook(7))
        }

        @Test
        fun `finding a Book by index returns null when index is invalid`() {
            assertNull(emptyBooks!!.findBook(0))
            assertNull(populatedBooks!!.findBook(-1))
            assertNull(populatedBooks!!.findBook(8))
        }


    }
}
