package com.social.referral.services;

import com.social.referral.config.JwtUtil;
import com.social.referral.dto.*;
import com.social.referral.entities.Company;
import com.social.referral.entities.Role;
import com.social.referral.entities.User;
import com.social.referral.entities.UserCompanyView;
import com.social.referral.repository.CompanyRepository;
import com.social.referral.repository.UserRepository;
import com.social.referral.utils.ReferralAppConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService{

    private final EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyService companyService;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;


    @Cacheable(value = "my-cache")
    public List<UserDTO> getAllUsers(){
        List<UserDTO> usersDto=new ArrayList<>();
        List<User> users= userRepository.getUsersByisActive(1);
        for (User user:users) {
            usersDto.add(getUserDto(user));
        }
        return usersDto;
    }

    @Transactional
    public String addUser(UserDTO userDTO){
        String name= userDTO.getCompany();
        User user= getUserEntity(userDTO);
        user.setIsActive(1);
        user.setCreatedTime(new Date());
        userRepository.save(user);
        return  jwtUtil.generateToken(user);
    }


    @Transactional
    public String updateUser(UserDTO userDTO) throws Exception {
        String name= userDTO.getCompany();
        User repoUser=userRepository.findById(userDTO.getId()).orElse(new User());
        if(repoUser.getId() == null)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"User Not Found");
        User user= getUserEntity(userDTO);
        if(user.getIsActive()==0)
        {
            user.setDeactivatedTime(new Date());
        }
        userRepository.save(user);
        return "success";

    }

    public UserDTO getUser(Integer id) {
        User repoUser=userRepository.findById(id).orElse(new User());
        UserDTO user = new UserDTO().toBuilder().id(repoUser.getId()).email(repoUser.getEmail()).tag(repoUser.getTag()).mobileNo(repoUser.getMobileNo()).company(repoUser.getCompany().getName()).isActive(repoUser.getIsActive()).createdTime(repoUser.getCreatedTime()).name(repoUser.getName()).deactivatedTime(repoUser.getDeactivatedTime()).build();
        return user;
    }

    private UserDTO getUserDto(User repoUser) {

        UserDTO user = new UserDTO().toBuilder().id(repoUser.getId()).email(repoUser.getEmail()).tag(repoUser.getTag()).mobileNo(repoUser.getMobileNo()).company(repoUser.getCompany().getName()).isActive(repoUser.getIsActive()).createdTime(repoUser.getCreatedTime()).name(repoUser.getName()).deactivatedTime(repoUser.getDeactivatedTime()).build();
        return user;
    }

    private User getUserEntity(UserDTO userDTO) {

        if(companyRepository.findByName(userDTO.getCompany())==null)
            companyService.addCompany(userDTO.getCompany());
        Company company=companyRepository.findByName(userDTO.getCompany());
        User user= new User().toBuilder().id(userDTO.getId()).email(userDTO.getEmail()).tag(userDTO.getTag()).mobileNo(userDTO.getMobileNo()).company(company).isActive(userDTO.getIsActive()).createdTime(userDTO.getCreatedTime()).name(userDTO.getName()).deactivatedTime(userDTO.getDeactivatedTime()).password(passwordEncoder.encode(userDTO.getPassword())).role(Role.USER).build();
        return user;
    }
    public String addRoleToUser(RoleAddDTO request)
    {
        User repoUser=userRepository.findById(Integer.valueOf(request.getUserId())).orElse(new User());
        repoUser.setRole(Role.valueOf(request.getRole().toUpperCase()));
        userRepository.save(repoUser);
        return "Success";
    }

    public UserDetails findUserByEmail(String userEmail) {
        return userRepository.getUserByEmail(userEmail).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"User Not Found"));
    }

