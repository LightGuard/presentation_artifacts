package org.ajug.event;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.TypedQuery;

import org.ajug.util.JPAUtil;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.Point;
import org.geolatte.geom.codec.Wkt;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventManagerTest {
    private javax.persistence.EntityManager EntityManager = JPAUtil.createEntityManager();

    @Test
    public void testCreateAndStoreEvent() {
        final EventManager cut = new EventManager();

        cut.createAndStoreEvent("Testing event", LocalDate.now(), "POINT(10 5)");

        final javax.persistence.EntityManager em = JPAUtil.createEntityManager();

        em.getTransaction().begin();
        List<Event> events = em.createQuery("SELECT e from Event as e").getResultList();
        assertThat(events).hasSize(1);
        em.remove(events.get(0));
        em.close();
        em.getTransaction().commit();
    }

    @Test
    public void testGeomSearch() {
        final javax.persistence.EntityManager em = JPAUtil.createEntityManager();

        em.getTransaction().begin();
        em.persist(createEvent("AJUG", LocalDate.parse("2015-11-17"), (Point) Wkt.fromWkt("POINT(0 0)")));
        em.persist(createEvent("DevNexus", LocalDate.parse("2016-02-15"), (Point) Wkt.fromWkt("POINT(0 5)")));
        em.persist(createEvent("JavaOne", LocalDate.parse("2016-09-18"), (Point) Wkt.fromWkt("POINT(-20 5)")));
        em.persist(createEvent("JavaLand", LocalDate.parse("2016-03-08"), (Point) Wkt.fromWkt("POINT(30 12)")));
        em.persist(createEvent("NFJS", LocalDate.parse("2015-09-16"), (Point) Wkt.fromWkt("POINT(0 8)")));
        em.getTransaction().commit();

        em.getTransaction().begin();
        Geometry filter = Wkt.fromWkt("POLYGON((0 0,0 10,10 10,10 0,0 0))");
        TypedQuery<Event> query = em.createQuery("select e from Event e where intersects(e.location, :filter) = true", Event.class);
        query.setParameter("filter", filter);
        List<Event> foundEvents = query.getResultList();
        assertThat(foundEvents).hasSize(3);
        em.getTransaction().commit();

        em.getTransaction().begin();
        em.createQuery("delete from Event").executeUpdate();
        em.getTransaction().commit();
    }

    private Event createEvent(String name, LocalDate date, Point location) {
        final Event e = new Event();
        e.setDate(date);
        e.setLocation(location);
        e.setTitle(name);
        return e;
    }
}
