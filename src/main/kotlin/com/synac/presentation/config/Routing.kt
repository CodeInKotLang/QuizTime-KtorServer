package com.synac.presentation.config

import com.synac.domain.repository.IssueReportRepository
import com.synac.domain.repository.QuizQuestionRepository
import com.synac.domain.repository.QuizTopicRepository
import com.synac.presentation.routes.issueReportRoutes
import com.synac.presentation.routes.quizQuestionRoutes
import com.synac.presentation.routes.quizTopicRoutes
import com.synac.presentation.routes.root
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    install(Resources)

    val quizQuestionRepository: QuizQuestionRepository by inject()
    val quizTopicRepository: QuizTopicRepository by inject()
    val issueReportRepository: IssueReportRepository by inject()

    routing {

        root()
        quizQuestionRoutes(quizQuestionRepository)
        quizTopicRoutes(quizTopicRepository)
        issueReportRoutes(issueReportRepository)

        staticResources(
            remotePath = "/images",
            basePackage = "images"
        )

    }
}