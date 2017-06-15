/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.lipptobusch.shareit.persistence;

import edu.hm.lipptobusch.shareit.models.Medium;

import java.io.Serializable;
import java.util.List;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public interface HibernatePersistence {

    void addMedium(Medium medium);

    void updateMedium(Medium medium);

    List<Medium> getTable(Class className);

    Medium findMedium(Class className, Serializable id);

}
