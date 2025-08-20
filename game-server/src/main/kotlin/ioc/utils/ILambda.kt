package ioc.utils

fun interface ILambda {

    operator fun invoke(key: String, params: List<Any?>?): Any?
}
