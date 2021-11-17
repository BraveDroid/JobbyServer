package com.bravedroid.jobby.auth.domain.exceptions

class UserNotFoundException : RuntimeException("User doesn't exit.")
class BadUserPasswordException : RuntimeException("Incorrect Password.")
class UserEmailNotAvailableException (email:String): RuntimeException("$email already exist, try to login!")
