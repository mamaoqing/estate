package com.estate.sdzy.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 县区
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RDistrict implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * Code
     */
    private String districtCode;

    private String districtName;

    /**
     * ???
     */
    private Long provinceId;

    private Long cityId;


}
