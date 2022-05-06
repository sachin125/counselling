package com.inn.counselling.security.mgmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import com.inn.counselling.security.authentication.CustomerInfo;
import com.inn.counselling.security.mgmt.PasswordExpiryConstants.PersistDetailBeanCondition;

/**
 * This helper service is responsible for loading and storing password expiry details and password feature configuration.
 *
 */
public class PasswordExpiryLoadStoreService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordExpiryLoadStoreService.class);


	@Autowired(required=true)
	@Qualifier("dataSource")
	private DataSource datasource;

	//private JdbcTemplate jdbcTemplate;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private SimpleJdbcCall jdbcCall;

	private static final String SQL_STATEMENT_CONSTANT="SQL_STATEMENT";

	public void init(){
		//jdbcTemplate = new JdbcTemplate(datasource);
		jdbcCall = new SimpleJdbcCall(datasource);
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);

	}

	public enum DBOper {insert, update, delete, select, execproc};
	/**
	 * represents query name for fetching password expiry details.
	 */
	private String detailsFetchQuery;
	/**
	 * represents query name for fetching features configuration.
	 */
	private String featureFetchQuery;
	/**
	 * represents query name for Setting features configuration.
	 */
	private String featureSetQuery; 
	/**
	 * represents query name for updating password expiration details on successful authentication.
	 */
	private String successDetailsUpdateQuery;
	/**
	 * represents query name for updating password expiration details on unsuccessful authentication.
	 */
	private String unsuccessDetailsUpdateQuery;
	/**
	 * represents query name for fetching user details.
	 */
	private String userDetailsQuery;
	/**
	 * This method executes the query which load or store the password expiry details
	 * or features.
	 * 
	 * Parameters passed to this method must include the following:
	 *      queryname : name of the which has to be executed
	 *      db_action : query type like select, insert or update.
	 *  
	 * It is assumed by default the database operation is via query. If other operation
	 * is performed an action parameter will be required.
	 *  
	 * @param params
	 * @return
	 * @throws ResourceLoaderException
	 */
	private List<Map<String, Object>> execute (Map<String, Object> params, DBOper op)
	{
		LOGGER.info("inside @class PasswordExpiryLoadStoreService @method execute @param : op: "+op);
		if(params == null){
			params = new HashMap<String, Object>();
		}

		if(CustomerInfo.getCustomerInfo() != null)
		{
			if(!params.containsKey("username")){
				params.putAll(CustomerInfo.getCustomerInfo());
			}
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("inside execute method with params["+params+"]");
		}

		String sql = (String)params.remove(SQL_STATEMENT_CONSTANT);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Going to execute ["+sql+"] with parameters["+params+"]");
		}

		List<Map<String, Object>> results = null;

		if(op.equals(DBOper.select))
		{
			results = namedParameterJdbcTemplate.queryForList(sql, params);
			LOGGER.debug("Going to execute 2 ["+sql+"] with parameters["+params+"]");
		}
		else
		{
			if(params.containsKey("LOCKED")){
				params.put("locked", params.remove("LOCKED"));
			}
			if(params.containsKey("USERNAME")){
				params.put("username", params.remove("USERNAME"));
			}
			if(params.containsKey("DOMAINNAME")){
				params.put("domainname", params.remove("DOMAINNAME"));
			}
			if(params.containsKey("FAILED_ATTEMPTS")){
				params.put("failed_attempts", params.remove("FAILED_ATTEMPTS"));
			}
			namedParameterJdbcTemplate.update(sql, params);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("result generated after executing query ["+results+"]");
		}
		return results;
	}
	private List<Map<String, Object>> executeCall (Map<String, Object> params)
	{
		String username=null;
		if(params.get("username")==null)
		{

			CustomerInfo ci=new  CustomerInfo();
			if( ci.getUserInContext()!=null)
			{
				username=ci.getUserInContext().getUsername();

			}

		}
		else{
			username= params.get("username").toString();
		}
		String procedureName = (String)params.remove("PROC_NAME");
		String procedureCondition=null;
		if(params.get("CONDITION")!=null)
		{
			procedureCondition=(String)params.remove("CONDITION");
		}

		if(CustomerInfo.getCustomerInfo() != null)
		{
			params.putAll(CustomerInfo.getCustomerInfo());
		}

		LOGGER.debug("Going to execute procedure["+procedureName+"] with parameters["+params+"]");
		new MapSqlParameterSource().addValues(params);
		PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();
		namedParameterJdbcTemplate.update("update password_expiry_details set locked=0, failed_attempts=0,first_time_login=0,last_updated_dt= now() ,history='"+params.get(PasswordExpiryConstants.PASSWORD_HISTORY).toString()+"'  where username = '"+params.get("username").toString()+"'",new HashMap<String, String>());
		namedParameterJdbcTemplate.update("update password_feature_config set first_time_change=0 where username = '"+params.get("username").toString()+"'",new HashMap<String, String>());

		namedParameterJdbcTemplate.update("update users set password='"+params.get("password").toString()+"'  where username = '"+params.get("username").toString()+"'",new HashMap<String, String>());


		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		LOGGER.debug("Result after executing procedure["+results+"]");
		return results;
	}
	/**
	 * persist the give detail bean to the database. When using this method it is assumed that authentication is successful 
	 * and customer related properties shall be picked from security context.
	 * 
	 * @param detailBean
	 * @param authenticatedSuccessfully this parameter shall be used to determine the query name for persisting the bean
	 */
	public void persist(PasswordExpiryDetailBean detailBean, PersistDetailBeanCondition condition)
	{
		persist(null, detailBean, condition);
	}
	/**
	 * persist the provided detail bean into database. Customer information passed as parameter shall be used
	 * for persisting the bean.
	 * 
	 * @param customerInfo
	 * @param detailBean
	 * @param condition this parameter shall be used to determine the query name for persisting the bean
	 */
	public void persist(Map<String, Object> customerInfo, PasswordExpiryDetailBean detailBean, PersistDetailBeanCondition condition)
	{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("persisting detailBean["+detailBean+"] to database");
		}

		Map<String, Object> params = detailBean.getContext();

		if(params==null || params.size() == 0){
			LOGGER.warn("Unable to persist the empty detail bean.");
			return;
		}

		if(customerInfo != null){
			params.putAll(customerInfo);
		}

		if(PersistDetailBeanCondition.AUTH_SUCCESS.equals(condition)){
			params.put("PROC_NAME", getSuccessDetailsUpdateQuery());
			executeCall(params);
		}
		else if (PersistDetailBeanCondition.AUTH_FAILURE.equals(condition)){
			params.put(SQL_STATEMENT_CONSTANT, getUnsuccessDetailsUpdateQuery());
			execute(params, DBOper.update);
		}
		else if(PersistDetailBeanCondition.ATTEMPT_RESET.equals(condition)){
			params.put(SQL_STATEMENT_CONSTANT, getUnsuccessDetailsUpdateQuery());
			execute(params, DBOper.update);
		}
		else  if(PersistDetailBeanCondition.PASS_CHANGE.equals(condition)){
			params.put("PROC_NAME", getSuccessDetailsUpdateQuery());
			params.put("CONDITION", PersistDetailBeanCondition.PASS_CHANGE.toString());
			executeCall(params);

		}else{
			Assert.isTrue(false, "Condition invalid ["+condition+"]");
		}


		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("done with persisting detail bean.");
		}
	}
	/**
	 * returns the password expiry detail bean after querying the database. 
	 * CustomerContext parameters are internally used for querying, so one need not set these
	 * explicitly.
	 * 
	 * By default this method adds queryname as specified in bean detailsFetchQuery parameter.
	 * Also, adds db_action param as fetch, since db operation is a fetch query.
	 * 
	 * @param params holder for additional query parameters
	 * @return
	 */
	public PasswordExpiryDetailBean getPasswordExpiryDetail(Map<String, Object> params)
	{
		LOGGER.info("inside @class PasswordExpiryLoadStoreService @method getPasswordExpiryDetail @param : ");

		if(params == null){
			params = new HashMap<String, Object>();
		}
		// add the correct query name to fetch password expiry details
		params.put(SQL_STATEMENT_CONSTANT, getDetailsFetchQuery());

		// fetch the result by executing the query.
		List<Map<String, Object>> result = execute(params,DBOper.select);


		if(result != null && result.size()>1){
			throw new RuntimeException("Multiple result returned instead of single rows");
		}

		Map<String, Object> resultMap = null;
		if(result.size() == 1 && !result.get(0).isEmpty()){
			resultMap = result.get(0);
		}
		// return the result in form of PasswordExpiryDetail bean.
		if(resultMap != null){
			return new PasswordExpiryDetailBean(resultMap);
		}
		else{
			return null;
		}
	}

	/**
	 * returns the password expiry detail bean after querying the database. 
	 * CustomerContext parameters are internally used for querying.
	 * 
	 * By default this method adds queryname as specified in bean detailsFetchQuery parameter.
	 * Also, adds db_action param as fetch, since db operation is a fetch query.
	 * 
	 * @return
	 */
	public PasswordExpiryDetailBean getPasswordExpiryDetail()
	{
		return getPasswordExpiryDetail(null);
	}
	/**
	 * returns the password feature configuration detail after querying the database. 
	 * CustomerContext parameters are internally used for querying, so one need not set these
	 * explicitly.
	 * 
	 * By default this method adds queryname as specified in bean featureFetchQuery parameter.
	 * Also, adds db_action param as fetch, since db operation is a fetch query.
	 * 
	 * @param params holder for additional query parameters
	 * @return
	 */
	public PasswordFeatureBean getPasswordFeatures(Map<String, Object> params)
	{
		LOGGER.info("inside @class PasswordExpiryLoadStoreService @method getPasswordFeatures @param : ");
		if(params == null)
		{
			params = new HashMap<String, Object>();
		}
		// add the correct query name to fetch password feature configurations.
		params.put(SQL_STATEMENT_CONSTANT, getFeatureFetchQuery());

		// fetch the result by executing the query.
		List<Map<String, Object>> result = execute(params,DBOper.select);
		if(result != null && result.size()>1){
			throw new RuntimeException("Multiple result returned instead of single rows");
		}

		Map<String, Object> resultMap = null;
		if(result.size() == 1 && !result.get(0).isEmpty()){
			resultMap = result.get(0);

		}
		if(resultMap != null){
			return new PasswordFeatureBean(resultMap);
		}
		else{
			return null;
		}
	}
	/**
	 * returns the password feature configuration detail after querying the database. 
	 * CustomerContext parameters are internally used for querying, so one need not set these
	 * explicitly.
	 * 
	 * By default this method adds queryname as specified in bean featureFetchQuery parameter.
	 * Also, adds db_action param as fetch, since db operation is a fetch query.
	 * 
	 * @return
	 */
	public PasswordFeatureBean getPasswordFeatures()
	{
		return getPasswordFeatures(null);
	}
	/**
	 * returns the user details using provided parameters which indeed is customer context information.
	 * @param params
	 * @return
	 */
	public Map<String, Object> getUserDetails(Map<String, Object> params) 
	{
		LOGGER.info("inside @class PasswordExpiryLoadStoreService @method getUserDetails @param : ");
		if(params == null)
		{
			params = new HashMap<String, Object>();
		}
		// add the correct query name to fetch password feature configurations.
		params.put(SQL_STATEMENT_CONSTANT, getUserDetailsQuery());

		// fetch the result by executing the query.
		List<Map<String, Object>> result = execute(params,DBOper.select);
		if(result != null && result.size()>1){
			throw new RuntimeException("Multiple result returned instead of single rows");
		}

		Map<String, Object> resultMap = null;
		if(result.size() == 1 && !result.get(0).isEmpty()){

			resultMap = result.get(0);

		}
		if(resultMap != null){
			return resultMap;
		}
		else{
			return null;
		}
	}
	/**
	 * @return
	 */
	public String getDetailsFetchQuery() 
	{
		return detailsFetchQuery;
	}
	/**
	 * @param detailsFetchQueryName
	 */
	public void setDetailsFetchQuery(String detailsFetchQuery) 
	{
		this.detailsFetchQuery = detailsFetchQuery;
	}
	/**
	 * @return
	 */
	public String getFeatureFetchQuery() 
	{
		return featureFetchQuery;
	}
	/**
	 * @param featureFetchQueryName
	 */
	public void setFeatureFetchQuery(String featureFetchQuery) 
	{
		this.featureFetchQuery = featureFetchQuery;
	}
	/**
	 * @return
	 */
	public String getSuccessDetailsUpdateQuery() 
	{
		return successDetailsUpdateQuery;
	}
	/**
	 * @param successDetailsUpdateQueryName
	 */
	public void setSuccessDetailsUpdateQuery(
			String successDetailsUpdateQuery) 
	{
		this.successDetailsUpdateQuery = successDetailsUpdateQuery;
	}
	/**
	 * @return
	 */
	public String getUnsuccessDetailsUpdateQuery() 
	{
		return unsuccessDetailsUpdateQuery;
	}
	/**
	 * @param unsuccessDetailsUpdateQueryName
	 */
	public void setUnsuccessDetailsUpdateQuery(
			String unsuccessDetailsUpdateQuery) 
	{
		this.unsuccessDetailsUpdateQuery = unsuccessDetailsUpdateQuery;
	}

	/**
	 * @return
	 */
	public String getUserDetailsQuery() 
	{
		return userDetailsQuery;
	}
	/**
	 * @param userDetailsQueryName
	 */
	public void setUserDetailsQuery(String userDetailsQuery) 
	{
		this.userDetailsQuery = userDetailsQuery;
	}
	/** 
	 * 
	 * @return
	 */
	public String getFeatureSetQuery() 
	{
		return featureSetQuery;
	}
	/**
	 * 
	 * @param featureSetQuery
	 */
	public void setFeatureSetQuery(String featureSetQuery) 
	{
		this.featureSetQuery = featureSetQuery;
	}

	/**
	 * Stores the password feature information for the provisioned User passed in param as "provisionedUser" key.
	 * @param params
	 **/
	public void setPasswordFeatures(Map<String, Object> params) {

		if(params == null)
		{
			params = new HashMap<String, Object>();
		}
		// add the correct query name to fetch password feature configurations.
		params.put(SQL_STATEMENT_CONSTANT, getFeatureSetQuery());

		// set the password feature by setting the query.
		execute(params,DBOper.update);		
	}
	public void setPasswordFeaturesbyDomain(Map<String, Object> params) {

		if(params == null)
		{
			params = new HashMap<String, Object>();
		}
		// add the correct query name to fetch password feature configurations.
		params.put(SQL_STATEMENT_CONSTANT, getFeatureSetQuery());

		// set the password feature by setting the query.
		executebyDomain(params,DBOper.update);		
	}

	private List<Map<String, Object>> executebyDomain (Map<String, Object> params, DBOper op)
	{
		if(params == null){
			params = new HashMap<String, Object>();
		}

		if(CustomerInfo.getCustomerInfo() != null)
		{


			Map<String, String> map= CustomerInfo.getCustomerInfo();
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				if(!entry.getKey().equalsIgnoreCase("domainname"))
				{
					params.put(entry.getKey(),entry.getValue());
				}
			}
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("inside execute method with params["+params+"]");
		}

		String sql = (String)params.remove(SQL_STATEMENT_CONSTANT);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Going to execute ["+sql+"] with parameters["+params+"]");
		}

		List<Map<String, Object>> results = null;

		if(op.equals(DBOper.select))
		{
			results = namedParameterJdbcTemplate.queryForList(sql, params);
		}
		else
		{
			namedParameterJdbcTemplate.update(sql, params);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("result generated after executing query ["+results+"]");
		}
		return results;
	}
}
