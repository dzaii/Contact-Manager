package com.ingsoftware.contactmanager.swagger;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
        @ApiImplicitParam(name = "page",
                          paramType = "query",
                          dataTypeClass = Integer.class,
                          example = "0",
                          value = "Results page you want to retrieve (0..N)"),

        @ApiImplicitParam(name = "size",
                          paramType = "query",
                          dataTypeClass = Integer.class,
                          example = "20",
                          value = "Number of records per page."),

        @ApiImplicitParam(name = "sort",
                          allowMultiple = true,
                          paramType = "query",
                          dataTypeClass = String.class ,
                          value = "Sorting criteria in the format: property(,asc|desc). "
                                + "Default sort order is ascending. " + "Multiple sort criteria are supported.")})
public @interface ApiPageable {
}
