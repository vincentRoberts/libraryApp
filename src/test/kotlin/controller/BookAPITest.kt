package controller

import models.Book
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals

class BookAPITest {

    private var learnKotlin: Book? = null
    private var summerHoliday: Book? = null
    private var codeApp: Book? = null
    private var testApp: Book? = null
    private var swim: Book? = null
    private var populatedBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))
    private var emptyNotes: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))

    @BeforeEach
    fun setup(){
        learnKotlin = Book("Hamlet", "Shakespeare", "Play")
        summerHoliday = Book("Summer Holiday to France", "Rimbeau", "Holiday")
        codeApp = Book("Code App", "Abc", "Work")
        testApp = Book("Test App", "Def", "Work")
        swim = Book("Swim - Pool", "Ghj", "Hobby")

        //adding 5 Note to the notes api
        populatedBooks!!.add(learnKotlin!!)
        populatedBooks!!.add(summerHoliday!!)
        populatedBooks!!.add(codeApp!!)
        populatedBooks!!.add(testApp!!)
        populatedBooks!!.add(swim!!)
    }

    @AfterEach
    fun tearDown(){
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedBooks = null
        emptyNotes = null
    }
}