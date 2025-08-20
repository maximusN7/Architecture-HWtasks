package org.example.interpreters

import org.example.exceptions.AuthErrorException
import javax.inject.Inject

class ErrorCodeInterpreter  @Inject constructor() {

    fun interpret(code: Int): Exception {
        return when (code) {
            403 -> AuthErrorException()
            else -> Exception()
        }
    }
}
