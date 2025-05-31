package org.example.hw6_adapter_and_bridge.ioc.generating.utils

import org.example.hw6_adapter_and_bridge.ioc.generating.resolvers.DefaultGetterResolver
import org.example.hw6_adapter_and_bridge.ioc.generating.resolvers.DefaultSetterResolver
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.KCallableUtils.filterSystemMethods
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.KCallableUtils.isGetter
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.KCallableUtils.isSetter
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.NameFormatter.getAdapterName
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.NameFormatter.getFactoryName
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.NameFormatter.getPluginName
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

fun getStandardIoCAdapter(kInterface: KClass<*>, generatedMethods: String, generatedImports: String): String {
    val adapterName = kInterface.getAdapterName()

    return """
      import ${kInterface.qualifiedName}  
      import org.example.hw5_ioc.ioc.IoC
      import org.example.hw3_abstractions.UObject
      $generatedImports
        
      class $adapterName(private val uObject: UObject) : ${kInterface.simpleName} {
        $generatedMethods
      }
    """.trimIndent()
}

fun getStandardIoCGetter(classPath: String?, method: KCallable<*>): Pair<Set<String>, String> {
    val paramName = method.name.substringAfter("get")
    val params = method.parameters
    val args = StringBuilder("")
    val argsNames = StringBuilder("")
    val imports = mutableSetOf<String>()

    val returnType = if (!method.returnType.toString().contains("<")) {
        method.returnType.toString().substringAfterLast(".")
    } else {
        method.returnType.toString().substringAfter(".")
    }

    if (!method.returnType.toString().contains("<")) {
        imports.add("import ${method.returnType}\n")
    }

    for (param in params) {
        if (param.name != null) {
            args.append("${param.name}: ${param.type.toString().substringAfterLast(".")}, ")
            argsNames.append("${param.name}, ")
            if (!param.type.toString().contains("<")) {
                imports.add("import ${param.type}\n")
            }
        }
    }


    val methodCode = """
      override fun ${method.name}($args): $returnType {
        return IoC.resolve("$classPath:$paramName.get", listOf(uObject, $argsNames)) as $returnType
      }
    """.trimIndent()

    return imports to methodCode
}

fun getStandardIoCSetter(classPath: String?, method: KCallable<*>): Pair<Set<String>, String> {
    val paramName = method.name.substringAfter("set")
    val params = method.parameters
    val args = StringBuilder("")
    val imports = mutableSetOf<String>()
    val argsNames = StringBuilder("")

    for (param in params) {
        if (param.name != null) {
            args.append("${param.name}: ${param.type.toString().substringAfterLast(".")}, ")
            argsNames.append("${param.name}, ")
            if (!param.type.toString().contains("<")) {
                imports.add("import ${param.type}\n")
            }
        }
    }

    val methodCode = """
      override fun ${method.name}($args) {
        IoC.resolve("$classPath:$paramName.set", listOf(uObject, $argsNames))
      }
    """.trimIndent()

    return imports to methodCode
}

fun getStandardIoCFun(classPath: String?, method: KCallable<*>): Pair<Set<String>, String> {
    val paramName = method.name
    val params = method.parameters
    val args = StringBuilder("")
    val imports = mutableSetOf<String>()
    val argsNames = StringBuilder("")

    for (param in params) {
        if (param.name != null) {
            args.append("${param.name}: ${param.type.toString().substringAfterLast(".")}, ")
            argsNames.append("${param.name}, ")
            if (!param.type.toString().contains("<")) {
                imports.add("import ${param.type}\n")
            }
        }
    }

    val methodCode = """
      override fun ${paramName}($args) {
        IoC.resolve("$classPath:$paramName", listOf(uObject, $argsNames))
      }
    """.trimIndent()

    return imports to methodCode
}

fun getStandardAdapterFactory(kInterface: KClass<*>): String {
    val adapterName = kInterface.getAdapterName()
    val factoryName = kInterface.getFactoryName()

    return """
      import ${kInterface.qualifiedName}
      import org.example.hw5_ioc.ioc.IoC
      import org.example.hw3_abstractions.UObject
      import org.example.hw6_adapter_and_bridge.ioc.generating.contract.IAdapterFactory
        
      class $factoryName : IAdapterFactory<${kInterface.simpleName}> {
        override fun create(uObject: UObject): ${kInterface.simpleName} {
            return $adapterName(uObject)
        }
      }
    """.trimIndent()
}

fun getStandardAdapterPlugin(kInterface: KClass<*>): String {
    val interfacePath = kInterface.qualifiedName.toString()
    val factoryName = kInterface.getFactoryName()
    val pluginName = kInterface.getPluginName()

    val registerCreateAdapterCode = getStandardIcCRegister(
        key = interfacePath,
        strategy = """
            factory.create(params?.get(0) as UObject)
        """.trimIndent()
    )

    val methods = kInterface.members.filterSystemMethods()

    val registerMethodsBlock = StringBuilder()
    val imports = mutableSetOf<String>()

    for (method in methods) {
        val annotation = method.annotations.firstOrNull { it is IoCResolver } as IoCResolver?
        val resolverType = annotation?.resolver

        val (key, strategy) = when {
            method.isGetter() -> {
                val paramName = method.name.substringAfter("get")
                val resolver = resolverType ?: DefaultGetterResolver::class
                imports.add("import ${resolver.qualifiedName}\n")

                val strat = """
                    val resolver = ${resolver.simpleName}()
                    resolver.resolve(params?.get(0) as UObject, listOf("$paramName"))
                """.trimIndent()
                "$interfacePath:$paramName.get" to strat
            }

            method.isSetter() -> {
                val paramName = method.name.substringAfter("set")
                val resolver = resolverType ?: DefaultSetterResolver::class
                imports.add("import ${resolver.qualifiedName}\n")

                val strat = """
                    val resolver = ${resolver.simpleName}()
                    resolver.resolve(params?.get(0) as UObject, listOf("$paramName", params?.get(1)))
                """.trimIndent()
                "$interfacePath:$paramName.set" to strat
            }

            else -> {
                val paramName = method.name
                val resolver = resolverType ?: DefaultGetterResolver::class
                imports.add("import ${resolver.qualifiedName}\n")

                val strat = """
                    val resolver = ${resolver.simpleName}()
                    resolver.resolve(params?.get(0) as UObject, params)
                """.trimIndent()
                "$interfacePath:$paramName" to strat
            }
        }

        val generatedCode = getStandardIcCRegister(
            key = key,
            strategy = strategy
        )
        registerMethodsBlock.append("$generatedCode\n\n")
    }


    return """
      import $interfacePath
      import org.example.hw2_exceptionhandler.contract.ICommand
      import org.example.hw5_ioc.ioc.IoC
      import org.example.hw5_ioc.utils.ILambda
      import org.example.hw3_abstractions.UObject
      import org.example.hw6_adapter_and_bridge.ioc.generating.contract.IAdapterFactory
      ${imports.joinToString("\n\n")}
        
      class $pluginName : ICommand {
        override fun invoke() {

            val factory = $factoryName()
            $registerCreateAdapterCode
        
            $registerMethodsBlock
        }
      }
    """.trimIndent()
}

private fun getStandardIcCRegister(key: String, strategy: String): String {
    return """
         IoC.resolve(
            "IoC.Register",
            listOf(
                "$key",
                ILambda { key, params ->
                    $strategy
                }
            )
        )
    """.trimIndent()
}
