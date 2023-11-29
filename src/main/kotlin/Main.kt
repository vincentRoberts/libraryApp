import utils.ScannerInput
import java.lang.System.exit

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt("""
        
        
    """.trimIndent(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {

            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}