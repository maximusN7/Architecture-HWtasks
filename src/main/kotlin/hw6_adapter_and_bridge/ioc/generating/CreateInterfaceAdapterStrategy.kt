package org.example.hw6_adapter_and_bridge.ioc.generating

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw3_abstractions.UObject
import org.example.hw5_ioc.ioc.IoC
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.*
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.KCallableUtils.filterSystemMethods
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.KCallableUtils.isGetter
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.KCallableUtils.isSetter
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.NameFormatter.getAdapterName
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.NameFormatter.getFactoryName
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.NameFormatter.getPluginName
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.File
import java.net.URLClassLoader
import java.nio.file.Files
import kotlin.reflect.KClass

class CreateInterfaceAdapterStrategy(
    private val kInterface: KClass<out Any>,
    private val uObject: UObject,
) {

    fun resolve(): Any? {
        val pathToClass = kInterface.qualifiedName.orEmpty()
        return try {
            IoC.resolve(pathToClass, uObject)
        } catch (e: AdapterFactoryNotExistingException) {
            getInterfaceAdapterPluginCommand().invoke()
            IoC.resolve(pathToClass, uObject)
        }
    }

    private fun getInterfaceAdapterPluginCommand(): ICommand {
        val adapterName = kInterface.getAdapterName()
        val factoryName = kInterface.getFactoryName()
        val pluginName = kInterface.getPluginName()

        val plugin = compileClassesReturnPluginCommand(
            listOf(adapterName, factoryName, pluginName),
            listOf(generateAdapterCode(), getStandardAdapterFactory(kInterface), getStandardAdapterPlugin(kInterface))
        )

        return plugin
    }

    private fun generateAdapterCode(): String {
        val methods = kInterface.members.filterSystemMethods()

        val combinedMethods = StringBuilder()
        val combinedImports = mutableSetOf<String>()

        for (method in methods) {
            val (generatedImports, generatedCode) = when {
                method.isGetter() -> {
                    getStandardIoCGetter(kInterface.qualifiedName.toString(), method)
                }

                method.isSetter() -> {
                    getStandardIoCSetter(kInterface.qualifiedName.toString(), method)
                }

                else -> {
                    getStandardIoCFun(kInterface.qualifiedName.toString(), method)
                }
            }

            combinedMethods.append("\n\n$generatedCode")
            combinedImports.addAll(generatedImports)
        }

        val stringImports = combinedImports.joinToString("\n\n")

        return getStandardIoCAdapter(kInterface, combinedMethods.toString(), stringImports)
    }

    private fun compileClassesReturnPluginCommand(interfaceNames: List<String>, sourceCodes: List<String>): ICommand {
        // 1. Сохраняем во временный .kt файл
        val tempDir = Files.createTempDirectory("autogenerated").toFile()

        val ktFiles = interfaceNames.map { interfaceName ->
            File(tempDir, "${interfaceName}.kt")
        }
        ktFiles.forEachIndexed { i, file ->
            file.writeText(sourceCodes[i])
        }

        // 2. Указываем путь для вывода классов
        val outputDir = File(tempDir, "out")
        outputDir.mkdirs()

        val stdlibPath = Class.forName("kotlin.Unit")
            .protectionDomain
            .codeSource
            .location
            .path

        val classesDir = File("build/classes/kotlin/main")

        // 3. Компилируем
        val args = K2JVMCompilerArguments().apply {
            freeArgs = ktFiles.map { it.absolutePath }
            destination = outputDir.absolutePath
            classpath = "$stdlibPath${File.pathSeparator}${classesDir.absolutePath}"
        }

        val compiler = K2JVMCompiler()
        val messageCollector = PrintingMessageCollector(System.out, MessageRenderer.PLAIN_RELATIVE_PATHS, true)
        val exitCode = compiler.execImpl(messageCollector, Services.EMPTY, args)

        if (exitCode == ExitCode.OK) {

            // 4. Загружаем скомпилированный класс
            val classLoader = URLClassLoader(arrayOf(outputDir.toURI().toURL()))
            val pluginClass = classLoader.loadClass(interfaceNames[2])
            val instance = pluginClass.getDeclaredConstructor().newInstance()

            return instance as ICommand
        } else {
            throw IllegalArgumentException()
        }
    }
}
