package hw2_exceptionhandler

internal val expectedOutputForLogException = """
incoming message: test 1
incoming message: test 2
throw exception IllegalArgumentException
incoming message: test 3
during proceeding of ThrowExceptionCommand occurred exception java.lang.IllegalArgumentException: test exception
""".trimIndent()

internal val expectedOutputForRepeatCommandOnce = """
incoming message: test 1
incoming message: test 2
throw exception NullPointerException
incoming message: test 3
repeat command ThrowExceptionCommand
throw exception NullPointerException
""".trimIndent()

internal val expectedOutputForRepeatCommandOnceAndAfterLog = """
incoming message: test 1
incoming message: test 2
throw exception NumberFormatException
incoming message: test 3
repeat command ThrowExceptionCommand
throw exception NumberFormatException
during proceeding of RepeatOnceCommand occurred exception java.lang.NumberFormatException: test exception
""".trimIndent()

internal val expectedOutputForRepeatCommandTwiceAndAfterLog = """
incoming message: test 1
incoming message: test 2
throw exception ArithmeticException
incoming message: test 3
repeat command ThrowExceptionCommand
throw exception ArithmeticException
repeat command RepeatTwiceCommand
repeat command ThrowExceptionCommand
throw exception ArithmeticException
during proceeding of RepeatOnceCommand occurred exception java.lang.ArithmeticException: test exception
""".trimIndent()
