package com.synac.presentation.routes

import com.synac.domain.model.IssueReport
import com.synac.domain.repository.IssueReportRepository
import com.synac.domain.util.onFailure
import com.synac.domain.util.onSuccess
import com.synac.presentation.routes.path.IssueReportRoutesPath
import com.synac.presentation.util.respondWithError
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route

fun Route.issueReportRoutes(
    repository: IssueReportRepository
) {

    post<IssueReportRoutesPath> {
        val report = call.receive<IssueReport>()
        repository.insertIssueReport(report)
            .onSuccess {
                call.respond(
                    message = "Report submitted successfully",
                    status = HttpStatusCode.Created
                )
            }
            .onFailure { error ->
                respondWithError(error)
            }
    }

    get<IssueReportRoutesPath> {
        repository.getAllIssueReports()
            .onSuccess { reports ->
                call.respond(
                    message = reports,
                    status = HttpStatusCode.OK
                )
            }
            .onFailure { error ->
                respondWithError(error)
            }
    }

    delete<IssueReportRoutesPath.ById> { path ->
        repository.deleteIssueReportById(path.reportId)
            .onSuccess {
                call.respond(HttpStatusCode.NoContent)
            }
            .onFailure { error ->
                respondWithError(error)
            }
    }


}