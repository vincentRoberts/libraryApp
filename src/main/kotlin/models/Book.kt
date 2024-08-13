package models

class Book (var bookTitle: String,
            var bookAuthor: String,
            var bookGenre: String,
            var bookRating: Int,
            var bookComments: String)
{
    override fun toString(): String {
        return "Title: $bookTitle, Author: $bookAuthor, Genre: $bookGenre, Rating: $bookRating"
    }

}