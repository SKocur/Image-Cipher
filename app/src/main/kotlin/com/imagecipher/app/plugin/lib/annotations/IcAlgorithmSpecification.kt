package com.imagecipher.app.plugin.lib.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IcAlgorithmSpecification(val algorithmName: String = "")