public List<UserCompanyView> searchUsers(SearchQuery searchQuery)
    {
        Query jpaQuery=getFilterCondition(searchQuery);
        jpaQuery.setFirstResult(ObjectUtils.isNotEmpty(searchQuery.getFromRowNum())?searchQuery.getFromRowNum():0);
        jpaQuery.setMaxResults(ObjectUtils.isNotEmpty(searchQuery.getResultSize())?searchQuery.getResultSize():5);
        return jpaQuery.getResultList();

    }

    private Query getFilterCondition(SearchQuery searchQuery) {
        Map<String,Object> parameterMap=new HashMap<>();
        List<String> whereClause=new ArrayList<>();
        StringBuilder queryBuilder=new StringBuilder();
        String queryString;
        String sortOrder;
        String sortField;
        queryBuilder.append(ReferralAppConstants.USER_COMPANY_VIEW_FILTER_QUERY);
        sortOrder= ReferralAppConstants.ASC;
        sortField=ReferralAppConstants.ID;

        if(ObjectUtils.isNotEmpty(searchQuery.getSingleValueFilters()))
        {
            queryBuilder.append(ReferralAppConstants.WHERE_CLAUSE);
            filterListQueryBuilder(searchQuery.getSingleValueFilters(),whereClause,parameterMap);
            queryBuilder.append(StringUtils.join(whereClause,ReferralAppConstants.AND_CLAUSE));
        }

        if (ObjectUtils.isNotEmpty(searchQuery.getSortField()))
        {
            sortOrder=searchQuery.getSortField().getOrder().toUpperCase();
            sortField=searchQuery.getSortField().getFieldName();

        }

        queryString=sortFieldQueryBuilder(sortOrder,sortField,queryBuilder);
        Query jpaQuery=entityManager.createQuery(queryString,UserCompanyView.class);
        if(!parameterMap.isEmpty()) {
            for (String key : parameterMap.keySet()) {
                jpaQuery.setParameter(key, parameterMap.get(key));
            }
        }
        return jpaQuery;
    }
    private String sortFieldQueryBuilder(String sortOrder, String sortField, StringBuilder queryBuilder) {
        String queryString;
        queryString=queryBuilder.append(ReferralAppConstants.ORDER_BY).append(sortField).append(" ").append(sortOrder).toString();
        return  queryString;
    }

    private void filterListQueryBuilder(List<SingleValueFilter> singleValueFilters, List<String> whereClause, Map<String, Object> parameterMap) {

        singleValueFilters.forEach(searchField ->{
            String fieldName="";
            if(StringUtils.isNoneBlank(searchField.getKey()))
            {
                fieldName=searchField.getKey();
            }
            if (StringUtils.isNoneBlank(fieldName))
            {
                if(StringUtils.equals(fieldName,ReferralAppConstants.ID))
                {
                    whereClause.add(" (e.id) in (:id)" );
                    parameterMap.put("id",searchField.getValue());
                }
                if(StringUtils.equals(fieldName,ReferralAppConstants.CREATED_TIME))
                {
                    whereClause.add(" (e.createdTime) = (:createdTime)" );
                    parameterMap.put("createdTime",searchField.getValue());
                }
                if(StringUtils.equals(fieldName,ReferralAppConstants.DEACTIVATED_TIME))
                {
                    whereClause.add(" (e.deactivatedTime) = (:deactivatedTime)" );
                    parameterMap.put("deactivatedTime",searchField.getValue());
                }

                if(StringUtils.equals(fieldName,ReferralAppConstants.COMPANY))
                {
                    whereClause.add(" (e.company) = (:company)" );
                    parameterMap.put("company",searchField.getValue());
                }
                if(StringUtils.equals(fieldName,ReferralAppConstants.IS_ACTIVE))
                {
                    whereClause.add(" (e.isActive) = (:isActive)" );
                    parameterMap.put("isActive",searchField.getValue());
                }
                if(StringUtils.equals(fieldName,ReferralAppConstants.NAME))
                {
                    whereClause.add(" (e.name) = (:name)" );
                    parameterMap.put("name",searchField.getValue());
                }

                if(StringUtils.equals(fieldName,ReferralAppConstants.EMAIL))
                {
                    whereClause.add(" (e.email) = (:email)" );
                    parameterMap.put("email",searchField.getValue());
                }
                if(StringUtils.equals(fieldName,ReferralAppConstants.MOBILE_NUMBER))
                {
                    whereClause.add(" (e.mobileNo) = (:mobileNo)" );
                    parameterMap.put("mobileNo",searchField.getValue());
                }
                if(StringUtils.equals(fieldName,ReferralAppConstants.TAG))
                {
                    whereClause.add(" (e.tag) = (:tag)" );
                    parameterMap.put("tag",searchField.getValue());
                }
                if(StringUtils.equals(fieldName,ReferralAppConstants.ROLE))
                {
                    whereClause.add(" (e.role) = (:role)" );
                    parameterMap.put("role",searchField.getValue());
                }


            }
        });
    }


}

