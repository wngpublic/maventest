package org.wayne.misc.concurrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PubSubPattern models publisher and subscriber pattern.
 * 
 * A subscriber registers to listen to an event emitted from a publisher.
 * 
 * A subscriber can register to one or many publishers.
 * 
 * Each publisher can emit different Event types that are predefined.
 * 
 * A subscriber can act as a publisher to a downstream subscriber.
 * 
 * Issues:
 * An unauthorized publisher may publish to subscribers and flood
 * a network. An authorized publisher may malfunction and also
 * flood a network. There is no guarantee that a subscriber is listening
 * when a publisher is publishing. There is no data recover or write
 * ahead logging, so data is lossy and redundant. 
 * 
 * To remedy unauthorized publisher, maybe publisher needs to register
 * with master node. If master authorizes, master passes token to publisher.
 * Any subscriber who wishes to subscribe to this publisher can validate
 * that it is a trusted publisher. 
 * 
 * To remedy a flooding or malfunctioning publisher, subscribers may 
 * rate limit event receiving from that publisher. Then subscriber may
 * convert to polling model, and poll for events from publisher instead.
 */
public class PubSubPattern {

private static long ctrGUID = 0;

public static long nextGUID() {
    return PubSubPattern.ctrGUID++;
}

public static long curGUID() {
    return PubSubPattern.ctrGUID;
}

public enum EventType {
    MESSAGE,
    SAVE,
    READ,
    MAP,
    JSON,
    FORWARD,
    REQUEST,
    RESPONSE,
    ALL,
    DEFAULT
}

public class EventStatus {
    public static final int PENDING = 0;
    public static final int ARCHIVED = 1;
    public static final int NULL = -1;
}

public interface Event {
    public EventType type();
    public Object payload();
    public Event payload(EventType type, Object obj);
    public Integer id();
    public String message();
    public Event message(String message);
    public Long guid();
    public long tsms();
}

public class BaseEvent implements Event {

    EventType eventType;
    Object payload;
    String message;
    final Integer id;
    final Long guid;
    final Long tsms;
    
    
    public BaseEvent(final Integer id) {
        this.id = id;
        this.guid = PubSubPattern.nextGUID();
        this.tsms = System.currentTimeMillis();
    }
    
    public BaseEvent(
            final Integer id,
            final EventType eventType,
            final Object payload,
            final String message)
    {
        this(id);
        this.eventType = eventType;
        this.payload = payload;
        this.message = message;
    }
    
    @Override
    public EventType type() {
        return eventType;
    }

    @Override
    public Object payload() {
        return payload;
    }

    @Override
    public Event payload(EventType type, Object obj) {
        this.eventType = type;
        this.payload = obj;
        return this;
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public Event message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public Long guid() {
        return guid;    }

    @Override
    public long tsms() {
        return tsms;
    }
}

public interface Callback {
    public Integer id();
    public void event(Event e);
}

/**
 * Publisher adds pay load into its receive queue.
 * At periodic time, publisher publishes an event, and 
 * then puts the published event into an archived queue.
 * 
 * This is a thread safe class, or at least intended to be...
 */
public class Publisher {

    /** map of CallbackId->Callback. */
    Map<Integer, Callback> mapListeners = new HashMap<>();
    /** map of Callback->set of EventType callback registered for. */
    Map<Callback, Set<EventType>> mapCallbackToEvents = new HashMap<>();
    /** map of EventType->set of Callbacks registered for this EventType. */
    Map<EventType, Set<Callback>> mapEventToCallbacks = new HashMap<>();
    /** set of pending Event Ids. */
    Set<Integer> setPendingIds = new HashSet<>();
    /** map of archived Event Ids and their location in circular list. */
    Map<Integer, Integer> mapArchivedIds = new HashMap<>();
    
    /** Linked list of pending events. */
    LinkedList<Event> listPending = new LinkedList<>();
    
    /** Circular list of archived events. */
    List<Event> listArchived = new ArrayList<>();
    
    int maxPending = 1000;
    int maxArchive = 100000;
    int idxTArchive = 0;

    /** 
     * firstAvailableArchivedEventId is the event Id of the first available
     * event in archived list. The archived list is a circular list, so the 
     * first available is at most == last available - list size.
     */
    int firstAvailableArchivedEventId = -1;

    /**
     * lastAvailableArchivedEventId is the event id of the last available
     * event in archived list. It is always the last event that got transferred
     * from the active/pending list.
     */
    int lastAvailableArchivedEventId = -1;

    long numTotalPublished = 0;
    
    /** The ID of this publisher.  */
    private final Integer id;
    
