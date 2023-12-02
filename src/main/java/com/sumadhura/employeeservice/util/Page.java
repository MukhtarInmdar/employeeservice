/**
 * 
 */
package com.sumadhura.employeeservice.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Page class provides Pagination specific services.
 * 
 * @author Venkat Koniki
 * @since 09.09.2019
 * @time 04:23PM
 */

@Getter
@Setter
@ToString
public class Page<E> {
	
	 private int pageNumber;
     private int pagesAvailable;
     private List<E> pageItems = new ArrayList<E>();
     private Long rowCount;
}
