package uk.comp2211.group13.data.log;

import java.util.Date;

/**
 * This record is used to store a row of the impression log.
 */

// Change dates to Date object.
public record Impression(
    Date date,
    String id,
    String gender,
    String age,
    String income,
    String context,
    Float cost,
    Click click,
    Server server
) {
}
