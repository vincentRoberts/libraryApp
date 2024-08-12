package utils

import utils.ScannerInput.readNextInt
import java.util.*

object ValidateInput {

    @JvmStatic
    fun readValidGenre(prompt: String?): String {
        print(prompt)
        var input = Scanner(System.`in`).nextLine()
        do {
            if (GenreUtility.isValidGenre(input))
                return input
            else {
                print("Invalid genre $input.  Please try again: ")
                input = Scanner(System.`in`).nextLine()
            }
        } while (true)
    }

    @JvmStatic
    fun readValidRating(prompt: String?): Int {
        var input =  readNextInt(prompt)
        do {
            if (Utilities.validRange(input, 1 ,5))
                return input
            else {
                print("Invalid rating $input.")
                input = readNextInt(prompt)
            }
        } while (true)
    }

}