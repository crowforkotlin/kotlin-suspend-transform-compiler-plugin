package love.forte.plugin.suspendtrans.runners

import love.forte.plugin.suspendtrans.services.SuspendTransformerEnvironmentConfigurator
import org.jetbrains.kotlin.config.ApiVersion
import org.jetbrains.kotlin.config.LanguageVersion
import org.jetbrains.kotlin.platform.jvm.JvmPlatforms
import org.jetbrains.kotlin.test.FirParser
import org.jetbrains.kotlin.test.TargetBackend
import org.jetbrains.kotlin.test.backend.ir.JvmIrBackendFacade
import org.jetbrains.kotlin.test.builders.TestConfigurationBuilder
import org.jetbrains.kotlin.test.builders.configureFirHandlersStep
import org.jetbrains.kotlin.test.builders.configureJvmArtifactsHandlersStep
import org.jetbrains.kotlin.test.directives.FirDiagnosticsDirectives.FIR_PARSER
import org.jetbrains.kotlin.test.frontend.classic.ClassicFrontend2IrConverter
import org.jetbrains.kotlin.test.frontend.classic.ClassicFrontendFacade
import org.jetbrains.kotlin.test.frontend.fir.Fir2IrResultsConverter
import org.jetbrains.kotlin.test.frontend.fir.FirFrontendFacade
import org.jetbrains.kotlin.test.initIdeaConfiguration
import org.jetbrains.kotlin.test.model.ArtifactKinds
import org.jetbrains.kotlin.test.model.DependencyKind
import org.jetbrains.kotlin.test.model.FrontendKind
import org.jetbrains.kotlin.test.model.FrontendKinds
import org.jetbrains.kotlin.test.runners.AbstractKotlinCompilerTest
import org.jetbrains.kotlin.test.runners.codegen.commonConfigurationForTest
import org.jetbrains.kotlin.test.services.EnvironmentBasedStandardLibrariesPathProvider
import org.jetbrains.kotlin.test.services.KotlinStandardLibrariesPathProvider
import org.jetbrains.kotlin.test.services.configuration.CommonEnvironmentConfigurator
import org.jetbrains.kotlin.test.services.configuration.JvmEnvironmentConfigurator
import org.junit.jupiter.api.BeforeAll

abstract class AbstractTestRunner : AbstractKotlinCompilerTest() {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setUp() {
            initIdeaConfiguration() // set system property to initialize idea service
        }
    }

    override fun TestConfigurationBuilder.configuration() {
        val targetFrontend: FrontendKind<*> = FrontendKinds.FIR
        globalDefaults {
            frontend = targetFrontend
            targetBackend = TargetBackend.JVM_IR
            targetPlatform = JvmPlatforms.defaultJvmPlatform
            artifactKind = ArtifactKinds.Jvm
            dependencyKind = DependencyKind.Source
            languageSettings {
                languageVersion = LanguageVersion.KOTLIN_2_1
                apiVersion = ApiVersion.KOTLIN_2_1
            }
//            languageSettings {
//                languageVersion = LanguageVersion.KOTLIN_1_9
//                apiVersion = ApiVersion.KOTLIN_1_9
//            }
        }

        defaultDirectives {
            FIR_PARSER with FirParser.LightTree

        }

        when (targetFrontend) {
            FrontendKinds.ClassicFrontend -> {
                commonConfigurationForTest(
                    FrontendKinds.ClassicFrontend,
                    ::ClassicFrontendFacade,
                    ::ClassicFrontend2IrConverter,
                    ::JvmIrBackendFacade
                ) { }
            }
            FrontendKinds.FIR -> {
                commonConfigurationForTest(
                    FrontendKinds.FIR,
                    ::FirFrontendFacade,
                    ::Fir2IrResultsConverter,
                    ::JvmIrBackendFacade
                ) { }
            }
        }

//        commonConfigurationForTest(
//            FrontendKinds.FIR,
//            ::FirFrontendFacade,
//            ::Fir2IrResultsConverter,
//            ::JvmIrBackendFacade
//        ) { }

        configureHandlers()
        configureFirHandlersStep {

        }

        configureJvmArtifactsHandlersStep {
        }

        useConfigurators(
            ::CommonEnvironmentConfigurator,     // compiler flags
            ::JvmEnvironmentConfigurator,        // jdk and kotlin runtime configuration (e.g. FULL_JDK)
            ::SuspendTransformerEnvironmentConfigurator,    // compiler plugin configuration
        )
    }

    abstract fun TestConfigurationBuilder.configureHandlers()

    override fun createKotlinStandardLibrariesPathProvider(): KotlinStandardLibrariesPathProvider {
        return EnvironmentBasedStandardLibrariesPathProvider
    }
}
