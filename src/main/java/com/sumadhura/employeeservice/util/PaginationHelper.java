/**
 * 
 */
package com.sumadhura.employeeservice.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * Page class provides PaginationHelper specific services.
 * 
 * @author Venkat Koniki
 * @since 09.09.2019
 * @time 04:23PM
 */
public class PaginationHelper<E> {

	 @SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<E> fetchPage(final NamedParameterJdbcTemplate nmdPJdbcTemplate, final String sqlCountRows,final String sqlFetchRows,final  SqlParameterSource paramSource,final int pageNo,final int pageSize,final ParameterizedRowMapper<E> rowMapper) {

	        // determine how many rows are available
		    @SuppressWarnings("deprecation")
			final int rowCount = nmdPJdbcTemplate.queryForInt(sqlCountRows,paramSource);
	       // final int rowCount = jt.queryForInt(sqlCountRows, args);

	        // calculate the number of pages
	        int pageCount = rowCount / pageSize;
	        if (rowCount > pageSize * pageCount) {
	            pageCount++;
	        }
	        // create the page object
	        final Page<E> page = new Page<E>();
	        page.setPageNumber(pageNo);
	        page.setPagesAvailable(pageCount);
	        page.setRowCount(Long.valueOf(rowCount));

	        // fetch a single page of results
	        final int startRow = (pageNo - 1) * pageSize;
	        nmdPJdbcTemplate.query(
	                sqlFetchRows,
	                paramSource,
	                new ResultSetExtractor() {
	                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
	                        final List pageItems = page.getPageItems();
	                        int currentRow = 0;
	                        while (rs.next() && currentRow <startRow + pageSize) {
	                            if (currentRow >= startRow) {
	                                pageItems.add(rowMapper.mapRow(rs, currentRow));
	                            }
	                            currentRow++;
	                        }
	                        return page;
	                    }
	                });
	        return page;
	    }
}
