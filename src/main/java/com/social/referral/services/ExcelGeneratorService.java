package com.social.referral.services;

import com.social.referral.dto.UserDTO;
import com.social.referral.utils.ExcelUtility;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class ExcelGeneratorService{
    @Autowired
    UserService userService;

    @Autowired
    ExcelUtility excelUtility;

    public ByteArrayInputStream exportUsers() throws IOException {
        ByteArrayOutputStream result=new ByteArrayOutputStream();
        List<String> headers= Arrays.asList("ID","Name","Email","MobileNo","isActive");
        XSSFWorkbook workbook =excelUtility.writeHeader(headers,"Referral User List");
        List<String> values=new ArrayList();
        List<UserDTO> users=userService.getAllUsers();
        int rowcount=1;
        for (UserDTO user:users)
        {
            values.clear();
            values.add(user.getId().toString());
            values.add(user.getName());
            values.add(user.getEmail());
            values.add(user.getMobileNo());
            values.add(user.getIsActive() == 1?"true":"false");
            workbook=excelUtility.write(values,workbook,rowcount);
            rowcount++;
        }
        workbook.write(result);
        return new ByteArrayInputStream(result.toByteArray());

    }
}
