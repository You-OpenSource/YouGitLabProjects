package com.github.youopensource.yougitlab.services

import com.github.youopensource.redhat.devtools.intellij.telemetry.core.service.TelemetryMessageBuilder

object TelemetryService {

    val instance: TelemetryMessageBuilder by lazy {
        TelemetryMessageBuilder(
            TelemetryService::class.java.classLoader
        )
    }

}
