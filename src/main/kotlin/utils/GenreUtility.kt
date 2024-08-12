package utils

object GenreUtility {
    @JvmStatic
    val genres = setOf ("Novel", "Poetry", "Play" ,"Short Story" ,"History", "Philosophy", "Science")

    @JvmStatic
    fun isValidGenre(genreToCheck: String?): Boolean {
        for (genre in genres) {
            if (genre.equals(genreToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }


}