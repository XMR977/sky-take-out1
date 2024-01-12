package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     *
     * @param employee
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "VALUES (#{name}, #{username},#{password}, #{phone}, #{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser}) ")
    void insert(Employee employee);

    /**
     *page query
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> query(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * update
     * @param employee
     */
    void status(Employee employee);

    /**
     * find by id
     * @param id
     */
    @Select("select * from employee where id = #{id}")
    Employee findbyid(Long id);
}
