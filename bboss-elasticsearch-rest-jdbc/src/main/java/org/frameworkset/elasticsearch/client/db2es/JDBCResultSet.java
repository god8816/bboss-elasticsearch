package org.frameworkset.elasticsearch.client.db2es;/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData;
import com.frameworkset.orm.adapter.DB;
import org.frameworkset.elasticsearch.client.ESDataImportException;
import org.frameworkset.elasticsearch.client.tran.TranMeta;
import org.frameworkset.elasticsearch.client.tran.TranResultSet;

import java.sql.ResultSet;

public class JDBCResultSet implements TranResultSet {
	protected ResultSet resultSet;
	protected TranMeta metaData;
	protected DB dbadapter;
	public ResultSet getResultSet() {
		return resultSet;
	}
	public DB getDbadapter(){
		return dbadapter;
	}
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public TranMeta getMetaData() {
		return metaData;
	}

	public void setMetaData(PoolManResultSetMetaData metaData) {
		this.metaData = new JDBCTranMetaData(metaData);
	}

	public boolean isOracleTimestamp(int sqlType){
		return dbadapter.isOracleTimestamp( sqlType);
	}

	public void setDbadapter(DB dbadapter) {
		this.dbadapter = dbadapter;
	}

	public Object getValue(  int i, String colName,int sqlType) throws ESDataImportException
	{
		try {
			if(!this.isOracleTimestamp(sqlType)) {
				return this.resultSet.getObject(i + 1);
			}
			else{
				return this.resultSet.getTimestamp(i + 1);
			}
		}
		catch (Exception ex){
			throw new ESDataImportException(ex);
		}

	}

	public Object getValue( String colName) throws ESDataImportException
	{
		if(colName == null)
			return null;
		try {
			Object value = this.resultSet.getObject(colName);
			return value;
		}
		catch (Exception ex){
			throw new ESDataImportException(ex);
		}

	}


	public Object getValue( String colName,int sqlType) throws ESDataImportException
	{
		if(colName == null)
			return null;
		try {
			if(!this.isOracleTimestamp(sqlType)) {
				return this.resultSet.getObject(colName);
			}
			else{
				return this.resultSet.getTimestamp(colName);
			}
		}
		catch (Exception ex){
			throw new ESDataImportException(ex);
		}


	}

	public Object getDateTimeValue( String colName) throws ESDataImportException
	{
		if(colName == null)
			return null;
		try {
			Object value = this.resultSet.getTimestamp(colName);
			return value;
		}
		catch (Exception e){
			try {
				Object value = this.resultSet.getDate(colName);
				return value;
			}
			catch (Exception ex){
				throw new ESDataImportException(ex);
			}

		}
	}


	public boolean next() throws ESDataImportException {
		try {
			return resultSet.next();
		}
		catch (Exception e){
			throw new ESDataImportException(e);
		}
	}
}