package controller

import models.User
import persistence.Serializer
import utils.Utilities.isValidListIndex

class UserAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
    private var users = ArrayList<User>()

    fun addUser(user: User): Boolean {
        return users.add(user)
    }

    fun removeUser(indexToDelete: Int): User? {
        return if (isValidListIndex(indexToDelete, users)) {
            users.removeAt(indexToDelete)
        } else null
    }

    fun updateUser(indexToUpdate: Int, updatedUser: User?): Boolean {
        val foundUser = findUser(indexToUpdate)

        if ((foundUser != null) && (updatedUser != null)) {
            foundUser.username = updatedUser.username
            foundUser.password = updatedUser.password
            foundUser.email = updatedUser.email
            return true
        }

        return false
    }

    fun listAllUsers(): String =
        if (users.isEmpty()) "No users registered" else formatListString(users)

    fun findUser(index: Int): User? {
        return if (isValidListIndex(index, users)) {
            users[index]
        } else null
    }

    fun searchByUsername(query: String): List<User> {
        return users.filter { it.username.contains(query, ignoreCase = true) }
    }

    fun numberOfUsers(): Int {
        return users.size
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, users);
    }


    @Throws(Exception::class)
    fun load() {
        users = serializer.read() as ArrayList<User>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(users)
    }

    private fun formatListString(usersToFormat: List<User>): String =
        usersToFormat.joinToString(separator = "\n") { user ->
            users.indexOf(user).toString() + ": " + user.toString()
        }
}
