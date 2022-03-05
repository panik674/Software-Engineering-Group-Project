package uk.comp2211.group13.data.log;

import java.util.Date;

/**
 * This record is used to store a row of the click log.
 */
// Change dates to Date object.
public record Click(
    Date date,
    String id,
    Float cost
) {}
