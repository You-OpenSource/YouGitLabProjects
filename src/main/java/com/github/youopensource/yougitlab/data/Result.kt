package com.github.youopensource.yougitlab.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class Result {
    @SerializedName("frontend_title")
    @Expose
    var frontendTitle: String? = null

    @SerializedName("frontend_url")
    @Expose
    var frontendUrl: String? = null

    @SerializedName("frontend_code_snippet")
    @Expose
    var frontendCodeSnippet: String? = null

    @SerializedName("frontend_text")
    @Expose
    var frontendText: String? = null

    @SerializedName("frontend_try_yourself_link")
    @Expose
    var frontendTryYourselfLink: String? = null

    @SerializedName("frontend_side_panel_results")
    @Expose
    var frontendSidePanelResults: List<FrontendSidePanelResult>? = null

    @SerializedName("code_snippets")
    @Expose
    var codeSnippets: List<CodeSnippet>? = null

    @SerializedName("snippet_code")
    @Expose
    var codeSnippet: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null
}
