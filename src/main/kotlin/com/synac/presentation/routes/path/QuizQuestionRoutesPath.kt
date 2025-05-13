package com.synac.presentation.routes.path

import io.ktor.resources.*

@Resource(path = "/quiz/questions")
class QuizQuestionRoutesPath(
    val topicCode: Int? = null
) {

    @Resource(path = "{questionId}")
    data class ById(
        val parent: QuizQuestionRoutesPath = QuizQuestionRoutesPath(),
        val questionId: String
    )

    @Resource(path = "bulk")
    data class Bulk(
        val parent: QuizQuestionRoutesPath = QuizQuestionRoutesPath()
    )

    @Resource(path = "random")
    data class Random(
        val parent: QuizQuestionRoutesPath = QuizQuestionRoutesPath(),
        val topicCode: Int? = null,
        val limit: Int? = null
    )

}