    public Publisher(Integer id) {
        this.id = id;
    }
    
    public Integer id() {
        return id;
    }
    
    public void reset() {
        
    }
    
    public long totalPublished() {
        return numTotalPublished;
    }
    
    public int numPending() {
        return listPending.size();
    }
    
    public int numArchived() {
        return listArchived.size();
    }
    
    public void register(Callback c, Set<EventType> setTypes) {
        Integer id = c.id();

        synchronized(this) {
            /*
             * for each callback
             *     add to map of id->callback
             *     add to map of id->set of eventTypes to listen
             *     add to map of eventType->set of callbacks listening
             */

            mapListeners.put(id, c);
            
            for(EventType eventType: setTypes) {

                Set<Callback> setCallbacks = mapEventToCallbacks.get(eventType);
                if(setCallbacks == null) {
                    setCallbacks = new LinkedHashSet<>();
                }
                setCallbacks.add(c);
                
                Set<EventType> setTypesForId = mapCallbackToEvents.get(id);
                if(setTypesForId == null) {
                    setTypesForId = new LinkedHashSet<>();
                    mapCallbackToEvents.put(c, setTypesForId);
                }
                setTypesForId.add(eventType);
            }
        }
    }
    
    public void unregister(Callback c, Set<EventType> setTypes) {
        Integer id = c.id();
        
        synchronized(this) {
            /*
             * for each callback
             *     for each EventType that callback is registered to
             *         remove from map of id->set of eventTypes to listen
             *         remove from map of eventType->set of callbacks listening
             *     if map of id->set is empty
             *         remove map entry
             *         remove map id->callback
             */
            for(EventType eventType: setTypes) {
                
                Set<Callback> setCallbacks = mapEventToCallbacks.get(eventType);
                if(setCallbacks != null) {
                    setCallbacks.remove(c);
                }
                if(setCallbacks.size() == 0) {
                    mapEventToCallbacks.remove(eventType);
                }
                
                Set<EventType> setEventTypes = mapCallbackToEvents.get(c);
                if(setEventTypes != null) {
                    setEventTypes.remove(c);
                }
                if(setEventTypes.size() == 0) {
                    mapCallbackToEvents.remove(c);
                    mapListeners.remove(id);
                }
            }
        }
    }
    
    public int eventStatus(Integer id) {
        synchronized(this) {
            if(setPendingIds.contains(id)) {
                return EventStatus.PENDING;
            }
            if(mapArchivedIds.containsKey(id)) {
                return EventStatus.ARCHIVED;
            }
            return EventStatus.NULL;
        }
    }
    
    public int addEvent(Event e) {
        Integer id = e.id();
        synchronized(this) {
            listPending.add(e);
            setPendingIds.add(id);
        }
        return id;
    }
    
    public int publish(int maxEvents) {
        
        synchronized(this) {

            int numPublished = 0;
            
            for(int i = 0; i < maxEvents; i++) {
                Event event = pollEvent();
                if(event == null) {
                    break;
                }
                publishToListeners(event);
                numPublished++;
            }
            
            numTotalPublished += numPublished;

            return numPublished;
        }
    }
    
    synchronized Event pollEvent() {
        /*
         * poll event from list of pending events
         * add to list of archived events, which is circular list, and update
         *     first available archived event id and remove from id map
         *     last available archived event id and add to id map
         */
        Event event = listPending.poll();
        
        if(event == null) {
            return null;
        }
        
        Integer eventId = event.id();
        
        if(listArchived.size() < maxArchive) {
            listArchived.add(event);
            mapArchivedIds.put(eventId, idxTArchive);
            
            idxTArchive++;
            if(firstAvailableArchivedEventId == -1) {
                firstAvailableArchivedEventId = event.id();
            }
        } 
        else {
            Event eventPrevious = listArchived.get(idxTArchive);
            if(eventPrevious != null) {
                Integer eventIdPrevious = eventPrevious.id();
                mapArchivedIds.remove(eventIdPrevious);
            }
            mapArchivedIds.put(eventId, idxTArchive);

            listArchived.set(idxTArchive, event);

            idxTArchive++;
            idxTArchive = idxTArchive % maxArchive;

            firstAvailableArchivedEventId = listArchived.get(idxTArchive).id();
        }

        lastAvailableArchivedEventId = eventId;
        
        return event;
    }
    
    synchronized void publishToListeners(Event event) {
        EventType eventType = event.type();
        Set<Callback> setCallbacks = mapEventToCallbacks.get(eventType);
        
        if(setCallbacks != null) {
            for(Callback c: setCallbacks) {
                c.event(event);
            }
        }        
    }
    
