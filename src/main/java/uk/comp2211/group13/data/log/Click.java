package uk.comp2211.group13.data.log;

/**
 * This record is used to store a row of the click log.
 */
// Change dates to Date object.
public record Click(
    String date,
    String id,
    Float cost
) {}
