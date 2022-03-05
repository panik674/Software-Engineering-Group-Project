package uk.comp2211.group13.data.log;

/**
 * This record is used to store a row of the server log.
 */
// Change dates to Date object.
public record Server(
    String entryDate,
    String id,
    String exitDate,
    Integer pages,
    Boolean conversion
) {}
