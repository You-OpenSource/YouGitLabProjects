package com.github.youopensource.yougitlab.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class FrontendSidePanelResult {
    @SerializedName("frontend_code_snippet")
    @Expose
    var frontendCodeSnippet: String? = null

    @SerializedName("frontend_text")
    @Expose
    var frontendText: String? = null

    @SerializedName("frontend_output")
    @Expose
    var frontendOutput: String? = null

    @SerializedName("frontend_try_yourself_link")
    @Expose
    var frontendTryYourselfLink: String? = null
}
