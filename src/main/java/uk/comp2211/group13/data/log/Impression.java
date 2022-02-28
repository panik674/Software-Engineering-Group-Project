package uk.comp2211.group13.data.log;

import uk.comp2211.group13.enums.Age;
import uk.comp2211.group13.enums.Context;
import uk.comp2211.group13.enums.Income;

/**
 * This record is used to store a row of the impression log.
 */
public record Impression(String date, String id, String gender, Age age, Income income, Context context, Float cost) { }
