package com.github.yougitlabprojects.merge.request;

import com.github.yougitlabprojects.component.SearchBoxModel;
import com.github.yougitlabprojects.configuration.ProjectState;
import com.github.yougitlabprojects.merge.info.BranchInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.SortedComboBoxModel;
import org.apache.commons.lang.StringUtils;
import org.gitlab.api.models.GitlabUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Dialog fore creating merge requests
 *
 * @author ppolivka
 * @since 30.10.2015
 */
public class CreateMergeRequestDialog extends DialogWrapper {

    private Project project;

    private JPanel mainView;
    private JComboBox targetBranch;
    private JComboBox currentBranch;
    private JTextField mergeTitle;
    private JTextArea mergeDescription;
    private JButton diffButton;
    private JComboBox assigneeBox;
    private JCheckBox removeSourceBranch;
    private JCheckBox wip;

    private SortedComboBoxModel<BranchInfo> currentBranchModel;
    private SortedComboBoxModel<BranchInfo> targetBranchModel;
    private BranchInfo lastSelectedBranch;

    final ProjectState projectState;

    @NotNull
    final GitLabCreateMergeRequestWorker mergeRequestWorker;

    public CreateMergeRequestDialog(@Nullable Project project, @NotNull GitLabCreateMergeRequestWorker gitLabMergeRequestWorker) {
        super(project);
        this.project = project;
        projectState = ProjectState.getInstance(project);
        mergeRequestWorker = gitLabMergeRequestWorker;
        init();

    }

    @Override
    protected void init() {
        super.init();
        setTitle("Create Merge Request");
        setVerticalStretch(2f);

        SearchBoxModel searchBoxModel = new SearchBoxModel(assigneeBox, mergeRequestWorker.getSearchableUsers());
        assigneeBox.setModel(searchBoxModel);
        assigneeBox.setEditable(true);
        assigneeBox.addItemListener(searchBoxModel);
        assigneeBox.setBounds(140, 170, 180, 20);

        currentBranchModel = new SortedComboBoxModel<>((o1, o2) -> StringUtil.naturalCompare(o1.getName(), o2.getName()));
        currentBranchModel.setAll(mergeRequestWorker.getLocalBranches());

        String currentBranchName = mergeRequestWorker.getGitLocalBranch().getName();
        currentBranch.setModel(currentBranchModel);
        currentBranchModel.getItems().stream().filter(b -> currentBranchName.equals(b.getName())).findFirst().ifPresent( b -> currentBranch.setSelectedItem(b));

        targetBranchModel = new SortedComboBoxModel<>((o1, o2) -> StringUtil.naturalCompare(o1.getName(), o2.getName()));
        targetBranchModel.setAll(mergeRequestWorker.getRemoteBranches());
        targetBranch.setModel(targetBranchModel);
        targetBranch.setSelectedIndex(0);
        if (mergeRequestWorker.getLastUsedBranch() != null) {
            targetBranch.setSelectedItem(mergeRequestWorker.getLastUsedBranch());
        }
        lastSelectedBranch = getSelectedBranch();

        targetBranch.addActionListener(e -> {
            prepareTitle();
            lastSelectedBranch = getSelectedBranch();
            projectState.setLastMergedBranch(getSelectedBranch().getName());
            mergeRequestWorker.getDiffViewWorker().launchLoadDiffInfo(mergeRequestWorker.getLocalBranchInfo(), getSelectedBranch());
        });

        prepareTitle();

        Boolean deleteMergedBranch = projectState.getDeleteMergedBranch();
        if(deleteMergedBranch != null && deleteMergedBranch) {
            this.removeSourceBranch.setSelected(true);
        }

        Boolean mergeAsWorkInProgress = projectState.getMergeAsWorkInProgress();
        if(mergeAsWorkInProgress != null && mergeAsWorkInProgress) {
            this.wip.setSelected(true);
        }

        diffButton.addActionListener(e -> mergeRequestWorker.getDiffViewWorker().showDiffDialog(mergeRequestWorker.getLocalBranchInfo(), getSelectedBranch()));
    }

    @Override
    protected void doOKAction() {
        BranchInfo branch = getSelectedBranch();
        if (mergeRequestWorker.checkAction(branch)) {
            String title = mergeTitle.getText();
            if(wip.isSelected()) {
                title = "WIP:"+title;
            }
            mergeRequestWorker.createMergeRequest(branch, getAssignee(), title, mergeDescription.getText(), removeSourceBranch.isSelected());
            super.doOKAction();
        }
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (StringUtils.isBlank(mergeTitle.getText())) {
            return new ValidationInfo("Merge title cannot be empty", mergeTitle);
        }
//        if (getSelectedBranch().getName().equals(currentBranch.getText())) {
        if (getSelectedBranch().equals(currentBranch.getSelectedItem())) {
            return new ValidationInfo("Target branch must be different from current branch.", targetBranch);
        }
        return null;
    }

    private BranchInfo getSelectedBranch() {
        return (BranchInfo) targetBranch.getSelectedItem();
    }

    @Nullable
    private GitlabUser getAssignee() {
        SearchableUser searchableUser = (SearchableUser) this.assigneeBox.getSelectedItem();
        if(searchableUser != null) {
            return searchableUser.getGitLabUser();
        }
        return null;
    }

    private void prepareTitle() {
        if (StringUtils.isBlank(mergeTitle.getText()) || mergeTitleGenerator(lastSelectedBranch).equals(mergeTitle.getText())) {
            mergeTitle.setText(mergeTitleGenerator(getSelectedBranch()));
        }
    }

    private String mergeTitleGenerator(BranchInfo branchInfo) {
        return "Merge of " + ((BranchInfo) currentBranch.getSelectedItem()).getName() + " to " + branchInfo;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainView;
    }
}
