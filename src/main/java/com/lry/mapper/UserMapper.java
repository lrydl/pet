package com.lry.mapper;

import com.lry.bean.Msg;
import com.lry.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
 
	/**
	 * 分页查询方法
	 */
	@Select("select * from  user limit #{offset},#{limit}")
	List<User> query(@Param("offset") Integer offset, @Param("limit") Integer limit);

	@Select("select * from user where id=#{id}")
	User get(@Param("id") int id);

	@Select("select * from user where username=#{username}")
	User getUserByName(@Param("username")String username);

	@Select("select * from user where username=#{username} and password=#{password}")
	User checkUser(@Param("username")String username,@Param("password")String password);

	@Update("update user set friends = #{friends} where id=#{id}")
	int updateUser(@Param("id")int id,@Param("friends")String friends);

	@Insert( "insert into user(id, username, password,friends) values(#{id}, #{username}, #{password}, #{friends})")
	int saveUser(User user);

	@Select({
			" <script> ",
			" select ",
			" username ",
			" from user",
			" where username like CONCAT('%',#{queryName},'%')",
			" and username not in ",
			" <foreach collection='filterList' item='filterName' open='(' separator=',' close=')'> ",
			" #{filterName} ",
			" </foreach> ",
			" </script> "
	})
	List<String> queryNotMyFriend(@Param("queryName")String queryName,@Param("filterList")List<String>filterList);
}
