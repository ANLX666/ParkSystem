package com.imust.mapper;

import java.util.List;

import com.imust.entity.VipShenQing;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.imust.entity.Users;
@Mapper
public interface UserMapper {
	
	//添加信息
	@Insert("insert into User(name,phone,plate_num,password,createDate,stauts,vipStaut) values(#{name},#{phone},#{plate_num},#{password},SYSDATE(),0,0)")
    public void insertUsers(Users users);
	
	//删除信息
	@Delete("delete from User where id=#{id}")
	public void deleteUserById(int id);
	
	//修改信息
	@Update("update user set stauts=#{stauts} where id=#{id}")
	public void updateUserStauts(@Param("id") int id,@Param("stauts") int stauts);
	
	@Update("update user set point=#{point} where id=#{id}")
	public void updateUserPoint(Users user);
	
	@Update("update user set phone=#{phone},plate_num=#{plate_num} where id=#{id}")
	public void updateUser(Users user);
	@Update("update user set password=#{password} where id=#{id}")
	public void updateUserPwd(Users user);
	//查询信息
	@Select("select * from user where name like #{name}")
	List<Users> findByName(@Param("name") String name);
	
	@Select("select * from user")
	List<Users> findAllUser();
	
	@Select("select * from user where id=#{id}")
	Users findUserById(@Param("id") int id);
	
	@Select("select * from user order by point desc")
	List<Users> findAllPoint();
	
	@Select("select * from user where name like #{name} order by point desc")
	List<Users> findPointByName(@Param("name") String name);
	
	//登陆使用
	@Select("select * from user where name=#{name} and password = #{password}")
	List<Users> findUserByNameAndPwd(@Param("name") String adminName,@Param("password") String password);

	//添加会员审批列表
	@Insert("INSERT INTO vipShenPi (vipId) VALUES (#{uId})")
    boolean addShenPiList(@Param("uId") int userId);

	//查询会员审批列表
	@Select("   select * from user  inner join vipShenPi\n" +
			"on user.id=vipShenPi.vipId  ")
	List<Users> queryVipList();

	@Update(" UPDATE user SET vipStaut=0 WHERE (id=#{uId})   ")
	boolean stopVip(@Param("uId") int id, int staut);

	@Update(" UPDATE user SET vipStaut=1 WHERE (id=#{uId})   ")
	boolean sartVip(@Param("uId") int id, int staut);

	//删除审批列表中的信息
	@Delete("   delete  from vipShenPi where  vipId=#{uId} ")
	void deleteShenpiListById(@Param("uId") int id);

	@Select("select id from vipShenPi where vipId = #{userId}")
    List<VipShenQing> queryVipListByUserId(@Param("userId") int userId);
	@Delete("   delete  from vipShenPi where  id=#{id} ")
	boolean deleteShenPiId(@Param("id") int id);
}
