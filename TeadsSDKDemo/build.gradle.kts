// ---------------------------------------------------------------------------
// Publisher-on-Kotlin-1.9.24 workaround — applied project-wide
// ---------------------------------------------------------------------------
// The Teads SDK is compiled with Kotlin 2.1.21; its class files carry Kotlin
// metadata version 2.1.0. Compiler 1.9.x can only read up to metadata 2.0 by
// default. Without this flag any module that touches the Teads SDK API fails
// with: "Class '...' was compiled with an incompatible version of Kotlin."
//
// `tasks.withType(...)` is project-scoped, so configuring it inside a single
// module's build.gradle.kts only fixes that module. Wrapping it in
// `subprojects { }` from the root build script applies the flag to every
// submodule with a single, maintainable declaration.
// ---------------------------------------------------------------------------
subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xskip-metadata-version-check"
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}