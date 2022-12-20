package com.github.youopensource.yougitlab.screen

import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.panels.VerticalLayout

class DataProviderPanel : JBPanelWithEmptyText(VerticalLayout(5)), DataProvider {
    override fun getData(dataId: String): Any? = null
}
