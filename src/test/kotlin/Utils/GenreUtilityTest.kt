package utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.GenreUtility.genres
import utils.GenreUtility.isValidGenre

internal class GenreUtilityTest {
    @Test
    fun genresReturnsFullGenresSet(){
        Assertions.assertEquals(7, genres.size)
        Assertions.assertTrue(genres.contains("Novel"))
        Assertions.assertTrue(genres.contains("Poetry"))
        Assertions.assertTrue(genres.contains("Play"))
        Assertions.assertTrue(genres.contains("Short Story"))
        Assertions.assertTrue(genres.contains("History"))
        Assertions.assertTrue(genres.contains("Philosophy"))
        Assertions.assertTrue(genres.contains("Science"))
        Assertions.assertFalse(genres.contains(""))
    }

    @Test
    fun isValidGenreTrueWhenGenreExists(){
        Assertions.assertTrue(isValidGenre("Novel"))
        Assertions.assertTrue(isValidGenre("novel"))
        Assertions.assertTrue(isValidGenre("Poetry"))
        Assertions.assertTrue(isValidGenre("POETRY"))
        Assertions.assertTrue(isValidGenre("pLaY"))
        Assertions.assertTrue(isValidGenre("Play"))
        Assertions.assertTrue(isValidGenre("Short Story"))
        Assertions.assertTrue(isValidGenre("short story"))
        Assertions.assertTrue(isValidGenre("History"))
        Assertions.assertTrue(isValidGenre("history"))
        Assertions.assertTrue(isValidGenre("Philosophy"))
        Assertions.assertTrue(isValidGenre("philosophy"))
        Assertions.assertTrue(isValidGenre("Science"))
        Assertions.assertTrue(isValidGenre("science"))
    }

    @Test
    fun isValidGenreFalseWhenGenreDoesNotExist(){
        Assertions.assertFalse(isValidGenre("Noovel"))
        Assertions.assertFalse(isValidGenre("potry"))
        Assertions.assertFalse(isValidGenre("Lay"))
        Assertions.assertFalse(isValidGenre("shortstory"))
        Assertions.assertFalse(isValidGenre("Histry"))
        Assertions.assertFalse(isValidGenre("philos"))
        Assertions.assertFalse(isValidGenre("sceence"))
        Assertions.assertFalse(isValidGenre(""))
    }
}