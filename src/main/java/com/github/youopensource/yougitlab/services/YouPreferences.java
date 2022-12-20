package com.github.youopensource.yougitlab.services;

import com.github.youopensource.yougitlab.data.YouPreferencesState;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

@State(name = "YouReloadiumPreferences", storages = @Storage("youcom_reloadium.xml"))
public class YouPreferences implements PersistentStateComponent<YouPreferencesState> {
    YouPreferencesState state = new YouPreferencesState();

    public static YouPreferences getInstance() {
        return ApplicationManager.getApplication().getService(YouPreferences.class);
    }

    @NotNull
    @Override
    public YouPreferencesState getState() {
        return this.state;
    }

    @Override
    public void loadState(@NotNull YouPreferencesState state) {
        XmlSerializerUtil.copyBean(state, this.state);
    }
}
