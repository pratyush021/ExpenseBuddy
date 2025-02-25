package com.app.ExpenseMate.dao.impl;

import com.app.ExpenseMate.dao.UserDao;
import com.app.ExpenseMate.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Slf4j
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("phoneNumber").is(phoneNumber));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public User getUserByEmail(String email) {
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query, User.class);
    }
}
