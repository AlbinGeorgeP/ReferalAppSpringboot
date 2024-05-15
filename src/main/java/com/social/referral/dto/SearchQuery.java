package com.social.referral.dto;

import lombok.Data;
import java.util.List;

@Data
public class SearchQuery {
    private Integer fromRowNum;
    private Integer resultSize;
    private List<SingleValueFilter> singleValueFilters;
    private SortField sortField;
}
