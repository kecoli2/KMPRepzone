package com.repzone.domain.events.base

import com.repzone.core.platform.Logger
import com.repzone.domain.events.DomainEvent

interface IEventLogger {
    /**
     * Event emit edildiğinde çağrılır
     *
     * @param event Emit edilen event
     * @param subscriberCount Kaç subscriber dinliyor
     */
    fun logEmit(event: DomainEvent, subscriberCount: Int)

    /**
     * Yeni subscription başladığında çağrılır
     *
     * @param eventType Subscribe edilen event tipi
     */
    fun logSubscribe(eventType: String)

    /**
     * Subscription iptal edildiğinde çağrılır
     *
     * @param eventType Unsubscribe edilen event tipi
     */
    fun logUnsubscribe(eventType: String)
}

class EventLoggerLogCat : IEventLogger {
    override fun logEmit(event: DomainEvent, subscriberCount: Int) {
        Logger.d("[EventBus]","Emitting: ${event::class.simpleName} to $subscriberCount subscribers")
    }

    override fun logSubscribe(eventType: String) {
        Logger.d("[EventBus]", "New subscriber for: $eventType")
    }

    override fun logUnsubscribe(eventType: String) {
        Logger.d("[EventBus]" ,"Unsubscribed from: $eventType")
    }

}

class NoOpEventLogger : IEventLogger {
    override fun logEmit(event: DomainEvent, subscriberCount: Int) {}
    override fun logSubscribe(eventType: String) {}
    override fun logUnsubscribe(eventType: String) {}
}