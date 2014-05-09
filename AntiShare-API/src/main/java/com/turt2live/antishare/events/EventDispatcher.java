package com.turt2live.antishare.events;

import com.turt2live.antishare.collections.ArrayArrayList;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AntiShare Event Dispatcher
 *
 * @author turt2live
 */
public class EventDispatcher {

    private static class Listener {

        Method method;
        Object object;
        Class<? extends Event> eventClass;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Listener)) return false;

            Listener listener = (Listener) o;

            if (eventClass != null ? !eventClass.equals(listener.eventClass) : listener.eventClass != null)
                return false;
            if (method != null ? !method.equals(listener.method) : listener.method != null) return false;
            if (object != null ? !object.equals(listener.object) : listener.object != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = method != null ? method.hashCode() : 0;
            result = 31 * result + (object != null ? object.hashCode() : 0);
            result = 31 * result + (eventClass != null ? eventClass.hashCode() : 0);
            return result;
        }
    }

    private static ConcurrentMap<String, List<Listener>> listeners = new ConcurrentHashMap<String, List<Listener>>();

    /**
     * Registers an object with the dispatcher. Invalid methods are silently ignored.
     * <p/>
     * A "valid" method is one which has the {@link com.turt2live.antishare.events.EventListener}
     * annotation as well as accepts a single argument of type {@link Event}.
     *
     * @param object the object to register. Null input is ignored.
     */
    public static void register(Object object) {
        if (object == null) return;
        List<Listener> listenerList = getListeners(object);
        for (Listener listener : listenerList) {
            List<Listener> methods = getList(listener.eventClass);
            methods.add(listener);
        }
    }

    /**
     * Removes an object from the dispatcher
     *
     * @param object the object to de-register. Null input is ignored.
     */
    public static void deregister(Object object) {
        if (object == null) return;
        List<Listener> listenerList = getListeners(object);
        for (Listener listener : listenerList) {
            List<Listener> methods = getList(listener.eventClass);
            methods.remove(listener);
        }
    }

    private static List<Listener> getListeners(Object object) {
        List<Listener> listenerList = new ArrayList<Listener>();
        Class<?> clazz = object.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())
                    && !Modifier.isStatic(method.getModifiers())
                    && method.isAnnotationPresent(EventListener.class)) {
                Class<?>[] paramaters = method.getParameterTypes();
                if (paramaters != null && paramaters.length == 1) {
                    Listener testListener = new Listener();
                    testListener.method = method;
                    testListener.object = object;

                    if (Event.class.isAssignableFrom(paramaters[0])) {
                        testListener.eventClass = (Class<? extends Event>) paramaters[0];
                        listenerList.add(testListener);
                    }
                }
            }
        }
        return listenerList;
    }

    private static List<Listener> getList(Class<? extends Event> eventClass) {
        String clazz = eventClass.getName();
        if (!listeners.containsKey(clazz)) listeners.put(clazz, new CopyOnWriteArrayList<Listener>());
        return listeners.get(clazz);
    }

    private static List<Class<? extends Event>> getEventClasses(Class<?> clazz) {
        List<Class<? extends Event>> classes = new ArrayList<Class<? extends Event>>();
        Class<?> sup = clazz.getSuperclass();

        if (Event.class.isAssignableFrom(sup)) {
            if (!classes.contains(sup)) classes.add((Class<? extends Event>) sup);

            List<Class<? extends Event>> more = getEventClasses(sup);
            for (Class<? extends Event> clazz2 : more) {
                if (!classes.contains(clazz2)) classes.add(clazz2);
            }
        }

        return classes;
    }

    /**
     * Dispatches an event to all listeners
     *
     * @param event the event to dispatch
     */
    public static void dispatch(Event event) {
        if (event == null) throw new IllegalArgumentException("Event is null");
        List<Class<? extends Event>> classes = new ArrayArrayList<Class<? extends Event>>(event.getClass(), Event.class);

        // Determine all subclass events (derived/generic listeners)
        classes.addAll(getEventClasses(event.getClass()));

        for (Class<? extends Event> clazz : classes) {
            List<Listener> listenerList = getList(clazz);
            if (listenerList == null) return; // Don't fire nothing
            for (Listener listener : listenerList) {
                try {
                    listener.method.invoke(listener.object, event);
                } catch (Exception e) {
                    throw new RuntimeException("Cannot access method: " + e.getMessage());
                }
            }
        }
    }

}
