package uk.comp2211.group13.data.log;

/**
 * This record is used to store a row of the impression log.
 */
public record Impression(String date, String id, String gender, String age, String income, String context, Float cost) { }
