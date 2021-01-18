package org.bausit.admin.search;

import lombok.Value;

@Value
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
