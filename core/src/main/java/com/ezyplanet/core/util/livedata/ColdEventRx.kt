package com.ezyplanet.core.util.livedata

import androidx.lifecycle.Observer


open class ColdEventRx <out T>(private val content: T?) {

    private val classesThatHandledTheEvent = HashSet<String>(0)



    fun getContentIfNotHandled(classThatWantToUseEvent: Any): T? {
        val canonicalName = classThatWantToUseEvent::javaClass.get().canonicalName

        canonicalName?.let {
            return if (!classesThatHandledTheEvent.contains(canonicalName)) {
                classesThatHandledTheEvent.add(canonicalName)
                content
            } else {
                null
            }
        } ?: return null
    }
    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content!!


}

class ColdEventObserver<T>(private val classThatWantToUseEvent: Any, private val onEventUnhandledContent: (T) -> Unit) : Observer<ColdEventRx<T>> {
    override fun onChanged(event: ColdEventRx<T>?) {
        event?.getContentIfNotHandled(classThatWantToUseEvent)?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}