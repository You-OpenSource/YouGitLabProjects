package com.github.youopensource.yougitlab.screen

import com.intellij.util.ui.JBUI
import java.awt.Insets
import javax.swing.JButton

class SmallButton(label: String) : JButton(label) {
    override fun getInsets(insets: Insets): Insets {
        return insets
    }

    override fun getInsets(): Insets {
        return JBUI.emptyInsets()
    }

    init {
        font = JBUI.Fonts.smallFont()
    }
}
