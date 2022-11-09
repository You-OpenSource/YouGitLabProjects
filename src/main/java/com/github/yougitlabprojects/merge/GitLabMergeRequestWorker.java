package com.github.yougitlabprojects.merge;

import com.github.yougitlabprojects.configuration.ProjectState;
import com.github.yougitlabprojects.configuration.SettingsState;
import com.github.yougitlabprojects.exception.MergeRequestException;
import com.github.yougitlabprojects.merge.helper.GitLabProjectMatcher;
import com.github.yougitlabprojects.util.MessageUtil;
import com.github.yougitlabprojects.util.GitLabUtil;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.commands.Git;
import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import org.gitlab.api.models.GitlabProject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Interface for worker classes that are related to merge requests
 *
 * @author ppolivka
 * @since 31.10.2015
 */
public interface GitLabMergeRequestWorker {

  String CANNOT_CREATE_MERGE_REQUEST = "Cannot Create Merge Request";

  Git getGit();

  Project getProject();

  ProjectState getProjectState();

  GitRepository getGitRepository();

  String getRemoteUrl();

  GitlabProject getGitlabProject();

  String getRemoteProjectName();

  GitLabDiffViewWorker getDiffViewWorker();

  void setGit(Git git);

  void setProject(Project project);

  void setProjectState(ProjectState projectState);

  void setGitRepository(GitRepository gitRepository);

  void setRemoteUrl(String remoteUrl);

  void setGitlabProject(GitlabProject gitlabProject);

  void setRemoteProjectName(String remoteProjectName);

  void setDiffViewWorker(GitLabDiffViewWorker diffViewWorker);

  class Util {

    private static SettingsState settingsState = SettingsState.getInstance();
    private static GitLabProjectMatcher projectMatcher = new GitLabProjectMatcher();

    public static void fillRequiredInfo(@NotNull final GitLabMergeRequestWorker mergeRequestWorker, @NotNull final Project project, @Nullable final VirtualFile file) throws
            MergeRequestException {
      ProjectState projectState = ProjectState.getInstance(project);
      mergeRequestWorker.setProjectState(projectState);

      mergeRequestWorker.setProject(project);

      Git git = ServiceManager.getService(Git.class);
      mergeRequestWorker.setGit(git);

      GitRepository gitRepository = GitLabUtil.getGitRepository(project, file);
      if (gitRepository == null) {
        MessageUtil.showErrorDialog(project, "Can't find git repository", CANNOT_CREATE_MERGE_REQUEST);
        throw new MergeRequestException();
      }
      gitRepository.update();
      mergeRequestWorker.setGitRepository(gitRepository);

      Pair<GitRemote, String> remote = GitLabUtil.findGitLabRemote(gitRepository);
      if (remote == null) {
        MessageUtil.showErrorDialog(project, "Can't find GitLab remote", CANNOT_CREATE_MERGE_REQUEST);
        throw new MergeRequestException();
      }

      String remoteProjectName = remote.first.getName();
      mergeRequestWorker.setRemoteProjectName(remoteProjectName);
      mergeRequestWorker.setRemoteUrl(remote.getSecond());

      try {
        Integer projectId;
        Optional<GitlabProject> gitlabProject = projectMatcher.resolveProject(projectState, remote.getFirst(), gitRepository);
        projectId = gitlabProject.orElseThrow(() -> new RuntimeException("No project found")).getId();

        mergeRequestWorker.setGitlabProject(settingsState.api(gitRepository).getProject(projectId));
      } catch (Exception e) {
        MessageUtil.showErrorDialog(project, "Cannot find this project in GitLab Remote", CANNOT_CREATE_MERGE_REQUEST);
        throw new MergeRequestException(e);
      }

      mergeRequestWorker.setDiffViewWorker(new GitLabDiffViewWorker(project, mergeRequestWorker.getGitRepository()));
    }
  }

}