    /**
     * Look in ArchivedList for eventId. If unavailable, get the next 
     * matching event for callback, based on event types to filter.
     * If set of event types is null, then return first matching event.
     * 
     * @param eventId
     * 
     * @param callback
     * 
     * @param filteredSet is a subset of the event types that the callback
     * is registered for this publisher. Return an event that only matches
     * this subset. If filteredSet is null, then match any event type that
     * this callback is registered for.
     * 
     * @return an Event.
     */
    public Event getArchivedEvent(
            int eventId, 
            Callback callback, 
            Set<EventType> filteredSet) 
    {
        Integer idCallback = callback.id();
        if(!mapListeners.containsKey(idCallback)) {
            return null;
        }
        
        Set<EventType> setEventTypesRegistered = mapCallbackToEvents.get(callback);
        if(setEventTypesRegistered == null) {
            return null;
        }
        
        Integer idxArchive = mapArchivedIds.get(eventId);
        
        if(idxArchive != null) {
            // if the eventId is in archived list, get it, if allowed.
            Event event = filterEvent(idxArchive, setEventTypesRegistered, filteredSet);
            return event;
        }
        else if(eventId > lastAvailableArchivedEventId) {
            return null;
        }
        else {
            /*
             * eventId is not in archived list, so get first available,
             * based on the callback's subscription type, if allowed.
             * 
             * There are 2 cases for first available to last available:
             * 
             * if the list has not reached max size, then last item is
             * just the tail.
             * 
             * if the list has already reached max list size, then the
             * first item keeps getting overwritten by the circular list.
             * 
             * idxArchive is the physical index location of first available
             * event id in the circular list.
             */
            idxArchive = firstAvailableArchivedEventId;
            int sizeList = listArchived.size();
            
            if(sizeList < maxArchive) {
                for(int i = idxArchive; i < sizeList; i++) {
                    Event event = filterEvent(i, setEventTypesRegistered, filteredSet);
                    if(event != null) {
                        return event;
                    }
                }
            }
            else {
                int idxLast = (idxArchive == 0) ? (sizeList - 1) : (idxArchive - 1);
                
                for(int i = idxArchive; i != idxLast; i = (i + 1) % sizeList) {
                    Event event = filterEvent(i, setEventTypesRegistered, filteredSet);
                    if(event != null) {
                        return event;
                    }
                }
            }
            return null;
        }
    }
    
    private Event filterEvent(
            int idx, 
            Set<EventType> setEventTypesRegistered,
            Set<EventType> filteredSet) 
    {
        Event event = listArchived.get(idx);
        EventType eventType = event.type();
        if(!setEventTypesRegistered.contains(eventType)) {
            return null;
        }
        if(filteredSet == null) {
            return event;
        }
        if(filteredSet.contains(eventType)) {
            return event;
        }
        return null;
    }
    
    public Integer getFirstAvailableArchivedEventId(Callback callback) {
        return null;
    }
    
    public Integer getFirstAvailableArchivedEventId() {
        return firstAvailableArchivedEventId;
    }
    
    public Integer getFirstArchivedIndex() {
        return idxTArchive;
    }
}

public class Subscriber implements Callback {
    LinkedList<Event> listEvent = new LinkedList<>();
    List<Event> listConsumed = new ArrayList<>();
    
    final private Integer id;
    
    public Subscriber(Integer id) {
        this.id = id;
    }
    
    @Override
    public void event(Event e) {
        synchronized(this) {
            listEvent.add(e);
        }
    }
    
    @Override
    public Integer id() {
        return id;
    }
    
    public Event consume() {
        Event event = null;
        synchronized(this) {
            event = listEvent.poll();
        }
        if(event == null) {
            return null;
        }
        processEvent(event);
        synchronized(this) {
            listConsumed.add(event);
        }
        return event;
    }
    
    protected void processEvent(Event event) {
        
    }
}

/**
 * SubscriberContainer has a collection of subscriptions.
 *
 */
public class SubscriberContainer {
    
}

/**
 * PublisherContainer has a collection of publishers.
 */
public class PublisherContainer {
    
}

/**
 * PublisherSubscriberContainer has a collection of 
 * subscriptions and publications.
 *
 */
public class PublisherSubscriberContainer {
    
}

public class SubscriberEntity extends Subscriber {

    public SubscriberEntity(Integer id) {
        super(id);
    }
    
}

public class PublisherEntity extends Publisher {

    public PublisherEntity(Integer id) {
        super(id);
    }
    
}

}



