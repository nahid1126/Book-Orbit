package com.nahid.book_orbit.core.utils.exception

class ServerException(override val message:String):Exception(message)
class InvalidUserException(override val message:String = "User Id Or Password Incorrect"):Exception(message)
class AuthException(override val message:String = "Authentication Failed"):Exception(message)
class NetworkException(override val message: String) : Exception(message)
class CustomException(override val message: String) : Exception(message)

