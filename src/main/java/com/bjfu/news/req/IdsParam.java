package com.bjfu.news.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IdsParam implements Serializable {

    private List<Long> ids;

}
