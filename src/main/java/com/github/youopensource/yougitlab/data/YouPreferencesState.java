package com.github.youopensource.yougitlab.data;

import java.util.Objects;

public class YouPreferencesState {
    public boolean onlySelectionSearch;

    public YouPreferencesState() {
        this.onlySelectionSearch = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YouPreferencesState that = (YouPreferencesState) o;
        return onlySelectionSearch == that.onlySelectionSearch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(onlySelectionSearch);
    }
}
