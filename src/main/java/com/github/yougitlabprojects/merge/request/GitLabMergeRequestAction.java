package com.github.yougitlabprojects.merge.request;

import com.github.yougitlabprojects.common.GitLabApiAction;
import com.github.yougitlabprojects.util.GitLabUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import git4idea.DialogManager;

/**
 * GitLab Merge Request Action class
 *
 * @author ppolivka
 * @since 30.10.2015
 */
public class GitLabMergeRequestAction extends GitLabApiAction {

    public GitLabMergeRequestAction() {
        super("Create _Merge Request...", "Creates merge request from current branch", AllIcons.Vcs.Merge);
    }

    @Override
    public void apiValidAction(AnActionEvent anActionEvent) {

        if (!GitLabUtil.testGitExecutable(project)) {
            return;
        }

        GitLabCreateMergeRequestWorker mergeRequestWorker = GitLabCreateMergeRequestWorker.create(project, file);
        if(mergeRequestWorker != null) {
            CreateMergeRequestDialog createMergeRequestDialog = new CreateMergeRequestDialog(project, mergeRequestWorker);
            DialogManager.show(createMergeRequestDialog);
        }
    }
}
