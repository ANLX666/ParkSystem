package com.imust.mapper;

import java.util.List;

import com.imust.entity.DiQu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.imust.entity.Park;
@Mapper
public interface ParkMapper {
	

	@Select(" select * from Park p inner join diqu dq \n" +
			"on p.diquId=dq.id ")
	List<Park> findAllPark();
	
	@Select("select * from Park where  diquId = #{diquId}")
	List<Park> findAllParkByKey(@Param("diquId") String status);
	
	@Select("select * from Park where  name like #{key}")
	List<Park> findParkByKey(@Param("key") String key);
	
	@Select("select * from Park where id=#{id}")
	Park findCarById(@Param("id") int id);
	
	//添加信息
	@Insert("insert into Park(name,price,status,address,diquId,jingduX,jingduY) values(#{name},#{price},0,#{address},#{diquId},#{jingduX},#{jingduY})")
    public void insertCar(Park car);
	
	//删除信息
	@Delete("delete from Car where id=#{id}")
	public void deleteCar(int id);
	
	@Update("update Park set status=#{status} where id=#{id}")
	public void updateCarStatus(Park car);
	
	//修改信息
	@Update("update Park set name=#{name},price=#{price},address=#{address},jingduX=#{jingduX},jingduY=#{jingduY} where id=#{id}")
	public void updateCar(Park car);

	//查询所有停车地区list
	@Select("select * from diqu ")
    List<DiQu> getDiQuList();
}
