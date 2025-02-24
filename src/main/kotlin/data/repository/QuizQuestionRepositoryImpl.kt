package com.synac.data.repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.synac.data.database.entity.QuizQuestionEntity
import com.synac.data.mapper.toQuizQuestion
import com.synac.data.mapper.toQuizQuestionEntity
import com.synac.data.util.Constant.QUESTIONS_COLLECTION_NAME
import com.synac.domain.model.QuizQuestion
import com.synac.domain.repository.QuizQuestionRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class QuizQuestionRepositoryImpl(
    mongoDatabase: MongoDatabase
) : QuizQuestionRepository {

    private val questionCollection = mongoDatabase
        .getCollection<QuizQuestionEntity>(QUESTIONS_COLLECTION_NAME)

    override suspend fun upsertQuestion(question: QuizQuestion) {
        try {
            if (question.id == null) {
                questionCollection.insertOne(question.toQuizQuestionEntity())
            } else {
                val filterQuery = Filters.eq(
                    QuizQuestionEntity::_id.name, question.id
                )
                val updateQuery = Updates.combine(
                    Updates.set(QuizQuestionEntity::question.name, question.question),
                    Updates.set(QuizQuestionEntity::correctAnswer.name, question.correctAnswer),
                    Updates.set(QuizQuestionEntity::incorrectAnswers.name, question.incorrectAnswers),
                    Updates.set(QuizQuestionEntity::explanation.name, question.explanation),
                    Updates.set(QuizQuestionEntity::topicCode.name, question.topicCode)
                )
                questionCollection.updateOne(filterQuery, updateQuery)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getAllQuestions(
        topicCode: Int?,
        limit: Int?
    ): List<QuizQuestion> {
        return try {
            val questionLimit = limit?.takeIf { it > 0 } ?: 10
            val filterQuery = topicCode?.let {
                Filters.eq(QuizQuestionEntity::topicCode.name, it)
            } ?: Filters.empty()

            questionCollection
                .find(filter = filterQuery)
                .limit(questionLimit)
                .map { it.toQuizQuestion() }
                .toList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getQuestionById(id: String): QuizQuestion? {
        return try {
            val filterQuery = Filters.eq(
                QuizQuestionEntity::_id.name, id
            )
            val questionEntity = questionCollection
                .find(filter = filterQuery)
                .firstOrNull()
            questionEntity?.toQuizQuestion()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun deleteQuestionById(id: String): Boolean {
        return try {
            val filterQuery = Filters.eq(
                QuizQuestionEntity::_id.name, id
            )
            val deleteResult = questionCollection
                .deleteOne(filter = filterQuery)
            deleteResult.deletedCount > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}