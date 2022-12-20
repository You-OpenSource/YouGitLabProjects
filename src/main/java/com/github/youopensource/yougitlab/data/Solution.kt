package com.github.youopensource.yougitlab.data

data class Solution(
    val number: Int,
    val codeSnippet: String?,
    val solutionText: String? = null,
    val solutionLink: String? = null
)
