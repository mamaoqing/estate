package com.estate.sdzy.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 城市
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RCity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * Code
     */
    private String cityCode;

    private String cityName;

    /**
     * ???
     */
    private Long provinceId;


}
