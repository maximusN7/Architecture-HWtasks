package ioc.utils

class ResolveDependencyException(key: String = "") :
    Exception("Unknown IoC dependency with key: $key. Make sure that $key has been registered before try to resolve the dependency")