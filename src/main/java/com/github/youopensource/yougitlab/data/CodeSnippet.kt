package com.github.youopensource.yougitlab.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class CodeSnippet {
    @SerializedName("language")
    @Expose
    var language: String? = null

    @SerializedName("author")
    @Expose
    var author: String? = null

    @SerializedName("author_link")
    @Expose
    var authorLink: String? = null

    @SerializedName("snippet_title")
    @Expose
    var snippetTitle: String? = null

    @SerializedName("snippet_code")
    @Expose
    var snippetCode: String? = null
}
