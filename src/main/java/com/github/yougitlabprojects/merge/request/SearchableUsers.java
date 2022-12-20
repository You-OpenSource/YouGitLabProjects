package com.github.yougitlabprojects.merge.request;

import com.github.yougitlabprojects.component.Searchable;
import com.github.yougitlabprojects.configuration.SettingsState;
import com.github.yougitlabprojects.util.MessageUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.gitlab.api.models.GitlabProject;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Searchable users model
 *
 * @author ppolivka
 * @since 1.4.0
 */
public class SearchableUsers implements Searchable<SearchableUser, String> {

    private Project project;
    private VirtualFile file;
    private GitlabProject gitlabProject;
    private Collection<SearchableUser> initialModel;
    private static SettingsState settingsState = SettingsState.getInstance();

    public SearchableUsers(Project project, VirtualFile file, GitlabProject gitlabProject) {
        this.project = project;
        this.file = file;
        this.gitlabProject = gitlabProject;
        this.initialModel = search("");
    }

    @Override
    public Collection<SearchableUser> search(String toSearch) {
        try {
            return settingsState
                    .api(project, file)
                    .searchUsers(gitlabProject, toSearch)
                    .stream()
                    .map(SearchableUser::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            MessageUtil.showErrorDialog(project, "New remote origin cannot be added to this project.", "Cannot Add New Remote");
        }
        return emptyList();
    }

    public Collection<SearchableUser> getInitialModel() {
        return initialModel;
    }

    public void setInitialModel(Collection<SearchableUser> initialModel) {
        this.initialModel = initialModel;
    }
}
