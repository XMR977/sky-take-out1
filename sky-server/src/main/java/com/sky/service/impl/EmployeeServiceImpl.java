package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;




    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);


        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);

        }


        //密码比对
        // TODO 进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     *new employee
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {

            Employee employee= new Employee();

            //copy properties from DTO to employee
        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
//        employee.setCreateTime(now());
//        employee.setUpdateTime(now());

        //maybe works in this way?
//        HttpServletRequest request;
//        String token = request.getHeader(jwtProperties.getAdminTokenName());
//        Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
//        Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
        //get user id from ThreadLocal!
//        Long userid = BaseContext.getCurrentId();
//        employee.setCreateUser(userid);
//        employee.setUpdateUser(userid);
//
//        BaseContext.removeCurrentId();

        employeeMapper.insert(employee);

    }

    /**
     *page query
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult page(EmployeePageQueryDTO employeePageQueryDTO) {

        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.query(employeePageQueryDTO);
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        PageResult pageResult = new PageResult(total,records);

        return pageResult;
    }

    /**
     * status
     * @param status
     * @param id
     */
    @Override
    public void status(Integer status, Long id) {

//        Long userid = BaseContext.getCurrentId();
//        Employee employee = new Employee();
//        employee.setUpdateTime(now());

//        employee.setUpdateUser(userid);

        Employee employee = Employee.builder()
                .status(status)
                .id(id)
//                .updateTime(now())
//                .updateUser(userid)
                .build();

        employeeMapper.status(employee);
    }

    @Override
    public Employee findbyid(Long id) {

        Employee employee = employeeMapper.findbyid(id);
        employee.setPassword("****");
        return  employee;
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
//        Long userid = BaseContext.getCurrentId();
//        employee.setUpdateTime(now());
//        employee.setUpdateUser(userid);
        employeeMapper.status(employee);
    }


}
