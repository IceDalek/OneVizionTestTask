package org.test.task.dto;

import lombok.Data;

@Data
public abstract class AbstractBook {

    protected String title;

    protected String author;

    protected String description;
}
