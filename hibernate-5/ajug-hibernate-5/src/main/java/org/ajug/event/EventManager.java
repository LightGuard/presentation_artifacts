/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ajug.event;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.ajug.util.JPAUtil;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryType;
import org.geolatte.geom.Point;
import org.geolatte.geom.codec.Wkt;

public class EventManager {
    public static void main(String[] args) {
        EventManager mgr = new EventManager();

        if (args[0].equals("store")) {
            mgr.createAndStoreEvent("My Event", LocalDate.now(), assemble(args));
        }
        JPAUtil.close();
    }

    void createAndStoreEvent(String title, LocalDate theDate, String wktPoint) {
        Geometry geom = wktToGeometry(wktPoint);

        if (geom.getGeometryType() != GeometryType.POINT) {
            throw new RuntimeException("Geometry must be a point. Got a " + geom.getGeometryType());
        }

        EntityManager em = JPAUtil.createEntityManager();

        em.getTransaction().begin();

        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);
        theEvent.setLocation((Point) geom);
        em.persist(theEvent);
        em.getTransaction().commit();
        em.close();
    }

    private Geometry wktToGeometry(String wktPoint) {
        return Wkt.fromWkt(wktPoint);
    }

    /**
     * Utility method to assemble all arguments save the first into a String
     */
    private static String assemble(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        return builder.toString();
    }
}
