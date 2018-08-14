package com.clouway.loggingservice.adapter.gae.datastore

import com.clouway.entityhelper.TypedEntity
import com.clouway.entityhelper.toUtilDate
import com.clouway.loggingservice.core.Log
import com.clouway.loggingservice.core.Logger
import com.clouway.pubsub.core.event.Event
import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.FetchOptions.Builder.withLimit
import com.google.appengine.api.datastore.Query
import com.google.appengine.repackaged.com.google.gson.Gson
import org.eclipse.jetty.http.HttpStatus
import java.time.LocalDateTime

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class DatastoreLogger(private val limit: Int = 100) : Logger {

    override fun getEventsFromTo(from: LocalDateTime, to: LocalDateTime): List<Log> {
        return mapEntitiesToList(service.prepare(Query("Log")
                .setFilter(Query.CompositeFilter(Query.CompositeFilterOperator.AND,
                        listOf(Query.FilterPredicate("time",
                                        Query.FilterOperator.GREATER_THAN,
                                        from.toUtilDate()),
                                Query.FilterPredicate("time",
                                        Query.FilterOperator.LESS_THAN,
                                        to.toUtilDate())))))
                .asList(withLimit(limit)))
    }

    override fun getEventsFromExactTime(time: LocalDateTime): List<Log> {
        return mapEntitiesToList(service.prepare(Query("Log")
                .setFilter(Query.FilterPredicate("time",
                        Query.FilterOperator.EQUAL,
                        time.toUtilDate())))
                .asList(withLimit(limit)))
    }

    override fun log(event: Event, eventType: Class<*>, time: LocalDateTime): Int {
        return try {
            service.put(mapLogToEntity(Log(
                    event,
                    eventType,
                    time
            )))
            HttpStatus.OK_200
        } catch (e: Exception) {
            e.printStackTrace()
            HttpStatus.INTERNAL_SERVER_ERROR_500
        }
    }

    private fun mapLogToEntity(log: Log): Entity {
        val entity = Entity("Log")
        val gson = Gson()
        val typedEntity = TypedEntity(entity)
        typedEntity.setIndexedProperty("time", log.time.toUtilDate())
        typedEntity.setIndexedProperty("eventType", log.eventType.name)
        typedEntity.setIndexedProperty("event", gson.toJson(log.event))
        return typedEntity.raw()
    }

    private fun mapEntitiesToList(entityList: List<Entity>): List<Log> {
        val gson = Gson()
        val logList = mutableListOf<Log>()
        entityList.forEach {
            val typedEntity = TypedEntity(it)
            val eventType = Class.forName(typedEntity.string("eventType"))
            logList.add(Log(
                    gson.fromJson(typedEntity.string("event"), eventType) as Event,
                    eventType,
                    typedEntity.dateTimeValueOrNull("time")!!
            ))
        }
        return logList
    }

    private val service: DatastoreService
        get() = DatastoreServiceFactory.getDatastoreService()
}