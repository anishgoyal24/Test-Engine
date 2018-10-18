package com.anish.testengine.utils;


public interface SQLConstants {
	String LOGIN_SQL = "select user_mst.userid, user_mst.password,role_mst.rolename"
			+ ",right_mst.rightname ,right_mst.screen from "
			+ "user_mst,role_mst,user_role_mapping,right_mst,role_right_mapping "
			+ " where user_mst.uid=user_role_mapping.uid and role_mst.roleid=user_role_mapping.roleid "
			+ " and role_mst.roleid=role_right_mapping.roleid and "
			+ "right_mst.rid=role_right_mapping.rid and user_mst.userid=?  "
			+ "and user_mst.password=? and user_mst.status='y'";
	
	String UPLOAD_QUES_SQL = "insert into questions(id, name, optiona, optionb, optionc, optiond, answer, positive"
			+ ", negative, test_code) values(?,?,?,?,?,?,?,?,?,?)";
	String UPDATE_TEST_NAME = "insert into test values(?,?)";
	String GET_TEST_LIST = "select * from test";
	String GET_COMPLETED_TEST = "select test_code from user_test_mapping where userid = ?";
	String GET_QUESTIONS = "select * from questions where test_code = ?";
	String OUTPUT_ANSWER = "insert into user_test_mapping values(?,?,?,?,?,?)";
	String ADD_TO_USER = "insert into user_mst(userid, password) values(?,?)";
	String ADD_ROLE = "insert into user_role_mapping values(?,?)";
	String GET_UID = "select uid from user_mst where userid = ?";
	String GET_RID = "select roleid from role_mst where rolename = ?";
	String DELETE_USER = "update user_mst set status = 'n' where userid = ?";
	String CHANGE_PASSWORD = "update user_mst set password = ? where userid = ?";
}
