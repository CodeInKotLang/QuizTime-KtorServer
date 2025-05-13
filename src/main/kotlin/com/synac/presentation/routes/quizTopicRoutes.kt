package com.synac.presentation.routes

import com.synac.domain.model.QuizTopic
import com.synac.domain.repository.QuizTopicRepository
import com.synac.domain.util.onFailure
import com.synac.domain.util.onSuccess
import com.synac.presentation.routes.path.QuizTopicRoutesPath
import com.synac.presentation.util.respondWithError
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route

fun Route.quizTopicRoutes(
    repository: QuizTopicRepository
) {

    post<QuizTopicRoutesPath> {
        val quizTopic = call.receive<QuizTopic>()
        repository.upsertTopic(quizTopic)
            .onSuccess {
                call.respond(
                    message = "Quiz Topic added",
                    status = HttpStatusCode.Created
                )
            }
            .onFailure { error ->
                respondWithError(error)
            }
    }

    get<QuizTopicRoutesPath> {
        repository.getAllTopics()
            .onSuccess { topics ->
                call.respond(
                    message = topics,
                    status = HttpStatusCode.OK
                )
            }
            .onFailure { error ->
                respondWithError(error)
            }
    }

    get<QuizTopicRoutesPath.ById> { path ->
        repository.getTopicById(path.topicId)
            .onSuccess { quizTopic ->
                call.respond(
                    message = quizTopic,
                    status = HttpStatusCode.OK
                )
            }
            .onFailure { error ->
                respondWithError(error)
            }
    }

    delete<QuizTopicRoutesPath.ById> { path ->
        repository.deleteTopicById(path.topicId)
            .onSuccess {
                call.respond(HttpStatusCode.NoContent)
            }
            .onFailure { error ->
                respondWithError(error)
            }
    }

}